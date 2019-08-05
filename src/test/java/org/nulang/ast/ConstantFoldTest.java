package org.nulang.ast;

import static org.nulang.core.Builder.add;
import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.decl;
import static org.nulang.core.Builder.id;
import static org.nulang.core.Builder.mul;
import static org.nulang.core.Builder.val;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;

public class ConstantFoldTest extends ExecutorTest {

	@Returns("50")
	public Lambda main() {

		return decl("main", call("fiveTimes", val(10)));
	}

	public Lambda one() {

		return decl("one", Builder.ONE);
	}

	public Lambda inc() {

		return decl("inc", add(id("x"), call("one")), "x");
	}

	public Lambda fiveTimes() {

		return decl("fiveTimes", mul(call("inc", add(val(2), val(2))), id("x")), "x");
	}

}
