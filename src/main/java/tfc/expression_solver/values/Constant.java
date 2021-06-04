package tfc.expression_solver.values;

import tfc.expression_solver.ExpressionParser;

public class Constant extends Value {
	private final double constant;
	
	public Constant(double val) {
		constant = val;
	}
	
	public double get(ExpressionParser parser) {
		return constant;
	}
}
