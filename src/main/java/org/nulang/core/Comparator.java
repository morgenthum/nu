package org.nulang.core;

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
import org.nulang.core.terms.values.SimpleValue;
import org.nulang.core.terms.values.StringValue;
import org.nulang.core.terms.values.Value;

public class Comparator implements TermsVisitor<Boolean> {

	private Term term;

	public Comparator(Term term) {

		this.term = term;
	}

	@Override
	public Boolean visit(Additive term) throws NuException {

		return this.term instanceof Additive && this.visit((BinaryTerm) term)
				&& ((Additive) this.term).operator == term.operator;
	}

	@Override
	public Boolean visit(BooleanValue term) throws NuException {

		return this.visit((SimpleValue<?>) term);
	}

	@Override
	public Boolean visit(Call term) throws NuException {

		if (!(this.term instanceof Call)) {
			return false;
		}

		Call thisTerm = (Call) this.term;

		if (!term.object.apply(new Comparator(thisTerm.object))) {
			return false;
		}

		if (term.parameters.size() != thisTerm.parameters.size()) {
			return false;
		}

		for (int i = 0; i < term.parameters.size(); ++i) {
			Term parameter = term.parameters.get(i);
			Term thisParameter = thisTerm.parameters.get(i);

			if (!parameter.apply(new Comparator(thisParameter))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Boolean visit(CharacterValue term) throws NuException {

		return this.visit((SimpleValue<?>) term);
	}

	@Override
	public Boolean visit(Concat term) throws NuException {

		return this.term instanceof Concat && this.visit((BinaryTerm) term);
	}

	@Override
	public Boolean visit(Conditional term) throws NuException {

		if (!(this.term instanceof Conditional)) {
			return false;
		}

		Conditional cond = (Conditional) this.term;

		return cond.condition.apply(new Comparator(term.condition))
				&& cond.otherwise.apply(new Comparator(term.otherwise)) && cond.then.apply(new Comparator(term.then));
	}

	@Override
	public Boolean visit(FloatValue term) throws NuException {

		return this.visit((SimpleValue<?>) term);
	}

	@Override
	public Boolean visit(Index term) throws NuException {

		return this.term instanceof Index && this.visit((BinaryTerm) term);
	}

	@Override
	public Boolean visit(Equality term) throws NuException {

		return this.term instanceof Equality && this.visit((BinaryTerm) term)
				&& ((Equality) this.term).operator == term.operator;
	}

	@Override
	public Boolean visit(Identifier term) throws NuException {

		return this.term instanceof Identifier && ((Identifier) this.term).identifier.equals(term.identifier);
	}

	@Override
	public Boolean visit(Lambda term) throws NuException {

		if (!(this.term instanceof Lambda)) {
			return false;
		}

		Lambda thisLambda = (Lambda) this.term;

		if (term.parameters.size() != thisLambda.parameters.size()) {
			return false;
		}

		for (int i = 0; i < term.parameters.size(); ++i) {
			Term param1 = term.parameters.get(i);
			Term param2 = thisLambda.parameters.get(i);

			if (!param1.apply(new Comparator(param2))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Boolean visit(ListValue term) throws NuException {

		if (!(this.term instanceof ListValue)) {
			return false;
		}

		ListValue thisTerm = (ListValue) this.term;

		if (term.value.length != thisTerm.value.length) {
			return false;
		}

		for (int i = 0; i < term.value.length; ++i) {
			Term element1 = term.value[i];
			Term element2 = thisTerm.value[i];

			if (!element1.apply(new Comparator(element2))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Boolean visit(IntegerValue term) throws NuException {

		return this.visit((SimpleValue<?>) term);
	}

	@Override
	public Boolean visit(Module term) throws NuException {

		if (!(this.term instanceof Module)) {
			return false;
		}

		Module thisTerm = (Module) this.term;

		if (!thisTerm.name.equals(term.name)) {
			return false;
		}

		return term.lambdas.equals(thisTerm.lambdas);
	}

	@Override
	public Boolean visit(Multiplicative term) throws NuException {

		return this.term instanceof Multiplicative && this.visit((BinaryTerm) term)
				&& ((Multiplicative) this.term).operator == term.operator;
	}

	@Override
	public Boolean visit(Negation term) throws NuException {

		return this.term instanceof Negation && ((Negation) this.term).term.apply(new Comparator(term.term));
	}

	@Override
	public Boolean visit(NoneValue term) throws NuException {

		return this.term instanceof NoneValue;
	}

	@Override
	public Boolean visit(ObjectValue term) throws NuException {

		return false;
	}

	@Override
	public Boolean visit(Relational term) throws NuException {

		return this.term instanceof Relational && this.visit((BinaryTerm) term)
				&& ((Relational) this.term).operator == term.operator;
	}

	@Override
	public Boolean visit(Shift term) throws NuException {

		return this.term instanceof Shift && this.visit((BinaryTerm) term);
	}

	@Override
	public Boolean visit(StringValue term) throws NuException {

		return this.visit((ListValue) term);
	}

	private Boolean visit(SimpleValue<?> term) {

		return this.term instanceof Value<?> && ((Value<?>) this.term).value.equals(term.value);
	}

	private Boolean visit(BinaryTerm term) {

		if (this.term instanceof BinaryTerm) {
			return ((BinaryTerm) this.term).lhs.equals(term.lhs) && ((BinaryTerm) this.term).rhs.equals(term.rhs);
		}

		return false;
	}

}
