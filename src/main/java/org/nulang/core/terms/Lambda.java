package org.nulang.core.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;

public class Lambda extends Term {

	public String name;
	public List<Term> parameters = new ArrayList<>();
	public Term body;

	public Lambda parent;
	public Map<String, Lambda> lambdas = new HashMap<>();

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {

		return visitor.visit(this);
	}

	public void addLambda(Lambda lambda) {

		lambda.parent = this;
		this.lambdas.put(lambda.name, lambda);
	}

	public Lambda lookup(String identifier) {

		Lambda current = this;

		do {
			Lambda lambda = current.lambdas.get(identifier);

			if (lambda != null) {
				return lambda;
			}
		} while ((current = current.parent) != null);

		return null;
	}
}
