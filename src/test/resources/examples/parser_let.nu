isPlus(value) = value == [] ? false : value.0 == '+'

expr(value) = plus ? lhs + rhs : lhs
	\ (lhs = value.0)
	\ (plus = isPlus(value << 1))
	\ (rhs = expr(value << 2))

main = expr([5, '+', 7, '+', 10]) == 22