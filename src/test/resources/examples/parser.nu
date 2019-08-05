@test = [utils]

main = expr(convertState(tokenize(tokenStream))).result == 1337

# This is the data-structure the tokenizer uses.
# See the index explanation below.
tokenStream = ["670+670-3", [], []]

# The result from the tokenizer is the input for the parser.
convertState(ts) = [ts.result, 0, 0]

### Generic ####################################################################

# Will be removed soon.
# Just forgot to implement a logical-and-operator (logical-or too). :D
and(a, b) = a ? b : false

isEmpty(s) = s.input == []
isDigit(s) = isEmpty(s) ? false : and(s.input.0 >= '0', s.input.0 <= '9')

### Tokenizer ##################################################################

# These are constants to access the token-stream.
# Index 0: The plain expression as string.
# Index 1: The resulting list.
# Index 2: Temp element to build up a token.
input = 0
result = 1
temp = 2

# Tokenizes a string.
# Example input: ["670+670-3", [], []]
# Example output: [[], [670, "+", 670, "-", 3], []]
tokenize(ts) =
	isEmpty(ts)
	? ts
	: tokenize(appendResult(
		isDigit(ts)
		? tempAsNumber(number(ts)) 
		: appendTemp(ts)
	))

# Consumes input until there are no more leading digits.
number(ts) = isDigit(ts) ? number(appendTemp(ts)) : ts

# Appends one single character to temp.
appendTemp(ts) = [ts.input << 1, ts.result, ts.temp ++ ts.input.0]

# Appends temp as new element to the result.
appendResult(ts) = [ts.input, ts.result + [ts.temp], []]

# The string in temp gets converted to a number.
# Calling java function - convention not final yet (underscores suck).
tempAsNumber(ts) = [ts.input, ts.result, java_lang_Long_parseLong(ts.temp)]

### Parser #####################################################################

# Implementation of https://www.hackerrank.com/challenges/expressions-v2
# This parser just implements add- and sub-operations on numbers (no parentheses).

# Parses the tokens.
# Example input: [[670, "+", 670, "-", 3], 0, 0]
# Example output: [[], 1337, 0]
expr(s) =
	isEmpty(s)
	? s
	: exprTerm(factor(s))

exprTerm(s) = 
	isEmpty(s)
	? s 
	: isPlus(s)
		? add(expr(add(skip(s))))
		: isMinus(s)
			? sub(expr(add(skip(s))))
			: s

factor(s) = store(s)

add(s) = [s.input, s.result + s.temp, 0]
sub(s) = [s.input, s.result - s.temp, 0]
store(s) = [s.input << 1, s.result, s.input.0]
skip(s) = [s.input << 1, s.result, s.temp]

isPlus(s) = s.input.0 == "+"
isMinus(s) = s.input.0 == "-"
