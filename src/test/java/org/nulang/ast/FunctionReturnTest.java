package org.nulang.ast;

import static org.nulang.core.Builder.add;
import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.decl;
import static org.nulang.core.Builder.id;
import static org.nulang.core.Builder.val;

import org.nulang.core.terms.Lambda;

public class FunctionReturnTest extends ExecutorTest {

	@Returns("17")
	public Lambda main() {

		return decl("main", call(call("add", val(7)), val(10)));
	}

	public Lambda addReturn() {

		return decl("add", decl(null, add(id("x"), id("y")), "y"), "x");
	}
}
