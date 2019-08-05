package org.nulang.frontend;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.nulang.TestConstants;
import org.nulang.ast.Returns;
import org.nulang.core.NuException;
import org.nulang.core.Stringifier;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Term;
import org.nulang.frontend.tokens.Token;
import org.nulang.interpreter.Interpreter;

public class GeneratedSourcesTest {

	private static final Logger LOGGER = LogManager.getLogger(GeneratedSourcesTest.class);

	@Test
	public void test() throws NuException {

		boolean valid = true;

		for (File file : TestConstants.FOLDER_GENERATED.listFiles()) {
			if (!this.testFile(file)) {
				valid = false;
			}
		}

		if (!valid) {
			Assert.fail("failed to execute files");
		}
	}

	private boolean testFile(File file) {

		String result = null;

		try {
			String content = IOUtils.toString(new FileReader(file));
			List<Token> tokens = new Tokenizer(content).allTokens();

			Parser parser = new Parser(tokens);
			Module module = parser.getModule();

			Term term = module.apply(new Interpreter(module));
			result = term.apply(Stringifier.INSTANCE);

			if (this.resultValid(file, result)) {
				return true;
			}
		} catch (Throwable e) {
			result = e.getMessage();
		}

		LOGGER.error("incorrect result for " + file.getName() + ": " + result);
		return false;
	}

	private boolean resultValid(File file, String result) {

		try {
			Class<?> clazz = this.getClass(file);
			if (clazz != null) {
				Method method = clazz.getMethod("main");
				Returns returns = method.getAnnotation(Returns.class);
				if (returns != null) {
					if (returns.value().equals(result)) {
						LOGGER.info("correct result for " + file.getName() + ": " + result);
						return true;
					} else {
						LOGGER.error("incorrect result for " + file.getName() + ": " + result);
						return false;
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error(e, e);
		}

		LOGGER.warn("unchecked result for " + file.getName() + ": " + result);
		return true;
	}

	private Class<?> getClass(File file) {

		String className = file.getName().replace('_', '.');
		className = className.substring(0, file.getName().length() - 3);

		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
		}

		return null;
	}
}
