package com.ernesta.service;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.ernesta.model.ImportsMap;
import com.ernesta.model.RulesManager;
import com.ernesta.rules.FirstNameContractionRule;
import com.ernesta.rules.NameEqualityRule;

public class OntologyMerger {

	private final IRI iri;

	private OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
	private RulesManager rulesManager = new RulesManager();

	public OntologyMerger(File file) {
		iri = IRI.create(file);
		rulesManager.addPersonRule(new NameEqualityRule());
		rulesManager.addPersonRule(new FirstNameContractionRule());
	}

	public void merge() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		for (ImportsMap m : ImportsMap.values()) {
			manager.getIRIMappers().add(new SimpleIRIMapper(IRI.create(m.getUri()), IRI.create(ClassLoader.getSystemResource("schema/" + m.getFile()))));
		}
		try {
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(iri);

			OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

			OWLClass person = manager.getOWLDataFactory().getOWLClass(IRI.create(ImportsMap.AGENTS.getUri() + "#person"));
			NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(person, true);
			PersonHandler personHandler = new PersonHandler(manager, ontology, reasoner, rulesManager);
			personHandler.handle(instances.getFlattened());

			ontology.saveOntology(IRI.create(new File("result.ttl")));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

}
