call(f) = f(5)

foo(x) = call(callback)
	\ (callback(y) = y + x)
	
main = foo(4) == 9