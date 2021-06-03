package tfc.equation_solver;

import tfc.equation_solver.operations.DefaultOperators;
import tfc.equation_solver.operations.Operator;
import tfc.equation_solver.operations.OperatorList;
import tfc.equation_solver.sorting.KeyOperator;
import tfc.equation_solver.sorting.StepSorter;
import tfc.equation_solver.values.Constant;
import tfc.equation_solver.values.Value;

import java.util.ArrayList;

public class EquationParser {
	private final OperatorList operators;
	//private final MethodList methods; //TODO
	private final StepSorter sorter;
	
	public EquationParser() {
		this(new DefaultOperators());
	}
	
	public EquationParser(OperatorList operators) {
		this.operators = operators;
		sorter = new StepSorter(operators.getOrder());
	}
	
	public Equation parse(String str) {
		ArrayList<KeyOperator> steps = new ArrayList<>();
		// iter over all chars
		// if char is a '(', and it is already storing to a temp var, increment a counter
		// else if char is a '(', start storing inner equation to temp var
		// else if char is a ')' and said counter is already at 0, parse the inner equation and store that as a value
		// else if char is a ')', decrement said counter
		// if reading through inner equation, break
		// if char is a ' ', discard it
		// else if char is numeric or '.', store it to a temp string
		// else if char is a valid symbol for the operator set, box the found number as a constant
		// else if char is a valid symbol for the operator list, create a Step instance with the last value, found operator, and and symbol char, and add it to the steps array list
		//if none of the above ifs have passed, throw an exception
		//end iter
		//sort steps array list according to operator list step order
		//TODO
	}
	
	public Operator getFor(char op) {
		return operators.get(op);
	}
	
	public Value box(double v) {
		return new Constant(v);
	}
}
