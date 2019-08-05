package org.nulang.core;

import org.nulang.core.terms.Lambda;

public class JavaMethod extends Lambda {

	public Class<?> clazz;
	public String methodName;

	public JavaMethod(Class<?> clazz, String methodName) {

		this.clazz = clazz;
		this.methodName = methodName;
	}
}
