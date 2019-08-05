package org.nulang.frontend.tokens;

public class OperatorToken extends Token {

	public OperatorToken(String lexem) {

		super(lexem);
	}

	@Override
	public String toString() {

		return "OperatorToken [lexem=" + this.lexem + "]";
	}
}
