package org.nulang.core.terms.values;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;
import org.nulang.core.ValueVisitor;

public class FloatValue extends NumberValue {

	public FloatValue(Double value) {
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
}
