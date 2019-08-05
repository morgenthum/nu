package org.nulang.interpreter.operations;

import java.util.Arrays;

import org.nulang.core.Builder;
import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.operations.ConcatOperation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.ListValue;

public class ConcatInterpreter implements ConcatOperation<Term> {

	@Override
	public Term visit(Term lhs, Term rhs, TermsVisitor<Term> visitor) throws NuException {

		return this.toList(lhs).apply(this, rhs, visitor);
	}

	@Override
	public Term visit(ListValue lhs, Term rhs, TermsVisitor<Term> visitor) throws NuException {

		ListValue rhsList = this.toList(rhs);

		Term[] newList = new Term[lhs.value.length + rhsList.value.length];

		System.arraycopy(lhs.value, 0, newList, 0, lhs.value.length);
		System.arraycopy(rhsList.value, 0, newList, lhs.value.length, rhsList.value.length);

		return Builder.val(newList);
	}

	private ListValue toList(Term lhs) {

		if (lhs instanceof ListValue) {
			return (ListValue) lhs;
		}

		return Builder.val(Arrays.asList(lhs));
	}
}
