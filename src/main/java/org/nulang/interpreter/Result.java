package org.nulang.interpreter;

import java.util.Map;

import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class Result {

	private Lambda lambda;
	private Map<String, Term> parameters;

	public Result(Lambda lambda, Map<String, Term> parameters) {

		this.lambda = lambda;
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lambda == null) ? 0 : lambda.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Result))
			return false;
		Result other = (Result) obj;
		if (lambda == null) {
			if (other.lambda != null)
				return false;
		} else if (!lambda.equals(other.lambda))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}
}
