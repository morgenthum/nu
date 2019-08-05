package org.nulang;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.math.BigDecimal;

import org.junit.Test;

public class InvokeDynamicDemo {

	@Test
	public void test() throws Throwable {

		MethodHandles.Lookup lookup = MethodHandles.lookup();

		CallSite site = bootstrapMethod(lookup, "valueOf", MethodType.methodType(String.class, BigDecimal.class));

		Object result = site.dynamicInvoker().invoke(new BigDecimal("5"));

		System.out.println(result);
	}

	public static CallSite bootstrapMethod(MethodHandles.Lookup callerClass, String methodName, MethodType methodType)
			throws Throwable {

		MethodHandle mh = callerClass.findStatic(String.class, methodName,
				MethodType.methodType(String.class, Object.class));

		return new ConstantCallSite(mh);
	}
}
