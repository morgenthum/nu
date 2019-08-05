package org.nulang.core.terms.values;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;
import org.nulang.core.operations.Additivable;
import org.nulang.core.operations.AdditiveOperation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;

public abstract class SimpleValue<T> extends Value<T> implements Additivable {

	public SimpleValue(T value) {
		super(value);
	}

	@Override
	public <TT> TT apply(AdditiveOperation<TT> operation, Term rhs, Additive.Operator operator, TermsVisitor<TT> visitor)
			throws NuException {

		return operation.visit(this, rhs, operator, visitor);
	}
}
