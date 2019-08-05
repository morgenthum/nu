package org.nulang.ast;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class HigherOrderTest extends ExecutorTest {

	public Lambda negate() {

		Term call = Builder.call("f", Builder.id("b"));
		Term negation = Builder.neg(call);
		Term inner = Builder.decl(null, negation, "b");

		return Builder.decl("negate", inner, "f");
	}

	public Lambda isTrue() {

		return Builder.decl("is_true", Builder.id("b"), "b");
	}

	public Lambda isFalse() {

		return Builder.decl("is_false", Builder.call("negate", Builder.id("is_true")));
	}

	@Returns("false")
	public Lambda main() {

		return Builder.decl("main", Builder.call("is_false", Builder.val(true)));
	}
}
