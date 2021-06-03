package tfc.equation_solver.operations;

public abstract class OperatorList {
	public abstract Operator get(char symbol);
	public abstract boolean has(char symbol);
	public abstract String getOrder();
}
