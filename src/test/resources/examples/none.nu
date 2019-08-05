@test = [utils]

replace(value, with) = (x) = x == value ? with : x

main = (==)(map(replace(none, 3), [1, none, none, 7]), [1, 3, 3, 7])
