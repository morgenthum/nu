package org.nulang.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nulang.core.Builder;
import org.nulang.core.Comparator;
import org.nulang.core.Copier;
import org.nulang.core.JavaMethod;
import org.nulang.core.Loader;
import org.nulang.core.NuException;
import org.nulang.core.TermsVisitor;
import org.nulang.core.operations.Additivable;
import org.nulang.core.operations.Concatable;
import org.nulang.core.operations.Indexable;
import org.nulang.core.operations.Shiftable;
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
import org.nulang.core.terms.values.NumberValue;
import org.nulang.core.terms.values.ObjectValue;
import org.nulang.core.terms.values.StringValue;
import org.nulang.core.terms.values.Value;
import org.nulang.interpreter.operations.AdditiveInterpreter;
import org.nulang.interpreter.operations.ConcatInterpreter;
import org.nulang.interpreter.operations.IndexInterpreter;
import org.nulang.interpreter.operations.ShiftInterpreter;

public class Interpreter implements TermsVisitor<Term> {

	private Module module;

	private Lambda lambda;
	private Frame frame;

	private Map<Result, Term> results = new HashMap<>();

	public Interpreter() {
	}

	public Interpreter(Module module) {

		this.module = module;
	}

	@Override
	public Term visit(Additive term) throws NuException {

		Term lhs = term.lhs.apply(this);
		if (!(lhs instanceof Additivable)) {
			throw new NuException("operation.additive.operands", lhs);
		}

		Term rhs = term.rhs.apply(this);

		Additivable additivable = (Additivable) lhs;
		return additivable.apply(new AdditiveInterpreter(term), rhs, term.operator, this);
	}

	@Override
	public Term visit(BooleanValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Call term) throws NuException {

		Term resolved = term.object.apply(this);

		// Called function already resolved
		if (!(resolved instanceof Lambda)) {
			if (!term.parameters.isEmpty()) {
				throw new NuException("interpreter.call.lambda", resolved);
			}
			return resolved;
		}

		// Calling java method
		if (resolved instanceof JavaMethod) {
			List<Term> parameters = new ArrayList<>(term.parameters.size());
			for (Term parameter : term.parameters) {
				parameters.add(parameter.apply(this));
			}
			return JavaCaller.call((JavaMethod) resolved, parameters);
		}

		Lambda lambda = (Lambda) resolved;
		List<Term> parameters = term.parameters;

		return this.call(lambda, parameters);
	}

	@Override
	public Term visit(CharacterValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Concat term) throws NuException {

		Term lhs = term.lhs.apply(this);
		if (!(lhs instanceof Concatable)) {
			throw new NuException("operation.concat.operands");
		}

		Term rhs = term.rhs.apply(this);

		Concatable concatable = lhs;
		return concatable.apply(new ConcatInterpreter(), rhs, this);
	}

	@Override
	public Term visit(Conditional term) throws NuException {

		Term condition = term.condition.apply(this);
		if (!(condition instanceof BooleanValue)) {
			throw new NuException("operation.conditional.operands", condition);
		}

		return ((BooleanValue) condition).value ? term.then.apply(this) : term.otherwise.apply(this);
	}

	@Override
	public Term visit(FloatValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Index term) throws NuException {

		Term lhs = term.lhs.apply(this);
		if (!(lhs instanceof Indexable)) {
			throw new NuException("operation.index.operands", lhs);
		}

		if (term.rhs instanceof FloatValue) {
			throw new NuException("operation.index.double", term);
		}

		Indexable accessable = (Indexable) lhs;
		return accessable.apply(new IndexInterpreter(), term.rhs, this);
	}

	@Override
	public Term visit(Equality term) throws NuException {

		Term lhs = term.lhs.apply(this);
		Term rhs = term.rhs.apply(this);

		boolean equals = lhs.apply(new Comparator(rhs));

		return Builder.val(term.operator == Equality.Operator.EQUAL ? equals : !equals);
	}

