sort([]) = []
sort(values) = sort_r(values.0, values << 1, [])

sort_r(min, [], result) = min ++ sort(result)
sort_r(min, values, result) = 
	values.0 < min 
	? sort_r(values.0, values << 1, result ++ min)
	: sort_r(min, values << 1, result ++ values.0)

main = sort([3, 1, 5, 9, 7]) == [1, 3, 5, 7, 9]