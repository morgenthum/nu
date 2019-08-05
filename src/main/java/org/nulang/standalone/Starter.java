package org.nulang.standalone;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.nulang.core.NuException;
import org.nulang.core.Stringifier;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Term;
import org.nulang.frontend.Parser;
import org.nulang.frontend.Tokenizer;
import org.nulang.frontend.tokens.Token;
import org.nulang.interpreter.Interpreter;

public class Starter {

	public static void main(String[] args) {

		Options options = parseArguments(args);

		if (options != null) {
			try {
				perform(options);
			} catch (Exception e) {
				System.err.println("execution failed: " + e.getMessage());
			}
		}
	}

	private static Options parseArguments(String[] args) {

		Options options = new Options();

		for (int i = 0; i < args.length; ++i) {
			String arg = args[i];

			if (arg.startsWith("-D")) {
				String pair = arg.substring(2);
				int index = pair.indexOf('=');
				String key = pair.substring(0, index);
				String value = pair.substring(index + 1);
				System.setProperty(key, value);
				continue;
			}

			if ("-i".equals(arg)) {
				options.mode = Options.ExecutionMode.INTERPRET;
				continue;
			}

			options.fileName = arg;
		}

		// Default values
		if (options.mode == null) {
			options.mode = Options.ExecutionMode.INTERPRET;
		}

		return options;
	}

	private static void perform(Options options) throws IOException, NuException {

		if (options.mode == Options.ExecutionMode.INTERPRET) {
			String content = IOUtils.toString(new FileReader(options.fileName));
			List<Token> tokens = new Tokenizer(content).allTokens();

			Parser parser = new Parser(tokens);
			Module module = parser.getModule();

			Term term = module.apply(new Interpreter(module));
			System.out.println(term.apply(Stringifier.INSTANCE));
		}
	}
}
