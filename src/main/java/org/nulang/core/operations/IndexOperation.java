package org.nulang.core.operations;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.ListValue;

public interface IndexOperation<T> {

	T visit(ListValue lhs, Term rhs, TermsVisitor<T> visitor) throws NuException;
}
