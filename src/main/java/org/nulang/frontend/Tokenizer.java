package org.nulang.frontend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nulang.frontend.tokens.CharacterToken;
import org.nulang.frontend.tokens.IdentifierToken;
import org.nulang.frontend.tokens.KeywordToken;
import org.nulang.frontend.tokens.NumberToken;
import org.nulang.frontend.tokens.OperatorToken;
import org.nulang.frontend.tokens.PunctuatorToken;
import org.nulang.frontend.tokens.StringToken;
import org.nulang.frontend.tokens.Token;

public class Tokenizer {

	private static final List<String> KEYWORDS = Arrays.asList("none", "true", "false");

	private String content;
	private int position;

	public Tokenizer(String content) {

		this.content = content;
	}

	public List<Token> allTokens() {

		List<Token> tokens = new ArrayList<>();

		Token token;
		while ((token = this.next()) != null) {
			tokens.add(token);
		}

		return tokens;
	}

	public Token next() {

		int start = this.skipWhitespace();

		if (Character.isJavaIdentifierStart(this.current())) {
			do {
				++this.position;
			} while (Character.isJavaIdentifierPart(this.current()));
			String identifier = this.content.substring(start, this.position);

			if (KEYWORDS.contains(identifier)) {
				return new KeywordToken(identifier);
			}

			return new IdentifierToken(identifier);
		}

		if (this.current() == '\'') {
			++this.position;
			while (this.current() != '\'') {
				++this.position;
			}
			return new CharacterToken(this.content.substring(start + 1, this.position++));
		}

		if (this.current() == '"') {
			++this.position;
			while (this.current() != '"') {
				++this.position;
			}
			return new StringToken(this.content.substring(start + 1, this.position++));
		}

		if (this.current() >= '0' && this.current() <= '9') {
			do {
				++this.position;
			} while (this.current() >= '0' && this.current() <= '9');
			if (this.current() == '.' && this.current(1) >= '0' && this.current(1) <= '9') {
				++this.position;
				do {
					++this.position;
				} while (this.current() >= '0' && this.current() <= '9');
			}
			return new NumberToken(this.content.substring(start, this.position));
		}

		switch (this.current()) {
		case '.':
		case '-':
		case '*':
		case '/':
		case '%': {
			return new OperatorToken(this.content.substring(start, ++this.position));
		}
		case '+': {
			if (this.current(1) == '+') {
				return new OperatorToken(this.content.substring(start, this.position += 2));
			}
			return new OperatorToken(this.content.substring(start, ++this.position));

		}
		case '=':
		case '!': {
			if (this.current(1) == '=') {
				return new OperatorToken(this.content.substring(start, this.position += 2));
			}
			return new OperatorToken(this.content.substring(start, ++this.position));
		}
		case '<': {
			if (this.current(1) == '<' || this.current(1) == '=') {
				return new OperatorToken(this.content.substring(start, this.position += 2));
			}
			return new OperatorToken(this.content.substring(start, ++this.position));
		}
		case '>': {
			if (this.current(1) == '>' || this.current(1) == '=') {
				return new OperatorToken(this.content.substring(start, this.position += 2));
			}
			return new OperatorToken(this.content.substring(start, ++this.position));
		}
		}

		switch (this.current()) {
		case '(':
		case ')':
		case '[':
		case ']':
		case '{':
		case '}':
		case '@':
		case '\\':
		case '%':
		case '?':
		case ':':
		case ',': {
			return new PunctuatorToken(this.content.substring(start, ++this.position));
		}
		}

		return null;
	}

	private int skipWhitespace() {

		boolean whitespace;

		do {
			whitespace = false;

			if (this.current() == '#') {
				whitespace = true;
				do {
					++this.position;
				} while (this.current() != '\n' && this.current() != Character.MAX_VALUE);
			} else if (Character.isWhitespace(this.current())) {
				whitespace = true;
				do {
					++this.position;
				} while (Character.isWhitespace(this.current()));
			}
		} while (whitespace);

		return this.position;
	}

	private char current() {

		return this.current(0);
	}

	private char current(int delta) {

		if (this.position + delta < this.content.length()) {
			return this.content.charAt(this.position + delta);
		}

		return Character.MAX_VALUE;
	}
}
