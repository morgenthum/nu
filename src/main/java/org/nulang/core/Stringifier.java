package org.nulang.core;

import java.util.Iterator;

import org.nulang.core.terms.Call;
import org.nulang.core.terms.Conditional;
import org.nulang.core.terms.Identifier;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Negation;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.binary.Additive;
import org.nulang.core.terms.binary.Additive.Operator;
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
import org.nulang.core.terms.values.Value;

public class Stringifier implements TermsVisitor<String> {

	public static final Stringifier INSTANCE = new Stringifier();

	private Stringifier() {
	}

	@Override
	public String visit(Additive term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append('(');
		builder.append(term.lhs.apply(this));
		if (Operator.ADD == term.operator) {
			builder.append(" + ");
		} else {
			builder.append(" - ");
		}
		builder.append(term.rhs.apply(this));
		builder.append(')');

		return builder.toString();
	}

	@Override
	public String visit(BooleanValue term) throws NuException {

		return String.valueOf(term.value);
	}

	@Override
	public String visit(Call term) throws NuException {

		StringBuilder builder = new StringBuilder(term.object.apply(this));
		if (term.parameters.isEmpty()) {
			return builder.toString();
		}

		builder.append('(');

		for (int i = 0; i < term.parameters.size(); ++i) {
			Term param = term.parameters.get(i);
			builder.append(param.apply(this));
			if (i < term.parameters.size() - 1) {
				builder.append(", ");
			}
		}

		builder.append(')');

		return builder.toString();
	}

	@Override
	public String visit(CharacterValue term) throws NuException {

		return "'" + String.valueOf(term.value) + "'";
	}

	@Override
	public String visit(Concat term) throws NuException {

		return term.lhs.apply(this) + " ++ " + term.rhs.apply(this);
	}

	@Override
	public String visit(Conditional term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append(term.condition.apply(this));
		builder.append(System.lineSeparator());
		builder.append("\t? ");
		builder.append(term.then.apply(this));
		builder.append(System.lineSeparator());
		builder.append("\t: ");
		builder.append(term.otherwise.apply(this));

		return builder.toString();
	}

	@Override
	public String visit(FloatValue term) throws NuException {

		return String.valueOf(term.value);
	}

	@Override
	public String visit(Index term) throws NuException {

		return term.lhs.apply(this) + '.' + term.rhs.apply(this);
	}

	@Override
	public String visit(Equality term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append('(');
		builder.append(term.lhs.apply(this));
		if (Equality.Operator.EQUAL == term.operator) {
			builder.append(" == ");
		} else {
			builder.append(" != ");
		}
		builder.append(term.rhs.apply(this));
		builder.append(')');

		return builder.toString();
	}

	@Override
	public String visit(Identifier term) {

		return term.identifier;
	}

	@Override
	public String visit(Lambda term) throws NuException {

		StringBuilder builder = new StringBuilder();

		Term body = this.visitPatternMatching(term, term.body, builder);
		this.visitPlain(term, body, null, null, builder);

		return builder.toString();
	}

	@Override
	public String visit(ListValue term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append('[');

		for (int i = 0; i < term.value.length; ++i) {
			builder.append(term.value[i].apply(this));

			if (i < term.value.length - 1) {
				builder.append(", ");
			}
		}

		builder.append(']');

		return builder.toString();
	}

	@Override
	public String visit(IntegerValue term) throws NuException {

		return String.valueOf(term.value);
	}

	@Override
	public String visit(Module term) throws NuException {

		StringBuilder builder = new StringBuilder();

		Iterator<Lambda> iterator = term.lambdas.values().iterator();
		while (iterator.hasNext()) {
			Lambda decl = iterator.next();
			builder.append(decl.apply(this));
			if (iterator.hasNext()) {
				builder.append(System.lineSeparator());
			}
		}

		return builder.toString();
	}

