package org.nulang.interpreter;

import java.util.Map;

import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
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

public class Substitutor implements TermsVisitor<Term> {

	private Map<Identifier, Term> substitutions;

	public Substitutor(Map<Identifier, Term> substitutions) {

		this.substitutions = substitutions;
	}

	@Override
	public Term visit(Additive term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	private Term visit(BinaryTerm term) throws NuException {

		term.lhs = term.lhs.apply(this);
		term.rhs = term.rhs.apply(this);

		return term;
	}

	@Override
	public Term visit(BooleanValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Call term) throws NuException {

		term.object = term.object.apply(this);

		for (int i = 0; i < term.parameters.size(); ++i) {
			Term parameter = term.parameters.get(i);
			term.parameters.set(i, parameter.apply(this));
		}

		return term;
	}

	@Override
	public Term visit(CharacterValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Concat term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Term visit(Conditional term) throws NuException {

		term.condition = term.condition.apply(this);
		term.then = term.then.apply(this);
		term.otherwise = term.otherwise.apply(this);

		return term;
	}

	@Override
	public Term visit(FloatValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Index term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Term visit(Equality term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Term visit(Identifier term) throws NuException {

		Term substitution = this.substitutions.get(term);
		if (substitution != null) {
			return substitution;
		}

		return term;
	}

	@Override
	public Term visit(Lambda term) throws NuException {

		term.body = term.body.apply(this);

		for (String key : term.lambdas.keySet()) {
			Lambda let = term.lambdas.get(key);
			term.lambdas.put(key, (Lambda) let.apply(this));
		}

		return term;
	}

	@Override
	public Term visit(ListValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(IntegerValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Module term) throws NuException {

		for (Lambda decl : term.lambdas.values()) {
			decl.apply(this);
		}

		return term;
	}

	@Override
	public Term visit(Multiplicative term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Term visit(Negation term) throws NuException {

		term.term = term.term.apply(this);

		return term;
	}

	@Override
	public Term visit(NoneValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(ObjectValue term) throws NuException {

		throw new NuException("java.substitute");
	}

	@Override
	public Term visit(Relational term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Term visit(Shift term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Term visit(StringValue term) throws NuException {

		return term;
	}
}
