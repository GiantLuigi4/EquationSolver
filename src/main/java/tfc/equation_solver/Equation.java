package tfc.equation_solver;

import tfc.equation_solver.operations.Operator;
import tfc.equation_solver.values.Constant;
import tfc.equation_solver.values.Value;
import tfc.utils.Pair;

import java.util.ArrayList;

//TODO: equation builder or smth
public class Equation extends Value {
	private final ArrayList<Pair<Operator, Value>> equation = new ArrayList<>();
	public final EquationParser parser;
	
	public Equation(EquationParser parser) {
		this.parser = parser;
	}
	
	public void addPart(Operator op, Value val) {
		equation.add(new Pair<>(op, val));
	}
	
	public double get() {
		return get(parser);
	}
	
	public double get(EquationParser parser) {
		double v = 0;
		for (Pair<Operator, Value> step : equation) {
			if (step.first == null) v = step.second.get(parser);
			else v = step.first.apply(this, new Constant(v), step.second);
		}
		return v;
	}
}