package org.nulang.ast;

import static org.nulang.core.Builder.call;
import static org.nulang.core.Builder.cnd;
import static org.nulang.core.Builder.decl;
import static org.nulang.core.Builder.val;

import org.nulang.core.Builder;
import org.nulang.core.terms.Lambda;

public class DeadCodeEliminationTest extends ExecutorTest {

	@Returns("false")
	public Lambda main() {

		return decl("main", call("dead"));
	}

	public Lambda dead() {

		return decl("dead",
				cnd(Builder.eq(Builder.val(2), Builder.ONE), val(true), val(false)));
	}
}
