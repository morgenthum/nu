package org.nulang;

public class Fibonacci {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println(fib(40));
		long time = System.currentTimeMillis() - start;
		System.out.println(time + " ms");
	}

	private static int fib(int n) {

		if (n == 1 || n == 2) {
			return 1;
		}

		return fib(n - 1) + fib(n - 2);
	}
}
