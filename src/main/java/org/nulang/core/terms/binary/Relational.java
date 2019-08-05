package org.nulang.core.terms.binary;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Relational extends BinaryTerm {

	public enum Operator {
		LESS, GREATER, LESS_EQUAL, GREATER_EQUAL
	}

	public Operator operator;

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
