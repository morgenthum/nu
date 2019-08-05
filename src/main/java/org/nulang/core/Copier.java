package org.nulang.core;

import java.util.ArrayList;
import java.util.List;

import org.nulang.core.terms.Call;
import org.nulang.core.terms.Conditional;
import org.nulang.core.terms.Identifier;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Negation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;
import org.nulang.core.terms.binary.BinaryTerm;
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

public class Copier implements TermsVisitor<Term> {

	public static final Copier INSTANCE = new Copier();

	private Copier() {
	}

	@Override
	public Term visit(Additive term) throws NuException {

		Additive copy = new Additive();
		copy.operator = term.operator;

		return this.visit(term, copy);
	}

	private Term visit(BinaryTerm term, BinaryTerm copy) throws NuException {

		copy.lhs = term.lhs.apply(this);
		copy.rhs = term.rhs.apply(this);

		return copy;
	}

	@Override
	public Term visit(BooleanValue term) throws NuException {

		return new BooleanValue(term.value);
	}

	@Override
	public Term visit(Call term) throws NuException {

		Term copy = term.object.apply(this);

		List<Term> paramsCopies = new ArrayList<>(term.parameters.size());
		for (Term param : term.parameters) {
			paramsCopies.add(param.apply(this));
		}

		return new Call(copy, paramsCopies);
	}

	@Override
	public Term visit(CharacterValue term) throws NuException {

		return new CharacterValue(term.value);
	}

	@Override
	public Term visit(Concat term) throws NuException {

		return this.visit(term, new Concat());
	}

	@Override
	public Term visit(Conditional term) throws NuException {

		Conditional copy = new Conditional();
		copy.condition = term.condition.apply(this);
		copy.then = term.then.apply(this);
		copy.otherwise = term.otherwise.apply(this);

		return copy;
	}

	@Override
	public Term visit(FloatValue term) throws NuException {

		return new FloatValue(term.value.doubleValue());
	}

	@Override
	public Term visit(Index term) throws NuException {

		return this.visit(term, new Index());
	}

	@Override
	public Term visit(Equality term) throws NuException {

		Equality copy = new Equality();
		copy.operator = term.operator;

		return this.visit(term, copy);
	}

	@Override
	public Term visit(Identifier term) throws NuException {

		return new Identifier(term.identifier);
	}

	@Override
	public Term visit(Lambda term) throws NuException {

		Lambda copy = new Lambda();
		copy.name = term.name;
		copy.parameters.addAll(term.parameters);
		copy.body = term.body.apply(this);

		copy.parent = term.parent;
		copy.lambdas.putAll(term.lambdas);

		return copy;
	}

	@Override
	public Term visit(ListValue term) throws NuException {

		Term[] copy = new Term[term.value.length];
		System.arraycopy(term.value, 0, copy, 0, copy.length);

		return new ListValue(copy);
	}

	@Override
	public Term visit(IntegerValue term) throws NuException {

		return new IntegerValue(term.value.longValue());
	}

	@Override
	public Term visit(Module term) throws NuException {

		Module copy = new Module();

		copy.name = term.name;
		copy.dependencies.putAll(term.dependencies);

		for (String key : term.lambdas.keySet()) {
			copy.lambdas.put(key, term.lambdas.get(key));
		}

		return copy;
	}

	@Override
	public Term visit(Multiplicative term) throws NuException {

		Multiplicative copy = new Multiplicative();
		copy.operator = term.operator;

		return this.visit(term, copy);
	}

	@Override
	public Term visit(Negation term) throws NuException {

		Negation copy = new Negation();
		copy.term = term.term.apply(this);

		return copy;
	}

	@Override
	public Term visit(NoneValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(ObjectValue term) throws NuException {

		throw new NuException("java.copy");
	}

	@Override
	public Term visit(Relational term) throws NuException {

		Relational copy = new Relational();
		copy.operator = term.operator;

		return this.visit(term, copy);
	}

	@Override
	public Term visit(Shift term) throws NuException {

		Shift copy = new Shift();
		copy.direction = term.direction;

		return this.visit(term, copy);
	}

	@Override
	public Term visit(StringValue term) throws NuException {

		Term[] copy = new Term[term.value.length];
		System.arraycopy(term.value, 0, copy, 0, copy.length);

		return new StringValue(copy);
	}
}
