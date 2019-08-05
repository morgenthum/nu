package org.nulang.core;

import org.nulang.core.terms.Call;
import org.nulang.core.terms.Conditional;
import org.nulang.core.terms.Identifier;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Negation;
import org.nulang.core.terms.binary.Additive;
import org.nulang.core.terms.binary.Concat;
import org.nulang.core.terms.binary.Equality;
import org.nulang.core.terms.binary.Index;
import org.nulang.core.terms.binary.Multiplicative;
import org.nulang.core.terms.binary.Relational;
import org.nulang.core.terms.binary.Shift;
import org.nulang.core.terms.values.BooleanValue;
import org.nulang.core.terms.values.CharacterValue;
import org.nulang.core.terms.values.FloatValue;
import org.nulang.core.terms.values.IntegerValue;
import org.nulang.core.terms.values.ListValue;
import org.nulang.core.terms.values.NoneValue;
import org.nulang.core.terms.values.ObjectValue;
import org.nulang.core.terms.values.StringValue;

public interface TermsVisitor<T> {

	T visit(Additive term) throws NuException;

	T visit(BooleanValue term) throws NuException;

	T visit(Call term) throws NuException;

	T visit(CharacterValue term) throws NuException;

	T visit(Concat term) throws NuException;

	T visit(Conditional term) throws NuException;

	T visit(Equality term) throws NuException;

	T visit(FloatValue term) throws NuException;

	T visit(Identifier term) throws NuException;

	T visit(Index term) throws NuException;

	T visit(IntegerValue term) throws NuException;

	T visit(Lambda term) throws NuException;

	T visit(ListValue term) throws NuException;

	T visit(Module term) throws NuException;

	T visit(Multiplicative term) throws NuException;

	T visit(Negation term) throws NuException;

	T visit(NoneValue term) throws NuException;

	T visit(ObjectValue term) throws NuException;

	T visit(Relational term) throws NuException;

	T visit(Shift term) throws NuException;

	T visit(StringValue term) throws NuException;
}
