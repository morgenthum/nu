package org.nulang.frontend;

import java.util.List;
import java.util.Map;

import org.nulang.core.Builder;
import org.nulang.core.NuException;
import org.nulang.core.terms.Identifier;
import org.nulang.core.terms.Lambda;
import org.nulang.core.terms.Module;
import org.nulang.core.terms.Term;

public class PatternResolver {

	public static Module resolve(Module module, Map<String, List<Lambda>> map) throws NuException {

		for (List<Lambda> list : map.values()) {
			merge(module, list);
		}

		return module;
	}

	private static void merge(Module module, List<Lambda> lambdas) throws NuException {

		Lambda mainLambda = mainLambda(lambdas);
		if (mainLambda == null) {
			throw new NuException("pattern.default.missing", lambdas.get(0).name);
		}

		for (Lambda lambda : lambdas) {
			if (lambda == mainLambda) {
				continue;
			}

			for (int i = 0; i < lambda.parameters.size(); ++i) {
				Term pattern = lambda.parameters.get(i);

				if (!(pattern instanceof Identifier)) {
					Term parameter = mainLambda.parameters.get(i);

					extend(mainLambda, parameter, pattern, lambda.body);
				}
			}
		}

		module.lambdas.put(mainLambda.name, mainLambda);
	}

	private static Lambda mainLambda(List<Lambda> lambdas) {

		outer: for (Lambda lambda : lambdas) {
			for (Term parameter : lambda.parameters) {
				if (!(parameter instanceof Identifier)) {
					continue outer;
				}
			}
			return lambda;
		}

		return null;
	}

	private static void extend(Lambda mainLambda, Term parameter, Term pattern, Term body) {

		Term condition = Builder.eq(parameter, pattern);
		Term then = body;
		Term otherwise = mainLambda.body;

		mainLambda.body = Builder.cnd(condition, then, otherwise);
	}
}
