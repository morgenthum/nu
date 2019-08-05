@test = [utils]

qsort([]) = []
qsort(vs) = 
	qsort(filterLesser(vs.0)(vs << 1)) ++ 
	vs.0 ++
	qsort(filterGreater(vs.0)(vs << 1))

main = qsort([5, 3, 9, 1, 2]) == [1, 2, 3, 5, 9]