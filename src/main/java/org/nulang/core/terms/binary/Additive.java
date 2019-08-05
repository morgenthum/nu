package org.nulang.core.terms.binary;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Additive extends BinaryTerm {

	public enum Operator {
		ADD, SUB
	}

	public Operator operator;

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
