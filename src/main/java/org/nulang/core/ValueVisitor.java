package org.nulang.core;

import org.nulang.core.terms.values.FloatValue;
import org.nulang.core.terms.values.IntegerValue;
import org.nulang.core.terms.values.StringValue;
import org.nulang.core.terms.values.Value;

public interface ValueVisitor<T> {

	<TT> T visit(Value<TT> value) throws NuException;

	T visit(FloatValue value) throws NuException;

	T visit(IntegerValue value) throws NuException;

	T visit(StringValue value) throws NuException;
}
