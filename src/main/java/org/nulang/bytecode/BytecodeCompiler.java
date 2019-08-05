package org.nulang.bytecode;

import java.util.ListIterator;

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

public class BytecodeCompiler implements TermsVisitor<Void> {

	private StringBuilder builder = new StringBuilder();

	private Lambda lambda;
	private int branches;

	@Override
	public Void visit(Additive term) throws NuException {

		term.lhs.apply(this);
		term.rhs.apply(this);

		if (Additive.Operator.ADD == term.operator) {
			this.builder.append("add");
		} else {
			this.builder.append("sub");
		}

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(BooleanValue term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Call term) throws NuException {

		ListIterator<Term> iterator = term.parameters.listIterator(term.parameters.size());
		while (iterator.hasPrevious()) {
			iterator.previous().apply(this);
		}
		term.object.apply(this);

		this.builder.append("call");

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(CharacterValue term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Concat term) throws NuException {

		term.lhs.apply(this);
		term.rhs.apply(this);

		this.builder.append("concat");

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Conditional term) throws NuException {

		term.condition.apply(this);

		String name = this.lambda.name + "_" + this.branches++;

		this.builder.append("branch " + name);
		this.builder.append(System.lineSeparator());

		term.otherwise.apply(this);

		this.builder.append(name + ':');
		this.builder.append(System.lineSeparator());

		term.then.apply(this);

		return null;
	}

	@Override
	public Void visit(Equality term) throws NuException {

		term.lhs.apply(this);
		term.rhs.apply(this);

		if (Equality.Operator.EQUAL == term.operator) {
			this.builder.append("equals");
		} else {
			this.builder.append("notEquals");
		}

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(FloatValue term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Identifier term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Index term) throws NuException {

		term.lhs.apply(this);
		term.rhs.apply(this);

		this.builder.append("index");

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(IntegerValue term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Lambda term) throws NuException {

		this.builder.append(term.name == null ? "clojure" : term.name);

		for (Term parameter : term.parameters) {
			this.builder.append(' ');
			this.builder.append(parameter);
		}
		this.builder.append(':');
		this.builder.append(System.lineSeparator());

		this.lambda = term;
		this.branches = 0;

		term.body.apply(this);

		return null;
	}

	@Override
	public Void visit(ListValue term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Module term) throws NuException {

		for (Lambda lambda : term.lambdas.values()) {
			lambda.apply(this);
			this.builder.append(System.lineSeparator());
		}

		return null;
	}

	@Override
	public Void visit(Multiplicative term) throws NuException {

		term.lhs.apply(this);
		term.rhs.apply(this);

		if (Multiplicative.Operator.DIV == term.operator) {
			this.builder.append("div");
		} else if (Multiplicative.Operator.MOD == term.operator) {
			this.builder.append("mod");
		} else {
			this.builder.append("mul");
		}

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Negation term) throws NuException {

		this.builder.append("negate");

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(NoneValue term) throws NuException {

		this.builder.append("push none");

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(ObjectValue term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Relational term) throws NuException {

		term.lhs.apply(this);
		term.rhs.apply(this);

		if (Relational.Operator.GREATER == term.operator) {
			this.builder.append("greater");
		} else if (Relational.Operator.GREATER_EQUAL == term.operator) {
			this.builder.append("greaterEquals");
		} else if (Relational.Operator.LESS == term.operator) {
			this.builder.append("less");
		} else if (Relational.Operator.LESS_EQUAL == term.operator) {
			this.builder.append("lessEquals");
		}

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(Shift term) throws NuException {

		term.lhs.apply(this);
		term.rhs.apply(this);

		if (Shift.Direction.LEFT == term.direction) {
			this.builder.append("leftShift");
		} else {
			this.builder.append("rightShift");
		}

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public Void visit(StringValue term) throws NuException {

		this.builder.append("push " + term);

		this.builder.append(System.lineSeparator());
		return null;
	}

	@Override
	public String toString() {

		return this.builder.toString();
	}
}
