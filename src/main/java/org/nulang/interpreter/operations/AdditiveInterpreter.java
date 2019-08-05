package org.nulang.interpreter.operations;

import org.nulang.core.Builder;
import org.nulang.core.JavaValueVisitor;
import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.operations.AdditiveOperation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;
import org.nulang.core.terms.binary.Additive.Operator;
import org.nulang.core.terms.values.IntegerValue;
import org.nulang.core.terms.values.ListValue;
import org.nulang.core.terms.values.NumberValue;
import org.nulang.core.terms.values.SimpleValue;
import org.nulang.core.terms.values.StringValue;

public class AdditiveInterpreter implements AdditiveOperation<Term> {

	private Additive term;

	public AdditiveInterpreter(Additive term) {

		this.term = term;
	}

	@Override
	public Term visit(SimpleValue<?> lhs, Term rhs, Additive.Operator operator, TermsVisitor<Term> visitor)
			throws NuException {

		if (!(rhs instanceof SimpleValue<?>)) {
			throw this.createException();
		}

		SimpleValue<?> simpleRhs = (SimpleValue<?>) rhs;

		return Builder.val(String.valueOf(lhs.value) + String.valueOf(simpleRhs.value));
	}

	@Override
	public Term visit(NumberValue lhs, Term rhs, Operator operator, TermsVisitor<Term> visitor) throws NuException {

		if (rhs instanceof NumberValue) {
			NumberValue numberRhs = (NumberValue) rhs;

			boolean integer = lhs instanceof IntegerValue && numberRhs instanceof IntegerValue;

			if (Operator.ADD == operator) {
				Number result = null;
				if (integer) {
					result = lhs.value.longValue() + numberRhs.value.longValue();
				} else {
					result = lhs.value.doubleValue() + numberRhs.value.doubleValue();
				}
				return Builder.val(result);
			} else {
				Number result = null;
				if (integer) {
					result = lhs.value.longValue() - numberRhs.value.longValue();
				} else {
					result = lhs.value.doubleValue() - numberRhs.value.doubleValue();
				}
				return Builder.val(result);
			}
		}

		return this.visit((SimpleValue<?>) lhs, rhs, operator, visitor);
	}

	@Override
	public Term visit(StringValue lhs, Term rhs, Operator operator, TermsVisitor<Term> visitor) throws NuException {

		if (rhs instanceof SimpleValue<?>) {
			Object lhsValue = lhs.apply(new JavaValueVisitor());
			String rhsString = String.valueOf(((SimpleValue<?>) rhs).value);

			return Builder.val(lhsValue + rhsString);
		}

		return this.visit((ListValue) lhs, rhs, operator, visitor);
	}

	@Override
	public Term visit(ListValue lhs, Term rhs, Additive.Operator operator, TermsVisitor<Term> visitor)
			throws NuException {

		if (Operator.ADD != operator) {
			throw this.createException();
		}

		return lhs.apply(new ConcatInterpreter(), rhs, visitor);
	}

	private NuException createException() {

		return new NuException("operation.additive.operands", this.term);
	}
}
