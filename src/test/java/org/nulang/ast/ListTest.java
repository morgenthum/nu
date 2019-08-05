package org.nulang.ast;

import static org.nulang.core.Builder.EMPTY_LIST;
import static org.nulang.core.Builder.add;
import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.concat;
import static org.nulang.core.Builder.cnd;
import static org.nulang.core.Builder.decl;
import static org.nulang.core.Builder.id;
import static org.nulang.core.Builder.lshft;

import java.util.Arrays;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class ListTest extends ExecutorTest {

	@Returns("[5, 2, 4]")
	public Lambda main() {

		Object first = Arrays.asList('h', 'a', 'l', 'l', 'o');
		Object second = Arrays.asList('d', 'u');
		Object third = Arrays.asList('d', 'o', 'r', 't');
		Term values = Builder.val(Arrays.asList(first, second, third));

		Term map = call("map", id("length"), values);

		return decl("main", map);
	}

	public Lambda map() {

		Term condition = Builder.eq(EMPTY_LIST, id("values"));
		Term then = EMPTY_LIST;
		Term mapFirst = call("f", Builder.idx(id("values"), 0));
		Term mapRecurse = call("map", id("f"), lshft(id("values"), 1));
		Term otherwise = concat(mapFirst, mapRecurse);

		return decl("map", cnd(condition, then, otherwise), "f", "values");
	}

	public Lambda length() {

		return decl("length", call("length_rec", id("values"), Builder.ZERO), "values");
	}

	public Lambda lengthRec() {

		Term condition = Builder.eq(EMPTY_LIST, id("values"));
		Term then = id("n");
		Term tail = lshft(id("values"), 1);
		Term otherwise = call("length_rec", tail, add(id("n"), Builder.ONE));

		return decl("length_rec", cnd(condition, then, otherwise), "values", "n");
	}
}
