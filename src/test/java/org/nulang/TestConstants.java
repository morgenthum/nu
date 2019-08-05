package org.nulang;

import java.io.File;

public class TestConstants {

	public static final File FOLDER_EXAMPLES = new File("src/test/resources/examples");
	public static final File FOLDER_GENERATED = new File("target/generated-test-sources/nu");

	static {
		FOLDER_GENERATED.mkdirs();
	}
}
