add(x, y) = x + y

listCall(f, []) = f
listCall(f, xs) = listCall(f(xs.0), xs << 1)

main = listCall(add, [5, 4]) == 9