	@Override
	public Term visit(Identifier term) throws NuException {

		Term value;
		if (this.frame != null && ((value = this.frame.lookup(term.identifier)) != null)) {
			return value;
		}

		if (this.lambda != null) {
			Lambda lambda = this.lambda.lookup(term.identifier);
			if (lambda != null) {
				Term result = lambda.apply(this);
				this.frame.addResolvedLet(term.identifier, result);
				return result;
			}
		}

		if (this.module != null) {
			Lambda lambda = this.module.lookup(term.identifier);
			if (lambda != null) {
				return lambda.apply(this);
			}
		}

		String elementName = term.identifier.replace('_', '.');
		Term resolved = JavaCaller.resolve(elementName);

		if (resolved != null) {
			return resolved;
		}

		throw new NuException("interpreter.identifier", term.identifier);
	}

	@Override
	public Term visit(Lambda term) throws NuException {

		Map<Identifier, Term> substitutions = new HashMap<>();

		if (this.frame != null) {
			for (String name : this.frame.variables.keySet()) {
				Identifier key = new Identifier(name);

				if (!term.parameters.contains(key)) {
					Term value = this.frame.variables.get(name);
					substitutions.put(key, value);
				}
			}

			if (!substitutions.isEmpty()) {
				term = (Lambda) term.apply(Copier.INSTANCE);
				term = (Lambda) term.apply(new Substitutor(substitutions));
			}
		}

		// We can already resolve the function if it has no parameters
		if (term.parameters.isEmpty()) {

			// We can inline the lambda body directly if it has no let-terms
			if (term.lambdas.isEmpty()) {
				return term.body.apply(this);
			}

			// If we have let-terms, we call the function to create a new
			// context (memoization of resolved let-terms etc.)
			return this.call(term, Collections.emptyList());
		}

		return term;
	}

	@Override
	public Term visit(ListValue term) throws NuException {

		List<Term> list = new LinkedList<>();

		for (Term value : term.value) {
			list.add(value.apply(this));
		}

		return Builder.val(list);
	}

