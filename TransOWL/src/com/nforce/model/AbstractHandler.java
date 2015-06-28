package com.nforce.model;

import org.eclipse.jdt.core.dom.Modifier;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public abstract class AbstractHandler {

	private final OWLOntology ontology;
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;

	protected AbstractHandler(OWLOntologyManager manager, OWLOntology ontology) {
		this.ontology = ontology;
		this.manager = manager;
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

	protected void applyAccessControl(OWLIndividual individual, int modifiers) {
		OWLIndividual accessControl = null;
		if (Modifier.isPublic(modifiers)) {
			accessControl = getFactory().getOWLNamedIndividual(ScroDefinitions.PUBLIC);
		} else if (Modifier.isProtected(modifiers)) {
			accessControl = getFactory().getOWLNamedIndividual(ScroDefinitions.PROTECTED);
		} else if (Modifier.isPrivate(modifiers)) {
			accessControl = getFactory().getOWLNamedIndividual(ScroDefinitions.PRIVATE);
		} else {
			accessControl = getFactory().getOWLNamedIndividual(ScroDefinitions.DEFAULT);
		}

		OWLObjectProperty hasAccessControl = getFactory().getOWLObjectProperty(ScroDefinitions.HAS_ACCESS_CONTROL);

		OWLObjectPropertyAssertionAxiom axiom = getFactory().getOWLObjectPropertyAssertionAxiom(hasAccessControl, individual, accessControl);
		AddAxiom addAxiom = new AddAxiom(getOntology(), axiom);

		getManager().applyChange(addAxiom);
	}
}
