package org.nulang.interpreter;

import java.util.HashMap;
import java.util.Map;

import org.nulang.core.terms.Term;

public class Frame {

	public Map<String, Term> variables;

	private Map<String, Term> resolvedLets;

	public Frame(Map<String, Term> variables) {

		this.variables = variables;
	}

	public void addResolvedLet(String identifier, Term resolved) {

		if (this.resolvedLets == null) {
			this.resolvedLets = new HashMap<>();
		}

		this.resolvedLets.put(identifier, resolved);
	}

	public Term lookup(String identifier) {

		Term term = this.variables.get(identifier);

		if (term == null && this.resolvedLets != null) {
			term = this.resolvedLets.get(identifier);
		}

		return term;
	}
}