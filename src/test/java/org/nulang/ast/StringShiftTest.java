package org.nulang.ast;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class StringShiftTest extends ExecutorTest {

	@Returns("\"all\"")
	public Lambda main() {

		Term value = Builder.val("hallo");

		return Builder.decl("main", Builder.lshft(Builder.rshft(value, 1), 1));
	}
}
