package org.nulang.interpreter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.nulang.core.Builder;
import org.nulang.core.JavaMethod;
import org.nulang.core.JavaValueVisitor;
import org.nulang.core.NuException;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.Value;

public class JavaCaller {

	public static Term resolve(String elementName) {

		int index = elementName.lastIndexOf('.');
		if (index == -1) {
			return null;
		}

		String javaClassName = elementName.substring(0, index);
		String javaElementName = elementName.substring(index + 1);

		Term term = null;

		try {
			Class<?> clazz = Class.forName(javaClassName);

			if (!"new".equals(javaElementName)) {
				term = resolveField(clazz, javaElementName);
				if (term != null) {
					return term;
				}
			}

			term = resolveMethod(clazz, javaElementName);
		} catch (ClassNotFoundException e) {
		}

		return term;
	}

	private static Term resolveField(Class<?> clazz, String javaElementName) {

		try {
			Field field = clazz.getField(javaElementName);
			Object object = field.get(null);

			return Builder.val(object);
		} catch (Exception e) {
		}

		return null;
	}

	private static Term resolveMethod(Class<?> clazz, String javaMethodName) {

		return Builder.decl(clazz, javaMethodName);
	}

	public static Term call(JavaMethod method, List<Term> parameters) throws NuException {

		Object[] parameterValues = getParameters(parameters);
		Class<?>[] parameterTypes = getParameterTypes(parameterValues);

		try {
			if (method.methodName.equals("new")) {
				Constructor<?> ctor = getMatchingConstructor(method.clazz, parameterTypes);
				if (ctor != null) {
					return Builder.val(ctor.newInstance(parameterValues));
				}
			} else {
				Method meth = getMatchingMethod(method, parameterTypes);

				if (Modifier.isStatic(meth.getModifiers())) {
					Object object = meth.invoke(null, parameterValues);
					return Builder.val(object);
				} else {
					Object[] subParameters = new Object[parameterValues.length - 1];
					System.arraycopy(parameterValues, 1, subParameters, 0, subParameters.length);

					Object object = meth.invoke(parameterValues[0], subParameters);
					return Builder.val(object);
				}
			}
		} catch (Exception e) {
		}

		return null;
	}

	private static Object[] getParameters(List<Term> parameters) throws NuException {

		Object[] objects = new Object[parameters.size()];

		for (int i = 0; i < objects.length; ++i) {
			Term term = parameters.get(i);
			if (!(term instanceof Value)) {
				return null;
			}

			Value<?> value = (Value<?>) term;

			objects[i] = value.apply(new JavaValueVisitor());
		}

		return objects;
	}

	private static Class<?>[] getParameterTypes(Object[] parameters) {

		Class<?>[] types = new Class<?>[parameters.length];

		for (int i = 0; i < types.length; ++i) {
			Object parameter = parameters[i];

			if (parameter instanceof Integer) {
				types[i] = int.class;
			} else {
				types[i] = parameter.getClass();
			}
		}

		return types;
	}

	private static Constructor<?> getMatchingConstructor(Class<?> clazz, Class<?>[] types) {

		out: for (Constructor<?> ctor : clazz.getConstructors()) {
			Class<?>[] ctorTypes = ctor.getParameterTypes();

			if (ctorTypes.length != types.length) {
				continue;
			}

			for (int i = 0; i < types.length; ++i) {
				if (!matches(ctorTypes[i], types[i])) {
					continue out;
				}
			}

			return ctor;
		}

		return null;
	}

	private static Method getMatchingMethod(JavaMethod javaMethod, Class<?>[] types) {

		out: for (Method method : javaMethod.clazz.getMethods()) {

			if (!method.getName().equals(javaMethod.methodName)) {
				continue;
			}

			Class<?>[] methodTypes = method.getParameterTypes();

			boolean isStatic = Modifier.isStatic(method.getModifiers());

			if (methodTypes.length != (isStatic ? types.length : types.length - 1)) {
				continue;
			}

			for (int i = 0; i < methodTypes.length; ++i) {
				if (!matches(methodTypes[i], types[isStatic ? i : i + 1])) {
					continue out;
				}
			}

			return method;
		}

		return null;
	}

	private static boolean matches(Class<?> expected, Class<?> actual) {

		if (expected == long.class) {
			expected = Long.class;
		} else if (expected == double.class) {
			expected = Double.class;
		}

		return expected.isAssignableFrom(actual);
	}
}
