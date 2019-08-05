package org.nulang.core.terms.values;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class BooleanValue extends SimpleValue<Boolean> {

	public BooleanValue(Boolean value) {
		super(value);
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
