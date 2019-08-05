package org.nulang.core.operations;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Shift;
import org.nulang.core.terms.values.IntegerValue;
import org.nulang.core.terms.values.ListValue;

public interface ShiftOperation<T> {

	T visit(IntegerValue lhs, Term rhs, Shift.Direction direction, TermsVisitor<T> visitor) throws NuException;

	T visit(ListValue lhs, Term rhs, Shift.Direction direction, TermsVisitor<T> visitor) throws NuException;
}
