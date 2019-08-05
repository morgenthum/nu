package org.nulang.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.nulang.core.terms.Module;
import org.nulang.frontend.Parser;
import org.nulang.frontend.Tokenizer;
import org.nulang.frontend.tokens.Token;

public class Loader {

	public static Module loadModule(String name) throws NuException {

		try (Reader reader = lookup(name)) {
			String content = IOUtils.toString(reader);
			List<Token> tokens = new Tokenizer(content).allTokens();

			Parser parser = new Parser(tokens);
			return parser.getModule();
		} catch (IOException e) {
			throw new NuException("module.find", name);
		}
	}

	private static Reader lookup(String name) throws NuException, IOException {

		// Classpath
		InputStream input = Loader.class.getResourceAsStream("/library/" + name + ".nu");
		if (input != null) {
			return new InputStreamReader(input);
		}

		// Working directory
		File file = new File("library/" + name + ".nu");
		if (file.exists()) {
			return new FileReader(file);
		}

		// Runtime system properties / operating system environment
		String home = System.getProperty(CoreConstants.HOME_VARIABLE);
		if (home == null || home.isEmpty()) {
			home = System.getenv(CoreConstants.HOME_VARIABLE);
		}
		if (home != null) {
			String path = home + "/library/" + name + ".nu";
			file = new File(path);
			if (file.exists()) {
				return new FileReader(file);
			}
		}

		throw new NuException("module.find", name);
	}
}
