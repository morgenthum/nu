package org.nulang.core.terms;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Negation extends Term {

	public Term term;

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
