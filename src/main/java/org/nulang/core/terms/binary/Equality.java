package org.nulang.core.terms.binary;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Equality extends BinaryTerm {

	public enum Operator {
		EQUAL, UNEQUAL
	}

	public Operator operator;

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
