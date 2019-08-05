package org.nulang.interpreter.operations;

import java.util.Arrays;

import org.nulang.core.Builder;
import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.operations.ShiftOperation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Shift;
import org.nulang.core.terms.binary.Shift.Direction;
import org.nulang.core.terms.values.IntegerValue;
import org.nulang.core.terms.values.ListValue;

public class ShiftInterpreter implements ShiftOperation<Term> {

	@Override
	public Term visit(IntegerValue lhs, Term rhs, Direction direction, TermsVisitor<Term> visitor) throws NuException {

		if (!(rhs instanceof IntegerValue)) {
			throw new NuException("operation.shift.operands");
		}

		long lhsValue = lhs.value.longValue();
		long rhsValue = ((IntegerValue) rhs).value.longValue();

		if (Shift.Direction.LEFT == direction) {
			return Builder.val(lhsValue << rhsValue);
		} else {
			return Builder.val(lhsValue >> rhsValue);
		}
	}

	@Override
	public Term visit(ListValue lhs, Term rhs, Direction direction, TermsVisitor<Term> visitor) throws NuException {

		if (!(rhs instanceof IntegerValue)) {
			throw new NuException("operation.shift.operands");
		}

		Term[] list = lhs.value;
		int times = ((IntegerValue) rhs).value.intValue();

		if (Shift.Direction.LEFT == direction) {
			return Builder.val(Arrays.copyOfRange(list, times, list.length));
		} else {
			return Builder.val(Arrays.copyOfRange(list, 0, list.length - times));
		}
	}
}
