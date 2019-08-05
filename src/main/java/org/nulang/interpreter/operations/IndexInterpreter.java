package org.nulang.interpreter.operations;

import org.nulang.core.Builder;
import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.operations.IndexOperation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.ListValue;
import org.nulang.core.terms.values.NumberValue;

public class IndexInterpreter implements IndexOperation<Term> {

	@Override
	public Term visit(ListValue lhs, Term rhs, TermsVisitor<Term> visitor) throws NuException {

		Term[] list = lhs.value;

		rhs = rhs.apply(visitor);

		if (rhs instanceof NumberValue) {
			int index = ((NumberValue) rhs).value.intValue();
			Term result = list[index];

			return result == null ? result : result.apply(visitor);
		}

		if (rhs instanceof ListValue) {
			Term[] indexes = ((ListValue) rhs).value;
			Term[] newList = new Term[indexes.length];

			int i = 0;
			for (Object obj : indexes) {
				if (!(obj instanceof NumberValue)) {
					throw new NuException("operation.index.numbers");
				}
				int index = ((NumberValue) obj).value.intValue();
				Term result = list[index];

				newList[i++] = result == null ? result : result.apply(visitor);
			}

			return Builder.val(newList);
		}

		throw new NuException("operation.index.operands");
	}
}
