package tfc.expression_solver;

import tfc.expression_solver.operations.DefaultOperators;
import tfc.expression_solver.operations.Operator;
import tfc.expression_solver.operations.OperatorList;
import tfc.expression_solver.sorting.Step;
import tfc.expression_solver.sorting.StepSorter;
import tfc.expression_solver.values.Constant;
import tfc.expression_solver.values.Value;

import java.util.ArrayList;

public class ExpressionParser {
	private final OperatorList operators;
	private final StepSorter sorter;
	public final ArrayList<MethodMarker> methods = new ArrayList<>();
	
	public ExpressionParser() {
		this(new DefaultOperators());
	}
	
	public ExpressionParser(OperatorList operators) {
		this.operators = operators;
		sorter = new StepSorter(operators.getOrder());
	}
	
	public Expression parse(String str) {
		ArrayList<Step> steps = getSteps(str);
		return parseNoSort(sorter.group(steps, this));
	}
	
	public Expression parseNoSort(String str) {
		ArrayList<Step> steps = getSteps(str);
		Expression eq = new Expression(this);
		for (Step step : steps) eq.addPart(step.op, step.value);
		return eq;
	}
	
	protected ArrayList<Step> getSteps(String str) {
		// iter over all chars
		// if char is a '(', and it is already storing to a temp var, increment a counter
		// else if char is a '(', start storing inner equation to temp var
		// else if char is a ')' and said counter is already at 0, parse the inner equation and store that as a value
		// else if char is a ')', decrement said counter
		// if reading through inner equation, continue
		// if char is a ' ', discard it
		// else if char is numeric or '.', store it to a temp string
		// else if char is a valid symbol for the operator set, box the found number as a constant
		// else if char is a valid symbol for the operator list, create a Step instance with the last value, found operator, and and symbol char, and add it to the steps array list
		//if none of the above ifs have passed, throw an exception
		//end iter
		ArrayList<Step> steps = new ArrayList<>();
		l:
		while (str.startsWith("(")) {
			int openParens = 0;
			int cI = 0;
			for (char c : str.toCharArray()) {
				cI++;
				if (cI == str.length()) break;
				if (c == '(') {
					openParens++;
				} else if (c == ')') {
					openParens--;
					if (openParens <= 0) break l;
				}
			}
			str = str.substring(1, str.length() - 1);
		}
		StringBuilder tempEq = new StringBuilder();
		int innerEqCount = 0;
		StringBuilder tempV = new StringBuilder();
		char op = ' ';
		StringBuilder methodParse = new StringBuilder();
		int unclosedParenthesisMethod = 0;
		boolean inStringMethod = false;
		boolean isEscaped = false;
		for (char c : str.toCharArray()) {
			if (Character.isAlphabetic(c) || methodParse.length() != 0) {
				methodParse.append(c);
				if (!isEscaped) {
					if (!inStringMethod) {
						if (c == '(') unclosedParenthesisMethod++;
						if (c == ')') unclosedParenthesisMethod--;
						if (c == '\\') isEscaped = true;
					}
					if (c == '\"') inStringMethod = !inStringMethod;
				} else {
					isEscaped = false;
				}
				if (unclosedParenthesisMethod == 0 && operators.has(op)) {
					for (MethodMarker method : methods) {
						if (method.matches(methodParse.toString())) {
							Value v = method.generate(methodParse.toString());
							if (operators.has(op) || op == ' ') {
								steps.add(new Step(op, getFor(op), v));
							}
							methodParse = new StringBuilder();
							inStringMethod = false;
							isEscaped = false;
						}
					}
				}
				continue;
			}
			if (c == ' ') {
				continue;
			} else if (c == '(' || (tempEq.length() > 0)) {
				if (tempV.length() > 0) {
					Value v = new Constant(Double.parseDouble(tempV.toString()));
					tempV = new StringBuilder();
					steps.add(new Step(op, getFor(op), v));
				}
				if (c == '(' && (tempEq.length() > 0)) innerEqCount += 1;
				else if (c == ')') {
					innerEqCount -= 1;
					if (innerEqCount < 0) {
						tempEq.append(c);
						innerEqCount = 0;
						Value v = parse(tempEq.toString());
						if (operators.has(op) || op == ' ') {
							steps.add(new Step(op, getFor(op), v));
						}
						tempEq = new StringBuilder();
						op = ' ';
						continue;
					}
				}
				tempEq.append(c);
			} else if (Character.isDigit(c) || c == '.') {
				tempV.append(c);
			} else {
				if (tempV.length() > 0) {
					Value v = new Constant(Double.parseDouble(tempV.toString()));
					tempV = new StringBuilder();
					steps.add(new Step(op, getFor(op), v));
				}
				op = c;
			}
		}
		if (tempV.length() > 0) {
			Value v = new Constant(Double.parseDouble(tempV.toString()));
			steps.add(new Step(op, getFor(op), v));
		}
		return steps;
	}
	
	public Operator getFor(char op) {
		return operators.get(op);
	}
	
	public char getSymbol(Operator first) {
		for (char c : operators.getOrder().replace(" ", "").toCharArray()) {
			if (operators.get(c) == first) {
				return c;
			}
		}
		return ' ';
	}
}
