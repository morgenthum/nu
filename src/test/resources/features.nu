# Parameter pattern matching
fib(<=2, m) = 1
fib(n, m) = fib(n - 1) + fib(n - 2)

# Switch case with pattern matching
word(n) =
	n | 1 	 ? "one"
		2	 ? "two"
		>= 3 ? "greater equals three"