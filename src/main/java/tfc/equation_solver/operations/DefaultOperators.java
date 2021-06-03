package tfc.equation_solver.operations;

import tfc.equation_solver.operations.operators.*;

import java.util.HashMap;

public class DefaultOperators extends OperatorList {
	private static final HashMap<Character, Operator> operators = new HashMap<>();
	
	// TODO: make this into a javadoc
	// "*/%" and "-+" are together as they are handled in the order they are provided in
	// the space separates the groupings
	// in other words
	// "*/%": handle multiplication, division, and modulus first without sorting
	// make sure they’re before
	// "-+": handle subtraction and addition without sorting
	private static final String order = "*/% -+";
	
	static {
		operators.put('*', new MultiplicationOperator());
		operators.put('-', new SubtractionOperator());
		operators.put('%', new ModulusOperator());
		operators.put('+', new AdditionOperator());
		operators.put('/', new DivisionOperator());
	}
	
	public boolean has(char symbol) {
		return operators.containsKey(symbol);
	}
	
	public Operator get(char symbol) {
		return operators.get(symbol);
	}
	
	public String getOrder() {
		return order;
	}
}
