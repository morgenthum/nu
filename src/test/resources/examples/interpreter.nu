@test = [utils]

main = execute([5, 4, (+), 2, (*)]) == 18

execute(xs) = length(xs) >= 3 ? execute(xs.2(xs.0, xs.1) ++ (xs << 3)) : xs.0