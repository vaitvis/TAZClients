package com.ernesta.service;

import java.util.Set;
import java.util.UUID;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.Searcher;

import com.ernesta.model.AbstractHandler;
import com.ernesta.model.ImportsMap;
import com.ernesta.model.PersonRule;
import com.ernesta.model.RulesManager;

public class PersonHandler extends AbstractHandler {

	public PersonHandler(OWLOntologyManager manager, OWLOntology ontology, OWLReasoner reasoner, RulesManager rulesManager) {
		super(manager, ontology, reasoner, rulesManager);
	}

	public void handle(Set<OWLNamedIndividual> individuals) {
		for (OWLNamedIndividual ind : individuals) {
			OWLLiteral lemma = null;
			for (OWLAnnotation annotation : Searcher.annotations(getOntology().getAnnotationAssertionAxioms(ind.getIRI()), getLabelLemma())) {
				if (annotation.getValue() instanceof OWLLiteral) {
					lemma = (OWLLiteral) annotation.getValue();
				}
			}
			if (lemma != null) {
				OWLNamedIndividual mergedPerson = getSimilarMergedPerson(lemma);
				if (mergedPerson == null) {
					mergedPerson = createMergedPerson(lemma);
				}
				markSimilarTo(ind, mergedPerson);
			}
		}
	}

	private void markSimilarTo(OWLNamedIndividual individual, OWLNamedIndividual similarTo) {
		OWLObjectProperty isSimilarTo = getFactory().getOWLObjectProperty(IRI.create(ImportsMap.AGENTS.getUri() + "#is_similar_to"));
		OWLObjectPropertyAssertionAxiom axiom = getFactory().getOWLObjectPropertyAssertionAxiom(isSimilarTo, individual, similarTo);
		AddAxiom addAxiom = new AddAxiom(getOntology(), axiom);

		getManager().applyChange(addAxiom);
	}

	private OWLNamedIndividual createMergedPerson(OWLLiteral lemma) {
		OWLClass tClass = getFactory().getOWLClass(IRI.create(ImportsMap.MOBJECTS.getUri() + "#merged_person"));
		OWLNamedIndividual tIndividual = getFactory().getOWLNamedIndividual(
				IRI.create(ImportsMap.MOBJECTS.getUri() + "#merged_person~" + UUID.randomUUID() + "~MergedPerson"));
		OWLClassAssertionAxiom classAssertion = getFactory().getOWLClassAssertionAxiom(tClass, tIndividual);
		getManager().addAxiom(getOntology(), classAssertion);

		OWLAnnotation label = getFactory().getOWLAnnotation(getFactory().getOWLAnnotationProperty(getLabelLemma().getIRI()), lemma);
		OWLAxiom axiom = getFactory().getOWLAnnotationAssertionAxiom(tIndividual.getIRI(), label);
		getManager().addAxiom(getOntology(), axiom);
		return tIndividual;
	}

	private OWLNamedIndividual getSimilarMergedPerson(OWLLiteral lemma) {
		OWLClass mergedPerson = getManager().getOWLDataFactory().getOWLClass(IRI.create(ImportsMap.MOBJECTS.getUri() + "#merged_person"));
		NodeSet<OWLNamedIndividual> instances = getReasoner().getInstances(mergedPerson, true);
		for (OWLNamedIndividual individual : instances.getFlattened()) {
			OWLLiteral mergedLemma = null;
			for (OWLAnnotation annotation : Searcher.annotations(getOntology().getAnnotationAssertionAxioms(individual.getIRI()), getLabelLemma())) {
				if (annotation.getValue() instanceof OWLLiteral) {
					mergedLemma = (OWLLiteral) annotation.getValue();
				}
			}
			if (mergedLemma != null) {
				if (isSimilar(lemma, mergedLemma)) {
					return individual;
				}
			}
		}
		return null;
	}

	private boolean isSimilar(OWLLiteral first, OWLLiteral second) {
		for (PersonRule rule : getRulesManager().getPersonRules()) {
			if (rule.accept(first.getLiteral(), second.getLiteral())) {
				return true;
			}
		}
		return false;
	}
}
