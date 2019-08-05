package org.nulang.frontend.tokens;

public class KeywordToken extends Token {

	public KeywordToken(String lexem) {

		super(lexem);
	}

	@Override
	public String toString() {

		return "KeywordToken [lexem=" + this.lexem + "]";
	}
}
