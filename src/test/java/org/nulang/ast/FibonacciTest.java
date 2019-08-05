package org.nulang.ast;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class FibonacciTest extends ExecutorTest {

	@Returns("55")
	public Lambda main() {

		return Builder.decl("main", Builder.call("fibonacci", Builder.val(10)));
	}

	public Lambda fibonacci() {

		Term recurse1 = Builder.call("fibonacci", Builder.sub(Builder.id("n"), Builder.val(1)));
		Term recurse2 = Builder.call("fibonacci", Builder.sub(Builder.id("n"), Builder.val(2)));
		Term otherwise = Builder.add(recurse1, recurse2);

		Term condition1 = Builder.eq(Builder.id("n"), Builder.ONE);
		Term condition2 = Builder.eq(Builder.id("n"), Builder.val(2));

		Term conditional2 = Builder.cnd(condition2, Builder.ONE, otherwise);
		Term conditional1 = Builder.cnd(condition1, Builder.ONE, conditional2);

		return Builder.decl("fibonacci", conditional1, "n");
	}
}
