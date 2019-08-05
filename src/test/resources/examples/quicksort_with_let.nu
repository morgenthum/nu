@test = [utils]

qsort([]) = []
qsort(vs) = qsort(lower) ++ vs.0 ++ qsort(upper)
	\ (lower = filterLesser(vs.0)(vs << 1))
	\ (upper = filterGreater(vs.0)(vs << 1))

main = qsort([5, 3, 9, 1, 2]) == [1, 2, 3, 5, 9]