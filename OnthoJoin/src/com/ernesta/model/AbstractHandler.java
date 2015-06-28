package com.ernesta.model;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class AbstractHandler {

	private final OWLOntology ontology;
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;
	private final OWLReasoner reasoner;
	private final RulesManager rulesManager;

	protected AbstractHandler(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner, RulesManager rulesManager) {
		this.ontology = ontology;
		this.manager = manager;
		this.reasoner = reasoner;
		this.rulesManager = rulesManager;
		this.factory = manager.getOWLDataFactory();
	}

	protected OWLOntology getOntology() {
		return ontology;
	}

	protected OWLOntologyManager getManager() {
		return manager;
	}

	protected OWLDataFactory getFactory() {
		return factory;
	}

	protected OWLReasoner getReasoner() {
		return reasoner;
	}

	protected RulesManager getRulesManager() {
		return rulesManager;
	}

	protected OWLAnnotationPropertyImpl getLabelLemma() {
		return new OWLAnnotationPropertyImpl(IRI.create(ImportsMap.ANNOTATIONS.getUri() + "#label_lemma"));
	}
}
