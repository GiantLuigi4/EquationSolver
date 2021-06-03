package tfc.equation_solver.values;

import tfc.equation_solver.EquationParser;

public abstract class Value {
	public abstract double get(EquationParser parser);
}
