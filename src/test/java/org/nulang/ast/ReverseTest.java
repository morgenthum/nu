package org.nulang.ast;

import static org.nulang.core.Builder.val;

import java.util.Arrays;
import java.util.Collections;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class ReverseTest extends ExecutorTest {

	private static final Term EMPTY_LIST = val(Collections.emptyList());

	@Returns("[5, 4, 3, 2, 1]")
	public Lambda main() {

		Term values = Builder.val(Arrays.asList(1, 2, 3, 4, 5));

		return Builder.decl("main", Builder.call("reverse", values));
	}

	public Lambda reverse() {

		Term condition = Builder.eq(EMPTY_LIST, Builder.id("values"));
		Term then = EMPTY_LIST;
		Term tail = Builder.call("reverse", Builder.lshft(Builder.id("values"), 1));
		Term head = Builder.idx(Builder.id("values"), 0);
		Term otherwise = Builder.concat(tail, head);

		return Builder.decl("reverse", Builder.cnd(condition, then, otherwise), "values");
	}
}
