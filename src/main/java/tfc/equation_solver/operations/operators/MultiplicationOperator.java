package tfc.equation_solver.operations.operators;

import tfc.equation_solver.Expression;
import tfc.equation_solver.operations.Operator;
import tfc.equation_solver.values.Value;

public class MultiplicationOperator extends Operator {
	public double apply(Expression expression, Value left, Value right) {
		return left.get(expression.parser) * right.get(expression.parser);
	}
}
