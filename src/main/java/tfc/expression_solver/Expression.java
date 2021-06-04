package tfc.expression_solver;

import tfc.expression_solver.operations.Operator;
import tfc.expression_solver.values.Constant;
import tfc.expression_solver.values.Value;
import tfc.utils.Pair;

import java.util.ArrayList;

//TODO: expression builder or smth
public class Expression extends Value {
	private final ArrayList<Pair<Operator, Value>> expression = new ArrayList<>();
	public final ExpressionParser parser;
	
	public Expression(ExpressionParser parser) {
		this.parser = parser;
	}
	
	public void addPart(Operator op, Value val) {
		expression.add(new Pair<>(op, val));
	}
	
	/**
	 * returns the value which the expression gets resolved to under the parser the expression was parsed by
	 */
	public double get() {
		return get(parser);
	}
	
	/**
	 * returns true if there are no variable or method calls anywhere in the expression, including in inner expressions
	 * if this is true, it means the expression is able to be represented using a single number without ever being wrong
	 * if this does wind up being true, the expression can be replaced with an instance of a Constant
	 * if you are using this for a program you plan to make be public which isn’t just a calculator, make sure you do so, or even just store it as a double instead of a Constant if possible
	 */
	public boolean isConstant() {
		for (Pair<Operator, Value> step : expression)
			if (!isValueConstant(step.second)) return false;
		return true;
	}
	
	/**
	 * returns true if it is a builtin Value type (Constant or Expression) and will never change
	 * in other words, it’s always true if the value is an instance of Constant, and sometimes true if it’s an instance of Expression
	 * for Variables and Methods, when those are implemented, it’ll always return false
	 * for custom Value types, it’ll also always return false
	 */
	public static Boolean isValueConstant(Value v) {
		if (v instanceof Expression) {
			return ((Expression) v).isConstant();
		} else return v instanceof Constant;
	}
	
	/**
	 * returns true if the any nested expressions have no non constants
	 */
	public boolean hasInnerConstantExpression() {
		if (isConstant()) return false;
		boolean hasNonConstant = false;
		for (Pair<Operator, Value> step : expression) {
			if (step.second instanceof Expression) {
				if (((Expression) step.second).hasInnerConstantExpression()) return true;
			}
			if (!isValueConstant(step.second)) hasNonConstant = true;
		}
		if (this.expression.size() == 1)
			return false;
		return !hasNonConstant;
	}
	
	public Value getMinimum() {
		return (expression.size() == 1) ? expression.get(0).second : this;
	}
	
	/**
	 * if the entire expression is constant, there’s no reason to resolve all constants of all inner expressions, it can just be simplified to just the constant final value right off the bat
	 * if there are non constant inner expressions, then it will resolve constants on those, and eventually all constant inner expressions will be an expression of just the constant value
	 * simplify, when implemented, will take those and simplify the expression to the bare minimum it can be other than redundant inner expressions which hold singular inner expressions
	 * returns a new Expression, which should return exactly the same value as the source function, but in a simpler fashion
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
		for (Pair<Operator, Value> step : expression) {
			if (step.second instanceof Expression) {
				if (((Expression) step.second).hasInnerConstantExpression()) {
					eq.addPart(step.first, ((Expression) step.second).resolveConstants().getMinimum());
				} else eq.addPart(step.first, step.second);
			} else eq.addPart(step.first, step.second);
		}
		if (eq.isConstant())
			throw new RuntimeException("Idk what happened, but something went horribly wrong… A non constant expression: " + this.toString() + " resolved to a constant expression: " + eq.toString());
		return eq;
	}
	
	/**
	 * simplifies expressions, will likely be imprecise however
	 * use in performance critical spots with massive repeated expressions, not with simple expressions that don't need high performance
	 * returns a new Expression, which will return a value similar to the correct answer, but is simplified
	 * this is entirely here for optimization purposes
	 */
	public Expression simplify() {
		// general rundown of simplification:
		// if "hasInnerConstantExpression" returns true, resolve values and return the result of simplify on that
		// elsewise:
		// create an new Expression under the parser which parsed the Expression in question
		// two values need to be taken in mind at a time: current and previous
		// it should start scanning on the second value, and go until the last value
		// if the current and previous values are both constant, resolve both values and run the operator of the second value on them, setting the operator of the new value to be the operator of the old current value
		// add the new current value and operator to the Expression created at the start of the method
		// return the simplified expression once done iterating through all values
		if (hasInnerConstantExpression()) return resolveConstants().simplify();
		Expression eq = new Expression(parser);
		Value lastV = null;
		Operator lastO = null;
		for (int i = 1; i < expression.size(); i++) {
			Pair<Operator, Value> step0 = expression.get(i - 1);
			Pair<Operator, Value> step1 = expression.get(i);
			if (isValueConstant(step0.second) && isValueConstant(step1.second)) {
				if (lastV == null) lastV = step0.second;
				lastV = new Constant(step1.first.apply(this, lastV, step1.second));
			} else {
				if (lastV != null) eq.addPart(lastO, lastV);
				if (i == 1)
					if (step0.second instanceof Expression)
						eq.addPart(step0.first, ((Expression) step0.second).simplify());
					else eq.addPart(step0.first, step0.second);
				if (step1.second instanceof Expression) lastV = ((Expression) step1.second).simplify();
				else lastV = step1.second;
				lastO = step1.first;
			}
		}
		if (lastV != null) eq.addPart(lastO, lastV);
		return eq;
	}
	
	/**
	 * returns the value which the expression gets resolved to under the parser provided to the method
	 */
	public double get(ExpressionParser parser) {
		double v = 1;
		for (Pair<Operator, Value> step : expression) {
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
		for (Pair<Operator, Value> operatorValuePair : expression) {
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
