package org.nulang.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.nulang.core.Builder;
import org.nulang.core.Intrinsic;
import org.nulang.core.NuException;
import org.nulang.core.terms.Call;
import org.nulang.core.terms.Identifier;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Term;
import org.nulang.core.terms.values.ListValue;
import org.nulang.core.terms.values.NoneValue;
import org.nulang.frontend.tokens.CharacterToken;
import org.nulang.frontend.tokens.IdentifierToken;
import org.nulang.frontend.tokens.KeywordToken;
import org.nulang.frontend.tokens.NumberToken;
import org.nulang.frontend.tokens.OperatorToken;
import org.nulang.frontend.tokens.StringToken;
import org.nulang.frontend.tokens.Token;

public class Parser {

	private List<Token> tokens;
	private int index;

	private Stack<Integer> state = new Stack<>();

	public Parser(List<Token> tokens) {

		this.tokens = tokens;
		this.index = 0;
	}

	public Module getModule() throws NuException {

		Module module = new Module();

		if (this.is(this.current(), "@")) {
			this.move();

			Lambda lambda = this.getLambda(false);
			if (lambda == null) {
				if (!this.is(this.current(), IdentifierToken.class)) {
					throw new NuException("parser.module.name");
				}

				module.name = this.current().lexem;
				this.move();
			} else {
				if (lambda.name == null) {
					throw new NuException("parser.module.name");
				}

				module.name = lambda.name;

				if (!(lambda.body instanceof ListValue)) {
					throw new NuException("parser.module.deps");
				}

				ListValue dependencies = (ListValue) lambda.body;

				for (Term term : dependencies.value) {
					if (!(term instanceof Identifier)) {
						throw new NuException("parser.module.dep");
					}

					Identifier dependency = (Identifier) term;
					module.dependencies.put(dependency.identifier, null);
				}

			}
		}

		Map<String, List<Lambda>> map = new HashMap<>();

		Lambda lambda;
		while ((lambda = this.getLambda(true)) != null) {
			List<Lambda> list = map.get(lambda.name);
			if (list == null) {
				list = new ArrayList<>();
				map.put(lambda.name, list);
			}

			list.add(lambda);
		}

		return PatternResolver.resolve(module, map);
	}

	private Lambda getLambda(boolean force) throws NuException {

		this.save();

		Lambda lambda = new Lambda();

		if (this.is(this.current(), IdentifierToken.class)) {
			lambda.name = this.current().lexem;
			this.move();
		}

		try {
			if (this.is(this.current(), "(")) {
				this.move();

				while (!this.is(this.current(), ")")) {
					lambda.parameters.add(this.getPrimary());

					if (this.is(this.current(), ",")) {
						this.move();
					} else if (!this.is(this.current(), ")")) {
						this.restore();
						return null;
					}
				}
				this.move();
			}

			if (this.is(this.current(), "=")) {
				this.move();

				lambda.body = this.getTerm();

				while (this.is(this.current(), "\\")) {
					this.move();

					if (!this.is(this.current(), "(")) {
						throw new NuException("parser.lambda.let.begin");
					}
					this.move();

					Lambda let = this.getLambda(true);

					if (!this.is(this.current(), ")")) {
						throw new NuException("parser.lambda.let.end");
					}
					this.move();

					lambda.addLambda(let);
				}

				this.accept();
				return lambda;
			}
		} catch (NuException e) {
			if (force) {
				throw e;
			}
		}

		int current = this.index;
		this.restore();

		if (force && current != this.index) {
			throw new NuException("parser.lambda");
		}

		return null;
	}

	public Term getTerm() throws NuException {

		Lambda lambda = this.getLambda(false);
		if (lambda != null) {
			return lambda;
		}

		return this.getConditional();
	}

	private Term getConditional() throws NuException {

		Term condition = this.getEquality();
		if (!this.is(this.current(), "?")) {
			return condition;
		}
		this.move();

		Term then = this.getTerm();
		if (!this.is(this.current(), ":")) {
			throw new NuException("parser.conditional.then");
		}
		this.move();

		Term otherwise = this.getTerm();

		return Builder.cnd(condition, then, otherwise);
	}

