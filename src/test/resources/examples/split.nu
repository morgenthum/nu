split_r([], delimiter, current, result) = 
	current == [] 
	? result 
	: result ++ [current]

split_r(values, delimiter, current, result) =
	values.0 == delimiter
	? split_r(values << 1, delimiter, [], result ++ [current])
	: split_r(values << 1, delimiter, current ++ values.0, result)

split(values, delimiter) = split_r(values, delimiter, [], [])

main = split("hallo du da", ' ') == ["hallo", "du", "da"]