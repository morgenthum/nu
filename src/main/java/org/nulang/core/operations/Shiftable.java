package org.nulang.core.operations;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Shift;

public interface Shiftable {

	<T> T apply(ShiftOperation<T> operation, Term rhs, Shift.Direction direction, TermsVisitor<T> visitor) throws NuException;
}
