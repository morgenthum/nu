package org.nulang.ast;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

/**
 * Proves the turing completeness by describing all μ-recursive functions.
 * 
 * @author Mario Morgenthum
 * @see https://en.wikipedia.org/wiki/%CE%9C-recursive_function
 */
public class MRecursiveFunctionsTest extends ExecutorTest {

	public Lambda constant() {

		return Builder.decl("constant", Builder.val(5), "x");
	}

	public Lambda successor() {

		return Builder.decl("successor", Builder.add(Builder.id("x"), Builder.ONE), "x");
	}

	public Lambda identity() {

		return Builder.decl("identity", Builder.id("x"), "x");
	}

	public Lambda multiply() {

		Term condition = Builder.eq(Builder.id("x"), Builder.ONE);
		Term then = Builder.id("y");
		Term recurse = Builder.call("multiply", Builder.sub(Builder.id("x"), Builder.ONE), Builder.id("y"));
		Term otherwise = Builder.add(Builder.id("y"), recurse);

		return Builder.decl("multiply", Builder.cnd(condition, then, otherwise), "x", "y");
	}

	@Returns("42")
	public Lambda main() {

		Term lhs = Builder.call("successor", Builder.call("identity", Builder.call("constant", Builder.val(10))));
		Term rhs = Builder.val(7);

		return Builder.decl("main", Builder.call("multiply", lhs, rhs));
	}
}
