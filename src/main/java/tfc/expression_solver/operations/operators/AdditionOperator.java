package tfc.expression_solver.operations.operators;

import tfc.expression_solver.Expression;
import tfc.expression_solver.operations.Operator;
import tfc.expression_solver.values.Value;

public class AdditionOperator extends Operator {
	public double apply(Expression expression, Value left, Value right) {
		return left.get(expression.parser) + right.get(expression.parser);
	}
}
