package org.nulang.frontend.tokens;

public class PunctuatorToken extends Token {

	public PunctuatorToken(String lexem) {

		super(lexem);
	}

	@Override
	public String toString() {

		return "PunctuatorToken [lexem=" + this.lexem + "]";
	}
}
