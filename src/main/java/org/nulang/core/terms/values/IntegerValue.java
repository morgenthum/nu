package org.nulang.core.terms.values;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;
import org.nulang.core.ValueVisitor;
import org.nulang.core.operations.ShiftOperation;
import org.nulang.core.operations.Shiftable;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Shift.Direction;

public class IntegerValue extends NumberValue implements Shiftable {

	public IntegerValue(Long value) {
		super(value);
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {

		return visitor.visit(this);
	}

	@Override
	public <TT> TT apply(ValueVisitor<TT> visitor) throws NuException {

		return visitor.visit(this);
	}

	@Override
	public <T> T apply(ShiftOperation<T> operation, Term rhs, Direction direction, TermsVisitor<T> visitor)
			throws NuException {

		return operation.visit(this, rhs, direction, visitor);
	}
}
