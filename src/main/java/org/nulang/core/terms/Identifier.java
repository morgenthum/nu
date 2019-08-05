package org.nulang.core.terms;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;

public class Identifier extends Term {

	public String identifier;

	public Identifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}
}
