package org.nulang.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import org.nulang.core.terms.binary.Shift.Direction;
import org.nulang.core.terms.values.BooleanValue;
import org.nulang.core.terms.values.CharacterValue;
import org.nulang.core.terms.values.FloatValue;
import org.nulang.core.terms.values.IntegerValue;
import org.nulang.core.terms.values.ListValue;
import org.nulang.core.terms.values.NoneValue;
import org.nulang.core.terms.values.NumberValue;
import org.nulang.core.terms.values.ObjectValue;
import org.nulang.core.terms.values.StringValue;

public class Builder {

	public static final NumberValue ZERO = val(0);
	public static final NumberValue ONE = val(1);

	public static final ListValue EMPTY_LIST = val(Collections.emptyList());
	public static final StringValue EMPTY_STRING = val("");

	/**
	 * @param name
	 * @param lambdas
	 * @return
	 */
	public static Module module(String name, Lambda... lambdas) {

		Module module = new Module();

		module.name = name;

		for (Lambda lambda : lambdas) {
			module.lambdas.put(lambda.name, lambda);
		}

		return module;
	}

	/**
	 * @param clazz
	 * @param method
	 * @param params
	 * @return
	 */
	public static JavaMethod decl(Class<?> clazz, String methodName) {

		return new JavaMethod(clazz, methodName);
	}

	/**
	 * @param identifier
	 * @param body
	 * @param params
	 * @return
	 */
	public static Lambda decl(String identifier, Term body, String... params) {

		Lambda term = new Lambda();
		term.name = identifier;
		term.parameters.addAll(Arrays.stream(params).map(p -> new Identifier(p)).collect(Collectors.toList()));
		term.body = body;

		return term;
	}

	/**
	 * @param identifier
	 * @param params
	 * @return
	 */
	public static Call call(String identifier, Term... params) {

		return call(id(identifier), params);
	}

	/**
	 * @param term
	 * @param params
	 * @return
	 */
	public static Call call(Term term, Term... params) {

		return new Call(term, params);
	}

