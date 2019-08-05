# nu

Dynamically typed functional programming language

## Why another functional programming language?

I wrote the specification and implementation of the language in late 2016 for educational reasons.

## Examples

Take a look at [src/test/resources](src/test/resources)

Here a quicksort example implemented in nu:

```
@test = [utils]

qsort([]) = []
qsort(vs) = qsort(lower) ++ vs.0 ++ qsort(upper)
	\ (lower = filterLesser(vs.0)(vs << 1))
	\ (upper = filterGreater(vs.0)(vs << 1))

main = qsort([5, 3, 9, 1, 2]) == [1, 2, 3, 5, 9]
```
