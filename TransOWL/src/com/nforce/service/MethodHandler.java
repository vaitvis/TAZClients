package com.nforce.service;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
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

public class MethodHandler extends AbstractHandler {

	protected MethodHandler(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}

	public void createMethod(MethodDeclaration node, AbstractTypeDeclaration currentClass, CompilationUnit cu) {
		OWLClass tClass = null;
		if (node.isConstructor()) {
			tClass = getFactory().getOWLClass(ScroDefinitions.CLASS_CONSTRUCTOR);
		} else {
			if (Modifier.isAbstract(node.getModifiers()) || (currentClass instanceof TypeDeclaration && ((TypeDeclaration) currentClass).isInterface())) {
				tClass = getFactory().getOWLClass(ScroDefinitions.ABSTRACT_METHOD);
			} else if (Modifier.isStatic(node.getModifiers())) {
				tClass = getFactory().getOWLClass(ScroDefinitions.STATIC_METHOD);
			} else if (Modifier.isFinal(node.getModifiers())) {
				tClass = getFactory().getOWLClass(ScroDefinitions.FINAL_METHOD);
			} else {
				tClass = getFactory().getOWLClass(ScroDefinitions.INSTANCE_METHOD);
			}
		}
		OWLNamedIndividual tIndividual = getFactory().getOWLNamedIndividual(
				IRI.create(ScroDefinitions.SCRO + "#" + currentClass.getName() + "." + node.getName()));
		OWLClassAssertionAxiom classAssertion = getFactory().getOWLClassAssertionAxiom(tClass, tIndividual);
		getManager().addAxiom(getOntology(), classAssertion);

		OWLIndividual methodName = tIndividual;

		String classTitle = null;
		if (cu.getPackage() != null) {
			classTitle = cu.getPackage().getName() + "~" + node.getName();
		} else {
			classTitle = node.getName().toString();
		}
		OWLIndividual className = getFactory().getOWLNamedIndividual(IRI.create(ScroDefinitions.SCRO + "#" + classTitle));
		OWLObjectProperty property = null;
		if (tClass.getIRI().equals(ScroDefinitions.CLASS_CONSTRUCTOR)) {
			property = getFactory().getOWLObjectProperty(ScroDefinitions.IS_CONSTRUCTOR_OF);
		} else if (tClass.getIRI().equals(ScroDefinitions.ABSTRACT_METHOD)) {
			property = getFactory().getOWLObjectProperty(ScroDefinitions.IS_ABSTRACT_METHOD_OF);
		} else if (tClass.getIRI().equals(ScroDefinitions.STATIC_METHOD)) {
			property = getFactory().getOWLObjectProperty(ScroDefinitions.IS_STATIC_METHOD_OF);
		} else if (tClass.getIRI().equals(ScroDefinitions.FINAL_METHOD)) {
			property = getFactory().getOWLObjectProperty(ScroDefinitions.IS_FINAL_METHOD_OF);
		} else if (tClass.getIRI().equals(ScroDefinitions.INSTANCE_METHOD)) {
			property = getFactory().getOWLObjectProperty(ScroDefinitions.IS_INSTANCE_METHOD_OF);
		}
		if (property != null) {
			OWLObjectPropertyAssertionAxiom axiom = getFactory().getOWLObjectPropertyAssertionAxiom(property, methodName, className);
			AddAxiom addAxiom = new AddAxiom(getOntology(), axiom);

			getManager().applyChange(addAxiom);
		}

		applyAccessControl(tIndividual, node.getModifiers());
	}
}
