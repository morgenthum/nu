package org.nulang.frontend;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.nulang.TestConstants;
import org.nulang.core.NuException;
import org.nulang.core.Stringifier;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.BooleanValue;
import org.nulang.frontend.tokens.Token;
import org.nulang.interpreter.Interpreter;

public class ExamplesTest {

	private static final Logger LOGGER = LogManager.getLogger(ExamplesTest.class);

	@Test
	public void test() throws NuException {

		boolean valid = true;

		for (File file : TestConstants.FOLDER_EXAMPLES.listFiles()) {
			if (file.isFile() && file.getName().endsWith("combinator.nu")) {
				if (!this.testFile(file)) {
					valid = false;
				}
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

			long start = System.currentTimeMillis();

			Term term = module.apply(new Interpreter(module));
			result = term.apply(Stringifier.INSTANCE);

			long time = (System.currentTimeMillis() - start);
			String info = file.getName() + " (" + time + " ms): " + result;

			if (term instanceof BooleanValue && ((BooleanValue) term).value) {
				LOGGER.info("correct result for " + info);
				return true;
			}

			LOGGER.info("incorrect result for " + info);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return false;
	}
}
