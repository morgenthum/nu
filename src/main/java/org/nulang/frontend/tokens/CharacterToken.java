package org.nulang.frontend.tokens;

public class CharacterToken extends Token {

	public CharacterToken(String lexem) {

		super(lexem);
	}

	@Override
	public String toString() {

		return "CharacterToken [lexem=" + this.lexem + "]";
	}
}
