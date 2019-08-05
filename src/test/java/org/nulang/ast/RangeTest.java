package org.nulang.ast;

import static org.nulang.core.Builder.EMPTY_LIST;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class RangeTest extends ExecutorTest {

	@Returns("[0, 2, 4, 6, 8, 10]")
	public Lambda main() {

		Term range = Builder.call("range", Builder.ZERO, Builder.val(10));
		Term filter = Builder.call("filter", Builder.id("even"), range);

		return Builder.decl("main", filter);
	}

	public Lambda range() {

		Term first = Builder.id("first");
		Term last = Builder.id("last");

		Term condition = Builder.le(first, last);
		Term recurse = Builder.call("range", Builder.add(first, Builder.ONE), last);
		Term then = Builder.concat(first, recurse);
		Term otherwise = EMPTY_LIST;
		Term conditional = Builder.cnd(condition, then, otherwise);

		return Builder.decl("range", conditional, "first", "last");
	}

	public Lambda filter() {

		Term f = Builder.id("f");
		Term values = Builder.id("values");
		Term values0 = Builder.idx(values, 0);
		Term recurse = Builder.call("filter", f, Builder.lshft(values, 1));

		Term condition = Builder.call(f, values0);
		Term then = Builder.concat(values0, recurse);
		Term otherwise = recurse;
		Term conditional = Builder.cnd(condition, then, otherwise);

		Term checkEmpty = Builder.cnd(Builder.eq(values, EMPTY_LIST), EMPTY_LIST, conditional);

		return Builder.decl("filter", checkEmpty, "f", "values");
	}

	public Lambda even() {

		Term mod = Builder.mod(Builder.id("n"), Builder.val(2));
		Term even = Builder.eq(mod, Builder.ZERO);

		return Builder.decl("even", even, "n");
	}
}