	private Term getEquality() throws NuException {

		Term lhs = this.getRelational();

		for (;;) {
			if (this.is(this.current(), "==")) {
				this.move();
				lhs = Builder.eq(lhs, this.getRelational());
			} else if (this.is(this.current(), "!=")) {
				this.move();
				lhs = Builder.ne(lhs, this.getRelational());
			} else {
				break;
			}
		}

		return lhs;
	}

	private Term getRelational() throws NuException {

		Term lhs = this.getConcat();

		for (;;) {
			if (this.is(this.current(), "<")) {
				this.move();
				lhs = Builder.lt(lhs, this.getConcat());
			} else if (this.is(this.current(), ">")) {
				this.move();
				lhs = Builder.gt(lhs, this.getConcat());
			} else if (this.is(this.current(), "<=")) {
				this.move();
				lhs = Builder.le(lhs, this.getConcat());
			} else if (this.is(this.current(), ">=")) {
				this.move();
				lhs = Builder.ge(lhs, this.getConcat());
			} else {
				break;
			}
		}

		return lhs;
	}

	private Term getConcat() throws NuException {

		Term lhs = this.getShift();

		while (this.is(this.current(), "++")) {
			this.move();
			lhs = Builder.concat(lhs, this.getShift());
		}

		return lhs;
	}

	private Term getShift() throws NuException {

		Term lhs = this.getAdditive();

		for (;;) {
			if (this.is(this.current(), "<<")) {
				this.move();
				lhs = Builder.lshft(lhs, this.getAdditive());
			} else if (this.is(this.current(), ">>")) {
				this.move();
				lhs = Builder.rshft(lhs, this.getAdditive());
			} else {
				break;
			}
		}

		return lhs;
	}

	private Term getAdditive() throws NuException {

		Term lhs = this.getMultiplicative();

		for (;;) {
			if (this.is(this.current(), "+")) {
				this.move();
				lhs = Builder.add(lhs, this.getMultiplicative());
			} else if (this.is(this.current(), "-")) {
				this.move();
				lhs = Builder.sub(lhs, this.getMultiplicative());
			} else {
				break;
			}
		}

		return lhs;
	}

	private Term getMultiplicative() throws NuException {

		Term lhs = this.getNegation();

		for (;;) {
			if (this.is(this.current(), "*")) {
				this.move();
				lhs = Builder.mul(lhs, this.getNegation());
			} else if (this.is(this.current(), "/")) {
				this.move();
				lhs = Builder.div(lhs, this.getNegation());
			} else if (this.is(this.current(), "%")) {
				this.move();
				lhs = Builder.mod(lhs, this.getNegation());
			} else {
				break;
			}
		}

		return lhs;
	}

	private Term getNegation() throws NuException {

		if (this.is(this.current(), "!")) {
			this.move();
			return Builder.neg(this.getNegation());
		}

		return this.getPostfix();
	}

	private Term getPostfix() throws NuException {

		Term postfix = this.getPrimary();

		for (;;) {
			if (this.is(this.current(), "(")) {
				this.move();

				List<Term> parameters = new ArrayList<>();

				while (!this.is(this.current(), ")")) {
					parameters.add(this.getTerm());

					if (this.is(this.current(), ",")) {
						this.move();
					} else if (!this.is(this.current(), ")")) {
						throw new NuException("parser.call");
					}
				}
				this.move();

				postfix = new Call(postfix, parameters);
			} else if (this.is(this.current(), ".")) {
				this.move();
				postfix = Builder.idx(postfix, this.getPrimary());
			} else {
				break;
			}
		}

		return postfix;
	}

