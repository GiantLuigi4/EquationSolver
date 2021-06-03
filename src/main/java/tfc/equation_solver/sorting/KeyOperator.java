package tfc.equation_solver.sorting;

import tfc.equation_solver.operations.Operator;
import tfc.equation_solver.values.Value;

//TODO: rename this to step
public class KeyOperator {
	public final char key;
	public final Operator op;
	public final Value value;
	
	public KeyOperator(char key, Operator op, Value value) {
		this.key = key;
		this.op = op;
		this.value = value;
	}
}

