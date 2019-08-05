@test = [utils]

simpleRange(first) = range(inc, first)

main = simpleRange(0, 2) == [0, 1, 2]
