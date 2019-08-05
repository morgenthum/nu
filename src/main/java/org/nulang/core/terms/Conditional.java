package org.nulang.core.terms;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Conditional extends Term {

	public Term condition;
	public Term then;
	public Term otherwise;

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