	private Term getPrimary() throws NuException {

		if (this.is(this.current(), IdentifierToken.class)) {
			return this.moveAfter(new Identifier(this.current().lexem));
		}

		if (this.is(this.current(), KeywordToken.class)) {
			if (this.is(this.current(), "none")) {
				return this.moveAfter(NoneValue.INSTANCE);
			}

			return this.getBooleanValue();
		}

		if (this.is(this.current(), CharacterToken.class)) {
			return this.getCharacterValue();
		}

		if (this.is(this.current(), StringToken.class)) {
			return this.getStringValue();
		}

		if (this.is(this.current(), "[")) {
			return this.getListValue();
		}

		if (this.is(this.current(), "{")) {
			return this.getMapValue();
		}

		if (this.is(this.current(), NumberToken.class)) {
			return this.getNumberValue();
		}

		if (this.is(this.current(), "(")) {
			this.move();

			Term term;
			if (this.is(this.current(), OperatorToken.class)) {
				term = this.getPrefixOperator();
				if (term != null) {
					return term;
				}
			}

			term = this.getTerm();

			if (!this.is(this.current(), ")")) {
				throw new NuException("parser.parenthesis");
			}
			this.move();

			return term;
		}

		throw new NuException("parser.term");
	}

	private Term getPrefixOperator() throws NuException {

		if (!this.is(this.current(), OperatorToken.class)) {
			return null;
		}

		this.save();

		OperatorToken token = (OperatorToken) this.current();
		this.move();

		if (!this.is(this.current(), ")")) {
			this.restore();
			return null;
		}

		this.accept();
		this.move();

		Term term = Intrinsic.OPERATORS.get(token.lexem);
		if (term == null) {
			throw new NuException("parser.operator", token.lexem);
		}

		return term;
	}

	private Term getBooleanValue() {

		return this.moveAfter(Builder.val(Boolean.valueOf(this.current().lexem)));
	}

	private Term getListValue() throws NuException {

		if (!this.is(this.current(), "[")) {
			throw new NuException("parser.list.begin");
		}
		this.move();

		List<Term> list = new ArrayList<>();

		while (!this.is(this.current(), "]")) {
			list.add(this.getTerm());

			if (this.is(this.current(), ",")) {
				this.move();
			} else if (!this.is(this.current(), "]")) {
				throw new NuException("parser.list.end");
			}
		}
		this.move();

		return Builder.val(list);
	}

	private Term getMapValue() throws NuException {

		if (!this.is(this.current(), "{")) {
			throw new NuException("parser.map.begin");
		}
		this.move();

		Map<Term, Term> map = new HashMap<>();

		while (!this.is(this.current(), "}")) {
			Term key = this.getTerm();
			if (!this.is(this.current(), ":")) {
				throw new NuException("parser.map.pair");
			}
			this.move();
			Term value = this.getTerm();
			map.put(key, value);

			if (this.is(this.current(), ",")) {
				this.move();
			} else if (!this.is(this.current(), "}")) {
				throw new NuException("parser.map.end");
			}
		}
		this.move();

		return Builder.val(map);
	}

	private Term getCharacterValue() throws NuException {

		if (!this.is(this.current(), CharacterToken.class)) {
			throw new NuException("parser.char.begin");
		}

		return this.moveAfter(Builder.val(this.current().lexem.charAt(0)));
	}

	private Term getStringValue() throws NuException {

		if (!this.is(this.current(), StringToken.class)) {
			throw new NuException("parser.string.begin");
		}

		return this.moveAfter(Builder.val(this.current().lexem));
	}

	private Term getNumberValue() {

		String lexem = this.current().lexem;

		if (lexem.indexOf('.') != -1) {
			return this.moveAfter(Builder.val(new Double(lexem)));
		} else {
			return this.moveAfter(Builder.val(new Long(lexem)));
		}
	}

	private boolean is(Token token, Class<?> clazz) {

		return token == null ? false : clazz.isInstance(token);
	}

	private boolean is(Token token, String lexem) {

		return token == null ? false : lexem.equals(token.lexem);
	}

	private Token current() {

		if (this.index < this.tokens.size()) {
			return this.tokens.get(this.index);
		}

		return null;
	}

	private Term moveAfter(Term term) {

		this.move();
		return term;
	}

	private void move() {

		++this.index;
	}

	private void save() {

		this.state.push(this.index);
	}

	private void accept() {

		this.state.pop();
	}

	private void restore() {

		this.index = this.state.pop();
	}
}
