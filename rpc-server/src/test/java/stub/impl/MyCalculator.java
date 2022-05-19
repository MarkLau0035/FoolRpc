package stub.impl;


import stub.Calculator;

/**
 * @description TODO
 * @Author luolinyuan
 * @date 2022/4/11
 **/
public class MyCalculator implements Calculator {
	@Override
	public int add(int a, int b) {
		return a + b;
	}

	@Override
	public int minus(int a, int b) {
		return a - b;
	}

	@Override
	public double add(double a, double b) {
		return a + b;
	}


}
