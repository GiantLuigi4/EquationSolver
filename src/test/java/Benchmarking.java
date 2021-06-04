import tfc.expression_solver.Expression;
import tfc.expression_solver.ExpressionParser;

public class Benchmarking {
	public static void main(String[] args) {
		ExpressionParser parser = new ExpressionParser();
		Expression eq = parser.parse("5*3/3*1*1*4/1/6-4/6/2");
		long solveNs = -1;
		long parseNs = -1;
		long start = System.currentTimeMillis();
		long startNs = System.nanoTime();
		long parseNs1 = 0;
		long solveNs1 = 0;
		for (int i = 0; i < 10000; i++) {
			parseNs1 = (System.nanoTime() - startNs);
			eq = parser.parse("5*3/3*1*1*4/1/6-4/6/2");
			System.out.println("parsing took " + parseNs1 + " ns, " + (System.nanoTime() - start) + " ms");
			if (parseNs == -1) parseNs = parseNs1;
			else parseNs = (parseNs1 + parseNs) / 2;
			start = System.currentTimeMillis();
			startNs = System.nanoTime();
			System.out.println(eq.get());
			solveNs1 = (System.nanoTime() - startNs);
			System.out.println("solving took " + solveNs1 + " ns, " + (System.nanoTime() - start) + " ms");
			if (solveNs == -1) solveNs = solveNs1;
			else solveNs = (solveNs1 + solveNs) / 2;
		}
		System.out.println("average ns parse: " + parseNs);
		System.out.println("average ns solve: " + solveNs);
	}
}
