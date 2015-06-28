package com.nforce.service;

import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
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

public class EnumHandler extends AbstractHandler {

	protected EnumHandler(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}

	public void createClass(EnumDeclaration node, PackageDeclaration packageNode) {
		OWLClass tClass = null;
		if (Modifier.isStatic(node.getModifiers())) {
			tClass = getFactory().getOWLClass(ScroDefinitions.STATIC_MEMBER_ENUM);
		} else {
			tClass = getFactory().getOWLClass(ScroDefinitions.ENUM_TYPE);
		}

		OWLNamedIndividual tIndividual = getFactory().getOWLNamedIndividual(
				IRI.create(ScroDefinitions.SCRO + "#" + packageNode.getName() + "~" + node.getName()));
		OWLClassAssertionAxiom classAssertion = getFactory().getOWLClassAssertionAxiom(tClass, tIndividual);
		getManager().addAxiom(getOntology(), classAssertion);

		OWLIndividual classType = tIndividual;
		OWLIndividual packageName = getFactory().getOWLNamedIndividual(IRI.create(ScroDefinitions.SCRO + "#" + packageNode.getName()));

		OWLObjectProperty isPackageMemberOf = getFactory().getOWLObjectProperty(ScroDefinitions.IS_PACKAGE_MEMBER_OF);

		OWLObjectPropertyAssertionAxiom axiom = getFactory().getOWLObjectPropertyAssertionAxiom(isPackageMemberOf, classType, packageName);
		AddAxiom addAxiom = new AddAxiom(getOntology(), axiom);

		getManager().applyChange(addAxiom);

		applyAccessControl(tIndividual, node.getModifiers());
	}
}
