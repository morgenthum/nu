package org.nulang.core.terms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nulang.core.TermsVisitor;
import org.nulang.core.Comparator;
import org.nulang.core.Hasher;
import org.nulang.core.NuException;
import org.nulang.core.Stringifier;
import org.nulang.core.operations.ConcatOperation;
import org.nulang.core.operations.Concatable;

public abstract class Term implements Concatable {

	private static final Logger LOGGER = LogManager.getLogger(Term.class);

	public abstract <T> T apply(TermsVisitor<T> visitor) throws NuException;

	@Override
	public <T> T apply(ConcatOperation<T> operation, Term rhs, TermsVisitor<T> visitor) throws NuException {

		return operation.visit(this, rhs, visitor);
	}

	@Override
	public int hashCode() {

		try {
			return this.apply(Hasher.INSTANCE);
		} catch (NuException e) {
			LOGGER.debug("hashCode failed: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof Term)) {
			return false;
		}

		try {
			return this.apply(new Comparator((Term) obj));
		} catch (NuException e) {
			LOGGER.debug("equals failed: " + e.getMessage());
			return false;
		}
	}

	@Override
	public String toString() {

		try {
			return this.apply(Stringifier.INSTANCE);
		} catch (NuException e) {
			LOGGER.debug("toString failed: " + e.getMessage());
			return super.toString();
		}
	}
}
