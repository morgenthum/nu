package org.nulang.frontend.tokens;

public class NumberToken extends Token {

	public NumberToken(String lexem) {

		super(lexem);
	}

	@Override
	public String toString() {

		return "NumericToken [lexem=" + this.lexem + "]";
	}
}