	@Override
	public Term visit(IntegerValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(Module term) throws NuException {

		for (String dependencyName : term.dependencies.keySet()) {
			Module dependency = Loader.loadModule(dependencyName);
			if (dependency == null) {
				throw new NuException("module.find", dependencyName);
			}

			term.dependencies.put(dependencyName, dependency);
		}

		return Builder.id("main").apply(this);
	}

	@Override
	public Term visit(Multiplicative term) throws NuException {

		Term lhsTerm = term.lhs.apply(this);
		Term rhsTerm = term.rhs.apply(this);

		if (!(lhsTerm instanceof NumberValue) || !(rhsTerm instanceof NumberValue)) {
			throw new NuException("operation.multiplicative.operands");
		}

		NumberValue lhs = (NumberValue) lhsTerm;
		NumberValue rhs = (NumberValue) rhsTerm;

		boolean integer = lhs.value instanceof Long && rhs.value instanceof Long;

		if (Multiplicative.Operator.MUL == term.operator) {
			Number result = null;
			if (integer) {
				result = lhs.value.longValue() * rhs.value.longValue();
			} else {
				result = lhs.value.doubleValue() * rhs.value.doubleValue();
			}
			return Builder.val(result);
		} else if (Multiplicative.Operator.DIV == term.operator) {
			Number result = null;
			if (integer) {
				result = lhs.value.longValue() / rhs.value.longValue();
			} else {
				result = lhs.value.doubleValue() / rhs.value.doubleValue();
			}
			return Builder.val(result);
		} else {
			Number result = null;
			if (integer) {
				result = lhs.value.longValue() % rhs.value.longValue();
			} else {
				result = lhs.value.doubleValue() % rhs.value.doubleValue();
			}
			return Builder.val(result);
		}
	}

	@Override
	public Term visit(Negation term) throws NuException {

		Term value = term.term.apply(this);
		if (!(value instanceof BooleanValue)) {
			throw new NuException("operation.negation.operands");
		}

		return Builder.val(!((BooleanValue) value).value);
	}

	@Override
	public Term visit(NoneValue term) throws NuException {

		return term;
	}

	@Override
	public Term visit(ObjectValue term) throws NuException {

		return term;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Term visit(Relational term) throws NuException {

		Term lhsTerm = term.lhs.apply(this);
		Term rhsTerm = term.rhs.apply(this);

		if (!(lhsTerm instanceof Value<?>) || !(rhsTerm instanceof Value<?>)) {
			throw new NuException("operation.relational.operands");
		}

		Value<?> lhs = (Value<?>) lhsTerm;
		Value<?> rhs = (Value<?>) rhsTerm;

		Comparable<Object> lhsValue = (Comparable<Object>) lhs.value;
		Comparable<Object> rhsValue = (Comparable<Object>) rhs.value;

		int result = lhsValue.compareTo(rhsValue);

		if (Relational.Operator.LESS == term.operator) {
			return Builder.val(result < 0);
		} else if (Relational.Operator.GREATER == term.operator) {
			return Builder.val(result > 0);
		} else if (Relational.Operator.LESS_EQUAL == term.operator) {
			return Builder.val(result <= 0);
		} else if (Relational.Operator.GREATER_EQUAL == term.operator) {
			return Builder.val(result >= 0);
		}

		throw new NuException("operation.relational.operator");
	}

	@Override
	public Term visit(Shift term) throws NuException {

		Term lhs = term.lhs.apply(this);
		if (!(lhs instanceof Shiftable)) {
			throw new NuException("operation.shift.operands");
		}

		Term rhs = term.rhs.apply(this);

		Shiftable shiftable = (Shiftable) lhs;
		return shiftable.apply(new ShiftInterpreter(), rhs, term.direction, this);
	}

	@Override
	public Term visit(StringValue term) throws NuException {

		return term;
	}

	private Term call(Lambda lambda, List<Term> parameters) throws NuException {

		while (parameters.size() != lambda.parameters.size()) {

			// Calling with fewer parameters than expected causes currying
			if (parameters.size() < lambda.parameters.size()) {
				return this.curry(lambda, parameters);
			}

			// Resolve calls with more parameters than expected one by one
			if (parameters.size() > lambda.parameters.size()) {
				int subSize = lambda.parameters.size();
				List<Term> subParameters = parameters.subList(0, subSize);

				Term subResult = this.call(lambda, subParameters);
				if (!(subResult instanceof Lambda)) {
					throw new NuException("interpreter.call.lambda", subResult);
				}

				lambda = (Lambda) subResult;
				parameters = parameters.subList(subSize, parameters.size());
			}
		}

		// Resolve parameters for the new frame
		Map<String, Term> variables = new HashMap<>();
		for (int i = 0; i < parameters.size(); ++i) {
			Term parameter = parameters.get(i);
			String name = ((Identifier) lambda.parameters.get(i)).identifier;
			variables.put(name, parameter.apply(this));
		}

		Result key = new Result(lambda, variables);
		Term result = this.results.get(key);
		if (result != null) {
			return result;
		}

		// Push new frame, execute the function body and restore the old frame
		Lambda oldLambda = this.lambda;
		Frame oldFrame = this.frame;
		try {
			this.lambda = lambda;
			this.frame = new Frame(variables);

			result = lambda.body.apply(this);
			this.results.put(key, result);
		} finally {
			this.frame = oldFrame;
			this.lambda = oldLambda;
		}

		return result;
	}

	private Lambda curry(Lambda lambda, List<Term> parameters) throws NuException {

		Lambda curried = (Lambda) lambda.apply(Copier.INSTANCE);
		curried.name = null;
		curried.parameters.clear();

		Map<Identifier, Term> substitutions = new HashMap<>();

		for (int i = 0; i < lambda.parameters.size(); ++i) {
			Identifier name = (Identifier) lambda.parameters.get(i);

			if (i < parameters.size()) {
				Term parameter = parameters.get(i);
				substitutions.put(name, parameter.apply(this));
			} else {
				curried.parameters.add(name);
			}
		}

		curried.body = curried.body.apply(new Substitutor(substitutions));

		return curried;
	}
}
