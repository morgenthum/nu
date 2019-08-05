package org.nulang.ast;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class StringMingleTest extends ExecutorTest {

	@Returns("\"hraacnkkeerr\"")
	public Lambda main() {

		Term h = Builder.val("hacker");
		Term r = Builder.val("ranker");

		return Builder.decl("main", Builder.call("mingle", h, r));
	}

	public Lambda mingle() {

		Term v1 = Builder.id("a");
		Term v2 = Builder.id("b");

		Term recurse = Builder.call("mingle", Builder.lshft(v1, 1), Builder.lshft(v2, 1));

		Term concat1 = Builder.add(Builder.idx(v1, 0), Builder.idx(v2, 0));
		Term concat2 = Builder.add(concat1, recurse);

		Term empty = Builder.cnd(Builder.eq(v1, Builder.EMPTY_STRING), Builder.EMPTY_STRING, concat2);

		return Builder.decl("mingle", empty, "a", "b");
	}
}
