package org.nulang.core;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NuException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(NuException.class);

	private static Properties properties = new Properties();
	static {
		try {
			properties.load(NuException.class.getResourceAsStream("/exceptions.properties"));
		} catch (IOException e) {
			LOGGER.error(e, e);
		}
	}

	private static String buildMessage(String key, Object... parameters) {

		String message = properties.getProperty(key);
		if (message == null) {
			return key;
		}

		return String.format(message, parameters);
	}

	public NuException(String key, Object... parameters) {

		super(buildMessage(key, parameters));
	}
}
