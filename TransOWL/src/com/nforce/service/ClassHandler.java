package com.nforce.service;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
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

public class ClassHandler extends AbstractHandler {

	protected ClassHandler(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}

	public void createClass(TypeDeclaration node, PackageDeclaration packageNode) {
		OWLClass tClass = null;
		if (node.isInterface()) {
			tClass = getFactory().getOWLClass(ScroDefinitions.INTERFACE_TYPE);
		} else {
			if (Modifier.isAbstract(node.getModifiers())) {
				tClass = getFactory().getOWLClass(ScroDefinitions.ABSTRACT_CLASS);
			} else {
				tClass = getFactory().getOWLClass(ScroDefinitions.CONCRETE_CLASS);
			}
		}
		String className = null;
		if (packageNode != null) {
			className = packageNode.getName() + "~" + node.getName();
		} else {
			className = node.getName().toString();
		}

		OWLNamedIndividual tIndividual = getFactory().getOWLNamedIndividual(IRI.create(ScroDefinitions.SCRO + "#" + className));
		OWLClassAssertionAxiom classAssertion = getFactory().getOWLClassAssertionAxiom(tClass, tIndividual);
		getManager().addAxiom(getOntology(), classAssertion);

		if (packageNode != null) {
			OWLIndividual classType = tIndividual;
			OWLIndividual packageName = getFactory().getOWLNamedIndividual(IRI.create(ScroDefinitions.SCRO + "#" + packageNode.getName()));

			OWLObjectProperty isPackageMemberOf = getFactory().getOWLObjectProperty(ScroDefinitions.IS_PACKAGE_MEMBER_OF);

			OWLObjectPropertyAssertionAxiom axiom = getFactory().getOWLObjectPropertyAssertionAxiom(isPackageMemberOf, classType, packageName);
			AddAxiom addAxiom = new AddAxiom(getOntology(), axiom);

			getManager().applyChange(addAxiom);
		}

		applyAccessControl(tIndividual, node.getModifiers());
	}
}
