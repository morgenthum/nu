@utils

add = (+)
sub = (-)
mul = (*)
div = (/)
mod = (%)
index = (.)
concat = (++)
equal = (==)
notEqual = (!=)
less = (<)
lessEqual = (<=)
greater = (>)
greaterEqual = (>=)
leftShift = (<<)
rightShift = (>>)
negate = (!)

identity(x) = x

inc(x) = x + 1
dec(x) = x - 1

length([]) = 0
length(xs) = 1 + length(xs << 1)

filter(f, []) = []
filter(f, xs) = (f(xs.0) ? xs.0 : []) ++ filter(f, xs << 1)

map(f, []) = []
map(f, xs) = f(xs.0) ++ map(f, xs << 1)

foldLeft(f, i, []) = i
foldLeft(f, i, xs) = foldLeft(f, f(i, xs.0), xs << 1)

foldRight(f, i, []) = i
foldRight(f, i, xs) = f(xs.0, foldRight(f, i, xs << 1))

contains(x, xs) = filter((i) = i == x, xs) != []
occurences(x, xs) = length(filter((i) = i == x, xs))
distinct(xs) = foldLeft((xs, x) = contains(x, xs) ? xs : xs ++ x, [], xs)

filterLesser(x) = filter((i) = i < x)
filterGreater(x) = filter((i) = i > x)

sum(xs) = foldLeft((+), 0, xs)

range(f, from, to) =
	from == to 
	? [from] 
	: from ++ range(f, f(from), to)