	@Override
	public String visit(Multiplicative term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append('(');
		builder.append(term.lhs.apply(this));
		if (Multiplicative.Operator.MUL == term.operator) {
			builder.append(" * ");
		} else if (Multiplicative.Operator.DIV == term.operator) {
			builder.append(" / ");
		} else if (Multiplicative.Operator.MOD == term.operator) {
			builder.append(" % ");
		}
		builder.append(term.rhs.apply(this));
		builder.append(')');

		return builder.toString();
	}

	@Override
	public String visit(Negation term) throws NuException {

		return "!" + term.term.apply(this);
	}

	@Override
	public String visit(NoneValue term) throws NuException {

		return "none";
	}

	@Override
	public String visit(ObjectValue term) throws NuException {

		throw new NuException("java.print");
	}

	@Override
	public String visit(Relational term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append('(');
		builder.append(term.lhs.apply(this));
		if (Relational.Operator.LESS == term.operator) {
			builder.append(" < ");
		} else if (Relational.Operator.GREATER == term.operator) {
			builder.append(" > ");
		} else if (Relational.Operator.LESS_EQUAL == term.operator) {
			builder.append(" <= ");
		} else {
			builder.append(" >= ");
		}
		builder.append(term.rhs.apply(this));
		builder.append(')');

		return builder.toString();
	}

	@Override
	public String visit(Shift term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append('(');
		builder.append(term.lhs.apply(this));
		if (Shift.Direction.LEFT == term.direction) {
			builder.append(" << ");
		} else {
			builder.append(" >> ");
		}
		builder.append(term.rhs.apply(this));
		builder.append(')');

		return builder.toString();
	}

	@Override
	public String visit(StringValue term) throws NuException {

		StringBuilder builder = new StringBuilder();

		builder.append('"');
		builder.append(term.apply(new JavaValueVisitor()));
		builder.append('"');

		return builder.toString();
	}

	private Term visitPatternMatching(Lambda term, Term body, StringBuilder builder) throws NuException {

		if (!(body instanceof Conditional)) {
			return body;
		}

		Conditional conditional = (Conditional) body;
		if (!(conditional.condition instanceof Equality)) {
			return body;
		}

		Equality condition = (Equality) conditional.condition;
		if (condition.operator != Equality.Operator.EQUAL) {
			return body;
		}

		Identifier parameter = null;
		Value<?> value = null;

		if (condition.lhs instanceof Identifier) {
			parameter = (Identifier) condition.lhs;
			if (condition.rhs instanceof Value) {
				value = (Value<?>) condition.rhs;
			} else {
				return body;
			}
		} else if (condition.rhs instanceof Identifier) {
			parameter = (Identifier) condition.rhs;
			if (condition.lhs instanceof Value) {
				value = (Value<?>) condition.lhs;
			} else {
				return body;
			}
		} else {
			return body;
		}

		this.visitPlain(term, conditional.then, parameter, value, builder);
		builder.append(System.lineSeparator());

		return this.visitPatternMatching(term, conditional.otherwise, builder);
	}

	private void visitPlain(Lambda term, Term body, Identifier parameterName, Value<?> value, StringBuilder builder)
			throws NuException {

		if (term.name != null) {
			builder.append(term.name);
		}

		if (!term.parameters.isEmpty()) {
			builder.append('(');
		}

		for (int i = 0; i < term.parameters.size(); ++i) {
			Term parameter = term.parameters.get(i);

			if (parameterName != null && parameter.equals(parameterName)) {
				builder.append(value.apply(this));
			} else {
				builder.append(term.parameters.get(i).apply(this));
			}

			if (i < term.parameters.size() - 1) {
				builder.append(", ");
			}
		}

		if (!term.parameters.isEmpty()) {
			builder.append(')');
		}

		builder.append(" = ");
		builder.append(body.apply(this));

		if (!term.lambdas.isEmpty()) {
			builder.append(System.lineSeparator());

			for (Lambda let : term.lambdas.values()) {
				builder.append("\t\\ (");
				builder.append(let.apply(this));
				builder.append(')');
				builder.append(System.lineSeparator());
			}
		}
	}
}
