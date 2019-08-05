package org.nulang.core.operations;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;
import org.nulang.core.terms.values.ListValue;
import org.nulang.core.terms.values.NumberValue;
import org.nulang.core.terms.values.SimpleValue;
import org.nulang.core.terms.values.StringValue;

public interface AdditiveOperation<T> {

	T visit(SimpleValue<?> lhs, Term rhs, Additive.Operator operator, TermsVisitor<T> visitor) throws NuException;

	T visit(NumberValue lhs, Term rhs, Additive.Operator operator, TermsVisitor<T> visitor) throws NuException;

	T visit(StringValue lhs, Term rhs, Additive.Operator operator, TermsVisitor<T> visitor) throws NuException;

	T visit(ListValue lhs, Term rhs, Additive.Operator operator, TermsVisitor<T> visitor) throws NuException;
}
