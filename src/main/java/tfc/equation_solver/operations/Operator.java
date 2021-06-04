package tfc.equation_solver.operations;

import tfc.equation_solver.Expression;
import tfc.equation_solver.values.Value;

public abstract class Operator {
	public abstract double apply(Expression expression, Value left, Value right);
}
