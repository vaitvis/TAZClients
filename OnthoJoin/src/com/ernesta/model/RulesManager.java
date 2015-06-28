package com.ernesta.model;

import java.util.ArrayList;
import java.util.List;

public class RulesManager {

	private List<PersonRule> personRules;

	public RulesManager() {
		personRules = new ArrayList<PersonRule>();
	}

	public List<PersonRule> getPersonRules() {
		return personRules;
	}

	public void addPersonRule(PersonRule rule) {
		personRules.add(rule);
	}
}
