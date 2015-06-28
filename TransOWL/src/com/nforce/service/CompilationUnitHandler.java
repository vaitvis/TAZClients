package com.nforce.service;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.nforce.model.AbstractHandler;
import com.nforce.model.ScroDefinitions;

public class CompilationUnitHandler extends AbstractHandler {

	protected CompilationUnitHandler(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}

	public void createCompilationUnit(CompilationUnit cu, String fileName) {
		OWLNamedIndividual tIndividual = getFactory().getOWLNamedIndividual(IRI.create(ScroDefinitions.SCRO + "#" + fileName));
		OWLClass tClass = getFactory().getOWLClass(ScroDefinitions.COMPILATION_UNIT);
		OWLClassAssertionAxiom classAssertion = getFactory().getOWLClassAssertionAxiom(tClass, tIndividual);
		getManager().addAxiom(getOntology(), classAssertion);

		if (cu.getPackage() != null) {
			OWLIndividual classType = tIndividual;
			OWLIndividual packageName = getFactory().getOWLNamedIndividual(IRI.create(ScroDefinitions.SCRO + "#" + cu.getPackage().getName()));

			OWLObjectProperty belongsToPackage = getFactory().getOWLObjectProperty(ScroDefinitions.BELONGS_TO_PACKAGE);

			OWLObjectPropertyAssertionAxiom axiom = getFactory().getOWLObjectPropertyAssertionAxiom(belongsToPackage, classType, packageName);
			AddAxiom addAxiom = new AddAxiom(getOntology(), axiom);

			getManager().applyChange(addAxiom);
		}
	}

}
