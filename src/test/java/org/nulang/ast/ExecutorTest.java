package org.nulang.ast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.nulang.TestConstants;
import org.nulang.core.NuException;
import org.nulang.core.Stringifier;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Term;
import org.nulang.interpreter.Interpreter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class ExecutorTest {

	private static final Logger LOGGER = LogManager.getLogger(ExecutorTest.class);

	private Module module = new Module();

	@Before
	public void addExpressions() {

		for (Method method : this.getClass().getDeclaredMethods()) {
			if (Lambda.class.equals(method.getReturnType())) {
				try {
					Lambda lambda = (Lambda) method.invoke(this);
					this.module.lambdas.put(lambda.name, lambda);
				} catch (Exception e) {
					LOGGER.error(e, e);
				}
			}
		}
	}

	@After
	public void newLine() {

		LOGGER.info("");
	}

	@Test
	public void test1Execute() throws NuException {

		LOGGER.info(this.title());

		long start = System.currentTimeMillis();

		Term term = this.module.apply(new Interpreter(this.module));
		String result = term.apply(Stringifier.INSTANCE);
		LOGGER.info(result);

		long end = System.currentTimeMillis();
		LOGGER.info("execution took " + (end - start) + " ms");

		try {
			Method mainMethod = this.getClass().getDeclaredMethod("main");
			Returns annotation = mainMethod.getAnnotation(Returns.class);
			if (annotation != null) {
				Assert.assertEquals(annotation.value(), result);
			}
		} catch (Exception e) {
			LOGGER.error(e, e);
		}
	}

	@Test
	public void test2Print() throws NuException {

		LOGGER.info(this.title());

		String stringified = this.module.apply(Stringifier.INSTANCE);
		LOGGER.info(stringified);

		String prefix = this.getClass().getPackage().getName().replace('.', '_');
		String className = this.getClass().getSimpleName();
		String suffix = ".nu";
		String fileName = prefix + "_" + className + suffix;
		String filePath = TestConstants.FOLDER_GENERATED.getPath() + File.separator + fileName;

		this.writeTestSource(filePath, stringified);
	}

	private String title() {

		String title = "> [" + Thread.currentThread().getStackTrace()[2].getMethodName() + "] <---";

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 80 - title.length(); ++i) {
			builder.append('-');
		}
		builder.append(title);

		return builder.toString();
	}

	private void writeTestSource(String filePath, String content) {

		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			LOGGER.error(e, e);
		}
	}
}
