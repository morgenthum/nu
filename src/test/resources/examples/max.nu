qsort(vs) = vs == [] ? [] : qsort(filter((v) = v < (vs.0), vs << 1)) ++ vs.0 ++ qsort(filter((v) = v > (vs.0))(vs << 1))
	\ (filter(f, vs) = vs == [] ? [] : (f(vs.0) ? vs.0 : []) ++ filter(f, vs << 1))

main = qsort([5, 3, 9, 1, 2]) == [1, 2, 3, 5, 9]