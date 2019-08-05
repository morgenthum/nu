### Application ###############################################################

main = success(parser(stream("1")))

parser = one

zero = literal('0')
one = literal('1')


### Parser library #############################################################

stream(s) = [false, s, []]

literal(c) = 
	(s) = input(s).0 == c ? move(s) : fail

or(p1, p2) = 
	(s) = success(p1(s)) ? p1(s) : success(p2(s)) ? p2(s) : fail
	
optional(p) =
	(s) = success(p(s)) ? p(s) : [true, input(s), output(s)]

and(p1, p2) =
	(s) = success(p1(s)) ? success(p2(s)) ? p2(s) : fail : fail
	
andThen(p1, p2) =
	(s) = success(p1(s)) ? success(p2(p1(s))) ? p2(p1(s)) : fail : fail	


### Result methods #############################################################

move(s) = [true, input(s) << 1, output(s) ++ input(s).0]	
fail = [false, [], []]


### Extract fields from stream #################################################

success(s) = s.0
input(s) = s.1
output(s) = s.2
