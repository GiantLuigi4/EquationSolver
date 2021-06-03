package tfc.equation_solver.values;

import tfc.equation_solver.EquationParser;

public abstract class Value {
	public abstract double get(EquationParser parser);
	
	public String toString(EquationParser parser) {
		return String.valueOf(get(parser));
	}
	
	@Override
	public String toString() {
		return toString(null);
	}
}
