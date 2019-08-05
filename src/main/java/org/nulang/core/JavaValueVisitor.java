package org.nulang.core;

import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.CharacterValue;
import org.nulang.core.terms.values.FloatValue;
import org.nulang.core.terms.values.IntegerValue;
import org.nulang.core.terms.values.StringValue;
import org.nulang.core.terms.values.Value;

public class JavaValueVisitor implements ValueVisitor<Object> {

	@Override
	public <TT> Object visit(Value<TT> value) throws NuException {

		return value.value;
	}

	@Override
	public Object visit(FloatValue value) throws NuException {

		return value.value.doubleValue();
	}

	@Override
	public Object visit(IntegerValue value) throws NuException {

		return value.value.longValue();
	}

	@Override
	public Object visit(StringValue value) throws NuException {

		StringBuilder builder = new StringBuilder(value.value.length);

		for (Term term : value.value) {
			builder.append((char) ((CharacterValue) term).value);
		}

		return builder.toString();
	}
}
