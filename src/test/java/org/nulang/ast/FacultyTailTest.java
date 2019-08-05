package org.nulang.ast;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class FacultyTailTest extends ExecutorTest {

	public Lambda faculty() {

		Term condition = Builder.lt(Builder.id("n"), Builder.val(2));
		Term then = Builder.id("acc");

		Term mul = Builder.mul(Builder.id("n"), Builder.id("acc"));
		Term sub = Builder.sub(Builder.id("n"), Builder.val(1));
		Term otherwise = Builder.call("faculty", mul, sub);

		return Builder.decl("faculty", Builder.cnd(condition, then, otherwise), "acc", "n");
	}

	@Returns("3628800")
	public Lambda main() {

		return Builder.decl("main", Builder.call("faculty", Builder.val(1), Builder.val(10)));
	}
}
