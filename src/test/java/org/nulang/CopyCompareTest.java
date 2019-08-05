package org.nulang;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.nulang.core.Builder;
import org.nulang.core.Comparator;
import org.nulang.core.Copier;
import org.nulang.core.NuException;
import org.nulang.core.terms.Term;

public class CopyCompareTest {

	@Test
	public void testAdditive() throws NuException {

		this.test(Builder.add(Builder.val(5), Builder.val(4)));
		this.test(Builder.sub(Builder.val(5), Builder.val(4)));
	}

	@Test
	public void testBooleanValue() throws NuException {

		this.test(Builder.val(true));
	}

	@Test
	public void testCall() throws NuException {

		this.test(Builder.call("f", Builder.val(3), Builder.val(2)));
	}

	@Test
	public void testCharacterValue() throws NuException {

		this.test(Builder.val('a'));
	}

	@Test
	public void testConcat() throws NuException {

		this.test(Builder.concat(Builder.val(5), Builder.val(4)));
	}

	@Test
	public void testConditional() throws NuException {

		this.test(Builder.cnd(Builder.val(true), Builder.ONE, Builder.ZERO));
	}

	@Test
	public void testDoubleValue() throws NuException {

		this.test(Builder.val(3.1415d));
	}

	@Test
	public void testIndex() throws NuException {

		this.test(Builder.idx(Builder.id("list"), Builder.ONE));
	}

	@Test
	public void testEquality() throws NuException {

		this.test(Builder.eq(Builder.val(5), Builder.val(5)));
		this.test(Builder.ne(Builder.val(5), Builder.val(5)));
	}

	@Test
	public void testIdentifier() throws NuException {

		this.test(Builder.id("id"));
	}

	@Test
	public void testLambda() throws NuException {

		this.test(Builder.decl("add", Builder.add(Builder.id("x"), Builder.id("y")), "x", "y"));
	}

	@Test
	public void testListValue() throws NuException {

		this.test(Builder.val(Arrays.asList(5, 3, "hallo")));
	}

	@Test
	public void testLongValue() throws NuException {

		this.test(Builder.val(1337));
	}

	@Test
	public void testModule() throws NuException {

		this.test(Builder.module("test", Builder.decl("main", Builder.val(true))));
	}

	@Test
	public void testMultiplicative() throws NuException {

		this.test(Builder.mul(Builder.val(5), Builder.val(5)));
		this.test(Builder.div(Builder.val(5), Builder.val(5)));
		this.test(Builder.mod(Builder.val(5), Builder.val(5)));
	}

	@Test
	public void testNegation() throws NuException {

		this.test(Builder.neg(Builder.val(true)));
	}

	@Test
	public void testRelational() throws NuException {

		this.test(Builder.lt(Builder.val(5), Builder.val(5)));
		this.test(Builder.gt(Builder.val(5), Builder.val(5)));
		this.test(Builder.le(Builder.val(5), Builder.val(5)));
		this.test(Builder.ge(Builder.val(5), Builder.val(5)));
	}

	@Test
	public void testShift() throws NuException {

		this.test(Builder.lshft(Builder.val("test"), Builder.val(2)));
		this.test(Builder.rshft(Builder.val("test"), Builder.val(2)));
	}

	private void test(Term original) throws NuException {

		Term copy = original.apply(Copier.INSTANCE);
		Assert.assertEquals(true, original.apply(new Comparator(copy)));
	}
}
