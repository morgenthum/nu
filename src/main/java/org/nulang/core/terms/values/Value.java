package org.nulang.core.terms.values;

import java.util.Arrays;

import org.nulang.core.NuException;
import org.nulang.core.ValueVisitor;
import org.nulang.core.terms.Term;

public abstract class Value<T> extends Term {

	public T value;

	public Value(T value) {
		this.value = value;
	}

	public <TT> TT apply(ValueVisitor<TT> visitor) throws NuException {

		return visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.value == null) ? 0 : valueHashCode());
		return result;
	}

	private int valueHashCode() {

		if (this.value.getClass().isArray()) {
			return Arrays.hashCode((Object[]) this.value);
		}

		return this.value.hashCode();
	}
}
