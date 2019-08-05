package org.nulang.ast;

import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.decl;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class FunctionParameterTest extends ExecutorTest {

	@Returns("12")
	public Lambda main() {

		return decl("main", call("add", Builder.val(7), Builder.id("value")));
	}

	public Lambda value() {

		return decl("value", Builder.val(5));
	}

	public Lambda add() {

		Term add = Builder.add(Builder.id("x"), Builder.id("y"));

		return decl("add", add, "x", "y");
	}
}
