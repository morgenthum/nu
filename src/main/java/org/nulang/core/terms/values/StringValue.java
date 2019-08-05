package org.nulang.core.terms.values;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.ValueVisitor;
import org.nulang.core.operations.AdditiveOperation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive.Operator;

public class StringValue extends ListValue {

	public StringValue(Term[] value) {

		super(value);
	}

	@Override
	public <TT> TT apply(ValueVisitor<TT> visitor) throws NuException {

		return visitor.visit(this);
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {

		return visitor.visit(this);
	}

	@Override
	public <T> T apply(AdditiveOperation<T> operation, Term rhs, Operator operator, TermsVisitor<T> visitor)
			throws NuException {

		return operation.visit(this, rhs, operator, visitor);
	}
}
