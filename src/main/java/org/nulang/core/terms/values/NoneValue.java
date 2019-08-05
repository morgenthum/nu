package org.nulang.core.terms.values;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;

public class NoneValue extends SimpleValue<Object> {

	public static final NoneValue INSTANCE = new NoneValue(Void.TYPE);

	private NoneValue(Object value) {
		super(value);
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {

		return visitor.visit(this);
	}
}
