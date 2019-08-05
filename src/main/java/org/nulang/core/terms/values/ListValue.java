package org.nulang.core.terms.values;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.operations.Additivable;
import org.nulang.core.operations.AdditiveOperation;
import org.nulang.core.operations.ConcatOperation;
import org.nulang.core.operations.IndexOperation;
import org.nulang.core.operations.Indexable;
import org.nulang.core.operations.ShiftOperation;
import org.nulang.core.operations.Shiftable;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;
import org.nulang.core.terms.binary.Shift;

public class ListValue extends Value<Term[]> implements Additivable, Indexable, Shiftable {

	public ListValue(Term[] value) {
		super(value);
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}

	@Override
	public <T> T apply(AdditiveOperation<T> operation, Term rhs, Additive.Operator operator, TermsVisitor<T> visitor)
			throws NuException {

		return operation.visit(this, rhs, operator, visitor);
	}

	@Override
	public <T> T apply(IndexOperation<T> operation, Term rhs, TermsVisitor<T> visitor) throws NuException {

		return operation.visit(this, rhs, visitor);
	}

	@Override
	public <T> T apply(ConcatOperation<T> operation, Term rhs, TermsVisitor<T> visitor) throws NuException {

		return operation.visit(this, rhs, visitor);
	}

	@Override
	public <T> T apply(ShiftOperation<T> operation, Term rhs, Shift.Direction direction, TermsVisitor<T> visitor)
			throws NuException {

		return operation.visit(this, rhs, direction, visitor);
	}
}
