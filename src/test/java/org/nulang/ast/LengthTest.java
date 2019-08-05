package org.nulang.ast;

import java.util.Arrays;
import java.util.Collections;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class LengthTest extends ExecutorTest {

	private static final Term EMPTY_LIST = Builder.val(Collections.emptyList());

	@Returns("4")
	public Lambda main() {

		Term values = Builder.val(Arrays.asList("hallo", "du", "dort", "drueben"));
		Term length = Builder.call("length", values);

		return Builder.decl("main", length);
	}

	public Lambda length() {

		return Builder.decl("length", Builder.call("length_rec", Builder.id("values"), Builder.ZERO), "values");
	}

	public Lambda lengthRec() {

		Term condition = Builder.eq(EMPTY_LIST, Builder.id("values"));
		Term then = Builder.id("n");
		Term tail = Builder.lshft(Builder.id("values"), 1);
		Term otherwise = Builder.call("length_rec", tail, Builder.add(Builder.id("n"), Builder.ONE));

		return Builder.decl("length_rec", Builder.cnd(condition, then, otherwise), "values", "n");
	}
}