	/**
	 * @param value
	 * @return
	 */
	public static Term val(Object value) {

		if (value == null) {
			return NoneValue.INSTANCE;
		} else if (value instanceof Term) {
			return (Term) value;
		} else if (value instanceof Boolean) {
			return val((Boolean) value);
		} else if (value instanceof Character) {
			return val((Character) value);
		} else if (value instanceof String) {
			return val((String) value);
		} else if (value instanceof Number) {
			return val((Number) value);
		} else if (value instanceof List<?>) {
			return val((List<?>) value);
		} else {
			return new ObjectValue(value);
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static BooleanValue val(Boolean value) {

		return new BooleanValue(value);
	}

	/**
	 * @param value
	 * @return
	 */
	public static CharacterValue val(Character value) {

		return new CharacterValue(value);
	}

	/**
	 * @param value
	 * @return
	 */
	public static NumberValue val(Number value) {

		if (value instanceof Float || value instanceof Double) {
			return new FloatValue(value.doubleValue());
		}

		return new IntegerValue(value.longValue());
	}

	/**
	 * @param value
	 * @return
	 */
	public static ListValue val(List<?> value) {

		return val(value.toArray());
	}

	/**
	 * @param value
	 * @return
	 */
	public static ListValue val(Object[] values) {

		Term[] terms = new Term[values.length];

		boolean string = false;

		if (values.length != 0) {
			string = true;

			for (int i = 0; i < values.length; ++i) {
				Term term = val(values[i]);
				if (!(term instanceof CharacterValue)) {
					string = false;
				}

				terms[i] = term;

			}
		}

		return string ? new StringValue(terms) : new ListValue(terms);
	}

	/**
	 * @param value
	 * @return
	 */
	public static StringValue val(String value) {

		Term[] term = new Term[value.length()];

		for (int i = 0; i < term.length; ++i) {
			term[i] = Builder.val(value.charAt(i));
		}

		return new StringValue(term);
	}

	/**
	 * @param id
	 * @return
	 */
	public static Identifier id(String id) {

		return new Identifier(id);
	}

	/**
	 * @param values
	 * @param index
	 * @return
	 */
	public static Index idx(Term values, int index) {

		return idx(values, val(index));
	}

	/**
	 * @param values
	 * @param indexes
	 * @return
	 */
	public static Index idx(Term values, int... indexes) {

		List<Integer> list = new ArrayList<>();
		for (int index : indexes) {
			list.add(index);
		}

		return idx(values, val(list));
	}

	/**
	 * @param object
	 * @param element
	 * @return
	 */
	public static Index idx(Term object, Term element) {

		Index term = new Index();
		term.lhs = object;
		term.rhs = element;

		return term;
	}

	/**
	 * @param condition
	 * @param then
	 * @param otherwise
	 * @return
	 */
	public static Conditional cnd(Term condition, Term then, Term otherwise) {

		Conditional term = new Conditional();
		term.condition = condition;
		term.then = then;
		term.otherwise = otherwise;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Equality eq(Term lhs, Term rhs) {

		Equality term = new Equality();
		term.operator = Equality.Operator.EQUAL;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Equality ne(Term lhs, Term rhs) {

		Equality term = new Equality();
		term.operator = Equality.Operator.UNEQUAL;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Relational ge(Term lhs, Term rhs) {

		Relational term = new Relational();
		term.operator = Relational.Operator.GREATER_EQUAL;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Relational gt(Term lhs, Term rhs) {

		Relational term = new Relational();
		term.operator = Relational.Operator.GREATER;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Relational le(Term lhs, Term rhs) {

		Relational term = new Relational();
		term.operator = Relational.Operator.LESS_EQUAL;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Relational lt(Term lhs, Term rhs) {

		Relational term = new Relational();
		term.operator = Relational.Operator.LESS;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Additive add(Term lhs, Term rhs) {

		Additive term = new Additive();
		term.operator = Additive.Operator.ADD;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Additive sub(Term lhs, Term rhs) {

		Additive term = new Additive();
		term.operator = Additive.Operator.SUB;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Multiplicative div(Term lhs, Term rhs) {

		Multiplicative term = new Multiplicative();
		term.operator = Multiplicative.Operator.DIV;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Multiplicative mod(Term lhs, Term rhs) {

		Multiplicative term = new Multiplicative();
		term.operator = Multiplicative.Operator.MOD;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Multiplicative mul(Term lhs, Term rhs) {

		Multiplicative term = new Multiplicative();
		term.operator = Multiplicative.Operator.MUL;
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param term
	 * @param times
	 * @return
	 */
	public static Shift lshft(Term term, int times) {

		return lshft(term, val(times));
	}

	/**
	 * @param term
	 * @param times
	 * @return
	 */
	public static Shift lshft(Term term, Term times) {

		Shift shift = new Shift();
		shift.direction = Direction.LEFT;
		shift.lhs = term;
		shift.rhs = times;

		return shift;
	}

	/**
	 * @param term
	 * @param times
	 * @return
	 */
	public static Shift rshft(Term term, int times) {

		return rshft(term, val(times));
	}

	/**
	 * @param term
	 * @param times
	 * @return
	 */
	public static Shift rshft(Term term, Term times) {

		Shift shift = new Shift();
		shift.direction = Direction.RIGHT;
		shift.lhs = term;
		shift.rhs = times;

		return shift;
	}

	/**
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static Concat concat(Term lhs, Term rhs) {

		Concat term = new Concat();
		term.lhs = lhs;
		term.rhs = rhs;

		return term;
	}

	/**
	 * @param term
	 * @return
	 */
	public static Negation neg(Term term) {

		Negation negation = new Negation();
		negation.term = term;

		return negation;
	}
}
