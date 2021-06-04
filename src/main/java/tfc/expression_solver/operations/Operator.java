package tfc.expression_solver.operations;

import tfc.expression_solver.Expression;
import tfc.expression_solver.values.Value;

public abstract class Operator {
	public abstract double apply(Expression expression, Value left, Value right);
}
