package org.nulang.frontend.tokens;

public class IdentifierToken extends Token {

	public IdentifierToken(String lexem) {

		super(lexem);
	}

	@Override
	public String toString() {

		return "IdentifierToken [lexem=" + this.lexem + "]";
	}
}
