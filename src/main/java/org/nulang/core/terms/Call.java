package org.nulang.core.terms;

import java.util.Arrays;
import java.util.List;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Call extends Term {

	public Term object;

	public List<Term> parameters;

	public Call(Term object, Term... parameters) {

		this(object, Arrays.asList(parameters));
	}

	public Call(Term object, List<Term> parameters) {

		this.object = object;
		this.parameters = parameters;
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
