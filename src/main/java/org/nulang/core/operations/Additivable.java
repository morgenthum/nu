package org.nulang.core.operations;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;

public interface Additivable {

	<T> T apply(AdditiveOperation<T> operation, Term rhs, Additive.Operator operator, TermsVisitor<T> visitor) throws NuException;
}
