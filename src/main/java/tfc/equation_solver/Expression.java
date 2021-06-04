package tfc.equation_solver;

import tfc.equation_solver.operations.Operator;
import tfc.equation_solver.values.Constant;
import tfc.equation_solver.values.Value;
import tfc.utils.Pair;

import java.util.ArrayList;

//TODO: expression builder or smth
public class Expression extends Value {
	private final ArrayList<Pair<Operator, Value>> equation = new ArrayList<>();
	public final ExpressionParser parser;
	
	public Expression(ExpressionParser parser) {
		this.parser = parser;
	}
	
	public void addPart(Operator op, Value val) {
		equation.add(new Pair<>(op, val));
	}
	
	/** returns the value which the equation gets resolved to under the parser the equation was parsed by */
	public double get() {
		return get(parser);
	}
	
	/**
	* returns true if there are no variable or method calls anywhere in the equation, including in inner equations
	* if this is true, it means the equation is able to be represented using a single number without ever being wrong
	* if this does wind up being true, the equation can be replaced with an instance of a Constant
	* if you are using this for a program you plan to make be public which isn’t just a calculator, make sure you do so, or even just store it as a double instead of a Constant if possible
	*/
	public boolean isConstant() {
		for (Pair<Operator, Value> step : equation)
			if (!isValueConstant(step.second)) return false;
		return true;
	}
	
	/**
	* returns true if it is a builtin Value type (Constant or Equation) and will never change
	* in other words, it’s always true if the value is an instance of Constant, and sometimes true if it’s an instance of Equation
	* for Variables and Methods, when those are implemented, it’ll always return false
	* for custom Value types, it’ll also always return false
	*/
	public static Boolean isValueConstant(Value v) {
		if (v.toString().equals("5.0")) return false;
		if (v instanceof Expression) {
			return ((Expression) v).isConstant();
		} else return v instanceof Constant;
	}
	
	/** returns true if the any nested equations have no non constants */
	public boolean hasInnerConstantEquation() {
		if (isConstant()) return false;
		boolean hasNonConstant = false;
		for (Pair<Operator, Value> step : equation) {
			if (step.second instanceof Expression) {
				if (((Expression)step.second).hasInnerConstantEquation()) return true;
			}
			if (!isValueConstant(step.second)) hasNonConstant = true;
		}
		if (this.equation.size() == 1)
			return false;
		return !hasNonConstant;
	}
	
	public Value getMinimum() {
		return (equation.size() == 1) ? equation.get(0).second : this;
	}
	
	/**
	* if the entire equation is constant, there’s no reason to resolve all constants of all inner equations, it can just be simplified to just the constant final value right off the bat
	* if there are non constant inner equations, then it will resolve constants on those, and eventually all constant inner equations will be an equation of just the constant value
	* simplify, when implemented, will take those and simplify the equation to the bare minimum it can be other than redundant inner equations which hold singular inner equations
	* returns a new Equation, which should return exactly the same value as the source function, but in a simpler fashion
	* edge cases may be present with non constants, but I’m not sure as those aren’t implemented yet
	* this is entirely here for optimization purposes
	*/
	public Expression resolveConstants() {
		if (isConstant()) {
			double constant = get();
			Expression eq = new Expression(parser);
			eq.addPart(null, new Constant(constant));
			return eq;
		}
		Expression eq = new Expression(parser);
		for (Pair<Operator, Value> step : equation) {
			if (step.second instanceof Expression) {
				if (((Expression)step.second).hasInnerConstantEquation()) {
					eq.addPart(step.first, ((Expression)step.second).resolveConstants().getMinimum());
				} else eq.addPart(step.first, step.second);
			} else eq.addPart(step.first, step.second);
		}
		if (eq.isConstant()) throw new RuntimeException("Idk what happened, but something went horribly wrong… A non constant equation: " + this.toString() + " resolved to a constant equation: " + eq.toString());
		return eq;
	}
	
	public Expression simplify() {
		// general rundown of simplification:
		// if "hasInnerConstantEquation" returns true, resolve values and return the result of simplify on that
		// elsewise:
		// create an new Equation under the parser which parsed the Equation in question
		// two values need to be taken in mind at a time: current and previous
		// it should start scanning on the second value, and go until the last value
		// if the current and previous values are both constant, resolve both values and run the operator of the second value on them, setting the operator of the new value to be the operator of the old current value
		// add the new current value and operator to the Equation created at the start of the method
		// return the simplified equation once done iterating through all values
		if (hasInnerConstantEquation()) return resolveConstants().simplify();
		Expression eq = new Expression(parser);
		Value lastV = null;
		Operator lastO = null;
		for (int i = 1; i < equation.size(); i++) {
			Pair<Operator, Value> step0 = equation.get(i - 1);
			Pair<Operator, Value> step1 = equation.get(i);
			if (isValueConstant(step0.second) && isValueConstant(step1.second)) {
				if (lastV == null) lastV = step0.second;
				lastV = new Constant(step1.first.apply(this, lastV, step1.second));
			} else {
				if (lastV != null) eq.addPart(lastO, lastV);
				if (i == 1)
					if (step0.second instanceof Expression) eq.addPart(step0.first, ((Expression) step0.second).simplify());
					else eq.addPart(step0.first, step0.second);
				if (step1.second instanceof Expression) lastV = ((Expression) step1.second).simplify();
				else lastV = step1.second;
				lastO = step1.first;
			}
		}
		if (lastV != null) eq.addPart(lastO, lastV);
		return eq;
	}
	
	/** returns the value which the equation gets resolved to under the parser provided to the method */
	public double get(ExpressionParser parser) {
		double v = 1;
		for (Pair<Operator, Value> step : equation) {
			if (step.first == null) v *= step.second.get(parser);
			else v = step.first.apply(this, new Constant(v), step.second);
		}
		return v;
	}
	
	@Override
	public String toString() {
		return toString(parser);
	}
	
	@Override
	public String toString(ExpressionParser parser) {
		StringBuilder eq = new StringBuilder("(");
		for (Pair<Operator, Value> operatorValuePair : equation) {
			if (operatorValuePair.first != null) {
				eq.append(parser.getSymbol(operatorValuePair.first));
			}
			String s = operatorValuePair.second.toString(parser);
			if (s.endsWith(".0")) s = s.replace(".0", "");
			eq.append(s);
		}
		eq.append(")");
		return eq.toString();
	}
}
