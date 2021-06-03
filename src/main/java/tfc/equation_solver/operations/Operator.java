package tfc.equation_solver.operations;

import tfc.equation_solver.Equation;
import tfc.equation_solver.values.Value;

public abstract class Operator {
	public abstract double apply(Equation equation, Value left, Value right);
}
