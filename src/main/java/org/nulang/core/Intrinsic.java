package org.nulang.core;

import java.util.HashMap;
import java.util.Map;

import org.nulang.core.terms.Lambda;

public class Intrinsic {

	public static Map<String, Lambda> OPERATORS = new HashMap<>();
	static {
		OPERATORS.put("+", Intrinsic.add());
		OPERATORS.put("-", Intrinsic.sub());
		OPERATORS.put("*", Intrinsic.mul());
		OPERATORS.put("/", Intrinsic.div());
		OPERATORS.put("%", Intrinsic.mod());
		OPERATORS.put(".", Intrinsic.idx());
		OPERATORS.put("++", Intrinsic.concat());
		OPERATORS.put("==", Intrinsic.eq());
		OPERATORS.put("!=", Intrinsic.ne());
		OPERATORS.put("<", Intrinsic.lt());
		OPERATORS.put("<=", Intrinsic.le());
		OPERATORS.put(">", Intrinsic.gt());
		OPERATORS.put(">=", Intrinsic.ge());
		OPERATORS.put("<<", Intrinsic.lshft());
		OPERATORS.put(">>", Intrinsic.rshft());
		OPERATORS.put("!", Intrinsic.neg());
	}

	private static Lambda add() {

		return Builder.decl(null, Builder.add(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda sub() {

		return Builder.decl(null, Builder.sub(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda mul() {

		return Builder.decl(null, Builder.mul(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda div() {

		return Builder.decl(null, Builder.div(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda mod() {

		return Builder.decl(null, Builder.sub(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda idx() {

		return Builder.decl(null, Builder.idx(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda concat() {

		return Builder.decl(null, Builder.concat(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda eq() {

		return Builder.decl(null, Builder.eq(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda ne() {

		return Builder.decl(null, Builder.ne(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda lt() {

		return Builder.decl(null, Builder.lt(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda le() {

		return Builder.decl(null, Builder.le(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda gt() {

		return Builder.decl(null, Builder.gt(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda ge() {

		return Builder.decl(null, Builder.ge(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda lshft() {

		return Builder.decl(null, Builder.lshft(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda rshft() {

		return Builder.decl(null, Builder.rshft(Builder.id("x"), Builder.id("y")), "x", "y");
	}

	private static Lambda neg() {

		return Builder.decl(null, Builder.neg(Builder.id("x")), "x");
	}
}
