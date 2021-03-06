package org.nulang.core.operations;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;
import org.nulang.core.terms.Term;

public interface Concatable {

	<T> T apply(ConcatOperation<T> operation, Term rhs, TermsVisitor<T> visitor) throws NuException;
}
