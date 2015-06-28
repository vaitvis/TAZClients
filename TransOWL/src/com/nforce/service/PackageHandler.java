package com.nforce.service;

import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.nforce.model.AbstractHandler;
import com.nforce.model.ScroDefinitions;

public class PackageHandler extends AbstractHandler {

	protected PackageHandler(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}

	public void createPackageIfNeeded(PackageDeclaration node) {
		Name packageName = node.getName();
		createPackage(packageName.getFullyQualifiedName());
	}

	private void createPackage(String packageName) {
		OWLClass tClass = getFactory().getOWLClass(ScroDefinitions.PACKAGE);
		OWLNamedIndividual tIndividual = getFactory().getOWLNamedIndividual(IRI.create(ScroDefinitions.SCRO + "#" + packageName));
		OWLClassAssertionAxiom classAssertion = getFactory().getOWLClassAssertionAxiom(tClass, tIndividual);
		getManager().addAxiom(getOntology(), classAssertion);
	}

}
