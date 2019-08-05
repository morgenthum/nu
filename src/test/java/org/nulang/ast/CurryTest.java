package org.nulang.ast;

import static org.nulang.core.Builder.add;
import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.decl;
import static org.nulang.core.Builder.id;
import static org.nulang.core.Builder.val;

import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class CurryTest extends ExecutorTest {

	public Lambda addCurry() {

		Term term = add(add(id("x"), id("y")), id("z"));

		return decl("add", term, "x", "y", "z");
	}

	@Returns("(z) = ((7 + 5) + z)")
	public Lambda main() {

		return decl("main", call(call("add", val(7)), val(5)));
	}
}
