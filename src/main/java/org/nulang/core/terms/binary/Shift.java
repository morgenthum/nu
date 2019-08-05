package org.nulang.core.terms.binary;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Shift extends BinaryTerm {

	public enum Direction {
		LEFT, RIGHT
	}

	public Direction direction;

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
