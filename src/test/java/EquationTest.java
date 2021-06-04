import tfc.equation_solver.Expression;
import tfc.equation_solver.ExpressionParser;
import tfc.equation_solver.operations.DefaultOperators;
import tfc.equation_solver.sorting.Step;
import tfc.equation_solver.sorting.StepSorter;
import tfc.equation_solver.values.Constant;

import java.util.ArrayList;

public class EquationTest {
	public static void main(String[] args) {
		ArrayList<Step> steps = new ArrayList<>();
		DefaultOperators operators = new DefaultOperators();
		steps.add(new Step(' ', null, new Constant(2)));
		steps.add(new Step('+', operators.get('+'), new Constant(4)));
		steps.add(new Step('*', operators.get('*'), new Constant(4)));
		steps.add(new Step('*', operators.get('*'), new Constant(4)));
		steps.add(new Step('/', operators.get('/'), new Constant(1.5)));
		StepSorter sorter = new StepSorter(operators.getOrder());
		ExpressionParser parser = new ExpressionParser(operators);
		sorter.group(steps, parser);
		Expression eq = new Expression(parser);
		for (Step step : steps) eq.addPart(step.op, step.value);
		System.out.println(eq);
		System.out.println(eq.get());
		Expression eq1 = parser.parse("(2.0+4.0*4.0*4.0/1.5)");
		System.out.println(eq1);
		System.out.println(eq1.get());
		eq1 = parser.parse("2*3/5+3-6*3/2");
		System.out.println(eq1);
		System.out.println(eq1.get());
		System.out.println(parser.parse("((2*3)/5)+3-((6*3)/2)"));
		Expression eq2 = parser.parse("3(2 + 6)");
		System.out.println(eq2);
		System.out.println(eq2.get());
		Expression eq3 = parser.parse("3(2)");
		System.out.println(eq3);
		System.out.println(eq3.get());
		Expression eq4 = parser.parse("(2)3");
		System.out.println(eq4);
		System.out.println(eq4.get());
		Expression eq5 = parser.parse("(2+)3");
		System.out.println(eq5);
		System.out.println(eq5.get());
	}
}
