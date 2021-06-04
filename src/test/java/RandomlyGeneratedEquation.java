import tfc.equation_solver.Expression;
import tfc.equation_solver.ExpressionParser;

import java.util.Random;

public class RandomlyGeneratedEquation {
	public static void main(String[] args) {
		StringBuilder s = new StringBuilder();
		char[] ops = new char[]{'+', '-', '*', '/', '%'};
		for (int i = 0; i < new Random().nextInt(8) + 8; i++) {
			s.append(new Random().nextInt(6) + 1);
			s.append(ops[new Random().nextInt(ops.length - 1)]);
		}
		s = new StringBuilder(s.substring(0, s.length() - 1));
		System.out.println(s);
		ExpressionParser parser = new ExpressionParser();
		Expression expression = parser.parse(s.toString());
		System.out.println(expression);
		System.out.println(expression.get());
	}
}
