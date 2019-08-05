package org.nulang.core.terms.values;

import org.nulang.core.TermsVisitor;
import org.nulang.core.NuException;
import org.nulang.core.operations.Additivable;
import org.nulang.core.operations.AdditiveOperation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;

public class CharacterValue extends SimpleValue<Character> implements Additivable {

	public CharacterValue(Character value) {
		super(value);
	}

	@Override
	public <T> T apply(TermsVisitor<T> visitor) throws NuException {
		return visitor.visit(this);
	}

	@Override
	public <T> T apply(AdditiveOperation<T> operation, Term rhs, Additive.Operator operator, TermsVisitor<T> visitor)
			throws NuException {

		return operation.visit(this, rhs, operator, visitor);
	}
}
