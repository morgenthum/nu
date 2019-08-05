package org.nulang.ast;

import java.util.Arrays;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class IndexTest extends ExecutorTest {

	@Returns("[\"hallo\", \"welt\"]")
	public Lambda main() {

		Term values = Builder.val(Arrays.asList("hallo", "meine", "welt"));

		return Builder.decl("main", Builder.idx(values, 0, 2));
	}
}
