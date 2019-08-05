package org.nulang.script;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class NuScriptEngineFactory implements ScriptEngineFactory {

	@Override
	public String getEngineName() {

		return "nu";
	}

	@Override
	public String getEngineVersion() {

		return "0.0.1";
	}

	@Override
	public List<String> getExtensions() {

		return Arrays.asList(".nu");
	}

	@Override
	public List<String> getMimeTypes() {

		return Collections.emptyList();
	}

	@Override
	public List<String> getNames() {

		return Arrays.asList("nu");
	}

	@Override
	public String getLanguageName() {

		return "nu";
	}

	@Override
	public String getLanguageVersion() {

		return "0.0.1";
	}

	@Override
	public Object getParameter(String key) {

		return null;
	}

	@Override
	public String getMethodCallSyntax(String obj, String m, String... args) {

		boolean hasArgs = args != null && args.length != 0;

		StringBuilder builder = new StringBuilder(m);
		builder.append('(');

		if (obj != null && !obj.isEmpty()) {
			builder.append(obj);

			if (hasArgs) {
				builder.append(", ");
			}
		}

		if (hasArgs) {
			for (int i = 0; i < args.length; ++i) {
				builder.append(args[i]);

				if (i != args.length - 1) {
					builder.append(", ");
				}
			}
		}

		builder.append(')');

		return builder.toString();
	}

	@Override
	public String getOutputStatement(String toDisplay) {

		return toDisplay;
	}

	@Override
	public String getProgram(String... statements) {

		StringBuilder builder = new StringBuilder();

		if (statements != null && statements.length != 0) {
			for (String statement : statements) {
				builder.append(statement);
				builder.append(System.lineSeparator());
			}
		}

		return builder.toString();
	}

	@Override
	public ScriptEngine getScriptEngine() {

		return new NuScriptEngine(this);
	}

}
