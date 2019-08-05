package org.nulang.core.terms;

import java.util.HashMap;
import java.util.Map;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;

public class Module extends Term {

	public String name;
	public Map<String, Module> dependencies = new HashMap<>();

	public Map<String, Lambda> lambdas = new HashMap<>();

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}

	public Lambda lookup(String identifier) {

		Lambda lambda = this.lambdas.get(identifier);
		if (lambda != null) {
			return lambda;
		}

		for (Module module : this.dependencies.values()) {
			lambda = module.lookup(identifier);
			if (lambda != null) {
				return lambda;
			}
		}

		return null;
	}
}
