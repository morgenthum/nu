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
import org.nulang.core.terms.values.StringValue;
import org.nulang.core.terms.values.Value;

public class Hasher implements TermsVisitor<Integer> {

	public static final Hasher INSTANCE = new Hasher();

	private Hasher() {
	}

	@Override
	public Integer visit(Additive term) throws NuException {

		return this.visit((BinaryTerm) term) + term.operator.hashCode();
	}

	@Override
	public Integer visit(BooleanValue term) throws NuException {

		return this.visit((Value<?>) term);
	}

	@Override
	public Integer visit(Call term) throws NuException {

		int hash = term.object.apply(this);

		for (Term parameter : term.parameters) {
			hash += parameter.apply(this);
		}

		return hash;
	}

	@Override
	public Integer visit(CharacterValue term) throws NuException {

		return this.visit((Value<?>) term);
	}

	@Override
	public Integer visit(Concat term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Integer visit(Conditional term) throws NuException {

		return term.condition.apply(this) + term.then.apply(this) + term.otherwise.apply(this);
	}

	@Override
	public Integer visit(FloatValue term) throws NuException {

		return this.visit((Value<?>) term);
	}

	@Override
	public Integer visit(Index term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Integer visit(Equality term) throws NuException {

		return this.visit((BinaryTerm) term);
	}

	@Override
	public Integer visit(Identifier term) throws NuException {

		return term.identifier.hashCode();
	}

	@Override
	public Integer visit(Lambda term) throws NuException {

		int hash = 0;

		if (term.name != null) {
			hash += term.name.hashCode();
		}

		for (Term parameter : term.parameters) {
			hash += parameter.apply(this);
		}

		hash += term.body.apply(this);

		return hash;
	}

	@Override
	public Integer visit(ListValue term) throws NuException {

		return this.visit((Value<?>) term);
	}

	@Override
	public Integer visit(IntegerValue term) throws NuException {

		return this.visit((Value<?>) term);
	}

	@Override
	public Integer visit(Module term) throws NuException {

		int hash = term.name.hashCode();

		for (String key : term.lambdas.keySet()) {
			hash += key.hashCode();
			key += term.lambdas.get(key).apply(this);
		}

		return hash;
	}

	@Override
	public Integer visit(Multiplicative term) throws NuException {

		return this.visit((BinaryTerm) term) + term.operator.hashCode();
	}

	@Override
	public Integer visit(Negation term) throws NuException {

		return term.term.apply(this);
	}

	@Override
	public Integer visit(NoneValue term) throws NuException {

		return 0;
	}

	@Override
	public Integer visit(ObjectValue term) throws NuException {

		return term.value.hashCode();
	}

	@Override
	public Integer visit(Relational term) throws NuException {

		return this.visit((BinaryTerm) term) + term.operator.hashCode();
	}

	@Override
	public Integer visit(Shift term) throws NuException {

		return this.visit((BinaryTerm) term) + term.direction.hashCode();
	}

	@Override
	public Integer visit(StringValue term) throws NuException {

		return this.visit((Value<?>) term);
	}

	private Integer visit(BinaryTerm term) throws NuException {

		return term.lhs.apply(this) + term.rhs.apply(this);
	}

	private Integer visit(Value<?> term) {

		return 31 * 1 + ((term.value == null) ? 0 : term.value.hashCode());
	}
}
