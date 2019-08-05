package org.nulang.frontend;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.BooleanValue;
import org.nulang.frontend.tokens.Token;
import org.nulang.interpreter.Interpreter;

public class TermsTest {

	private static final Logger LOGGER = LogManager.getLogger(TermsTest.class);

	@Test
	public void test() throws Exception {

		InputStream input = this.getClass().getResourceAsStream("/termstest.nu");
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		String line;
		while ((line = reader.readLine()) != null) {
			List<Token> tokens = new Tokenizer(line).allTokens();
			if (tokens.isEmpty()) {
				continue;
			}

			Parser parser = new Parser(tokens);
			Term term = parser.getTerm();

			Term result = term.apply(new Interpreter());
			if (result instanceof BooleanValue) {
				if (((BooleanValue) result).value) {
					LOGGER.info("correct result for: " + line);
				} else {
					Assert.fail("incorrect result for: " + line);
				}
			} else {
				Assert.fail("unchecked result for: " + line + " ---> " + result);
			}
		}
	}
}
