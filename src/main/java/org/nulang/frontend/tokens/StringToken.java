package org.nulang.frontend.tokens;

public class StringToken extends Token {

	public StringToken(String lexem) {

		super(lexem);
	}

	@Override
	public String toString() {

		return "StringToken [lexem=" + this.lexem + "]";
	}
}
