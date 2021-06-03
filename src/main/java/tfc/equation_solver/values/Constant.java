package tfc.equation_solver.values;

import tfc.equation_solver.EquationParser;

public class Constant extends Value {
	private final double constant;
	
	public Constant(double val) {
		constant = val;
	}
	
	public double get(EquationParser parser) {
		return constant;
	}
}
