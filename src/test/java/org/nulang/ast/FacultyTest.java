package org.nulang.ast;

import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.cnd;
import static org.nulang.core.Builder.decl;
import static org.nulang.core.Builder.id;
import static org.nulang.core.Builder.mul;
import static org.nulang.core.Builder.sub;
import static org.nulang.core.Builder.val;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class FacultyTest extends ExecutorTest {

	@Returns("3628800")
	public Lambda main() {

		return decl("main", call("faculty", val(10)));
	}

	public Lambda faculty() {

		Term decrement = sub(id("n"), Builder.ONE);
		Term calculation = mul(id("n"), call("faculty", decrement));
		Term condition = Builder.eq(id("n"), Builder.ZERO);
		Term conditional = cnd(condition, Builder.ONE, calculation);

		return decl("faculty", conditional, "n");
	}
}
