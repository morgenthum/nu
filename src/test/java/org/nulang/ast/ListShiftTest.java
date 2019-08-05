package org.nulang.ast;

import java.util.Arrays;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Term;

public class ListShiftTest extends ExecutorTest {

	@Returns("[\"du\"]")
	public Lambda main() {

		Term value = Builder.val(Arrays.asList("hallo", "du", "dort"));

		return Builder.decl("main", Builder.lshft(Builder.rshft(value, 1), 1));
	}
}
