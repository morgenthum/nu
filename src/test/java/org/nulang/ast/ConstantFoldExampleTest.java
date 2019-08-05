package org.nulang.ast;

import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.cnd;
import static org.nulang.core.Builder.decl;
import static org.nulang.core.Builder.div;
import static org.nulang.core.Builder.gt;
import static org.nulang.core.Builder.mul;
import static org.nulang.core.Builder.sub;
import static org.nulang.core.Builder.val;

import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class ConstantFoldExampleTest extends ExecutorTest {

	@Returns("4")
	public Lambda main() {

		return decl("main", call("result"));
	}

	public Lambda a() {

		return decl("a", val(30));
	}

	public Lambda b() {

		return decl("b", sub(val(9), div(call("a"), val(5))));
	}

	public Lambda c() {

		return decl("c", mul(call("b"), val(4)));
	}

	public Lambda cc() {

		Term then = sub(call("c"), val(10));
		Term condition = gt(call("c"), val(10));

		return decl("cc", cnd(condition, then, call("c")));
	}

	public Lambda result() {

		return decl("result", mul(call("cc"), div(val(60), call("a"))));
	}
}
