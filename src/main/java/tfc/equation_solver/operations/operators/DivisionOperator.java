package tfc.equation_solver.operations.operators;

import tfc.equation_solver.Equation;
import tfc.equation_solver.operations.Operator;
import tfc.equation_solver.values.Value;

public class DivisionOperator extends Operator {
	public double apply(Equation equation, Value left, Value right) {
		return left.get(equation.parser) / right.get(equation.parser);
	}
}
