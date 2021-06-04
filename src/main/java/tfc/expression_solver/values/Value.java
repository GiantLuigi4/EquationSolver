package tfc.expression_solver.values;

import tfc.expression_solver.ExpressionParser;

public abstract class Value {
	public abstract double get(ExpressionParser parser);
	
	public String toString(ExpressionParser parser) {
		return String.valueOf(get(parser));
	}
	
	@Override
	public String toString() {
		return toString(null);
	}
}
