package org.nulang.standalone;

public class Options {

	public enum ExecutionMode {
		COMPILE, INTERPRET
	}

	public ExecutionMode mode;

	public String fileName;
}
