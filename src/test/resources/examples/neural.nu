# Implementation of a neural network
# see https://stevenmiller888.github.io/mind-how-to-build-a-neural-network/

@test = [utils, math]

neuron(value, weights) = [0.0, value, weights]

inputs = [ neuron(1.0, [0.8, 0.4, 0.3]),
		   neuron(1.0, [0.2, 0.9, 0.5]) ]
		   
hiddens = [ neuron(0.0, [0.3]),
			neuron(0.0, [0.5]),
			neuron(0.0, [0.9]) ]
			
outputs = [ neuron(0.0, []) ]

layers = [ inputs, hiddens, outputs ]

main = true #applyLayers(layers.0, layers << 1)

applyLayers(current, []) = [current]
applyLayers(current, remaining) = [current] ++ recurse
	\ (applied = applyLayer(current, remaining.0))
	\ (recurse = applyLayers(applied, remaining << 1))

applyLayer([], tos) = tos
applyLayer(from, tos) = applyLayer(from << 1, applyInput(from.0, tos, 0))

applyInput(from, [], i) = []
applyInput(from, tos, i) = [applied] ++ applyInput(from, tos << 1, i + 1)
	\ (applied = activate(sigmoid, fire(from, tos.0, i)))

fire(from, to, i) = [to.0 + from.1 * from.2.i, 0.0, to.2]

activate(f, neuron) = [neuron.0, f(neuron.0), neuron.2]

results(neurons) = map(result, neurons)
result(neuron) = neuron.1

errorMargins(expected, neurons) = map(errorMargin(expected), neurons)
errorMargin(expected, neuron) = expected - result(neuron)
