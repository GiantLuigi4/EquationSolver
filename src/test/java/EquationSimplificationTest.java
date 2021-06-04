import tfc.equation_solver.Expression;
import tfc.equation_solver.ExpressionParser;

public class EquationSimplificationTest {
	public static void main(String[] args) {
		ExpressionParser parser = new ExpressionParser();
		Expression eq1 = parser.parse("5*3/3*1*1*4/1/6-4/6/2");
		System.out.println(eq1);
		System.out.println(eq1.resolveConstants());
		System.out.println(eq1.simplify());
		System.out.println(eq1.get());
		System.out.println(eq1.resolveConstants().get());
		System.out.println(eq1.simplify().get());
		
		System.out.println();
		Expression eq2 = parser.parse("(((((5*3)/3)*1*1*4)/1)/6)-((4/6)/2)");
		System.out.println(eq2);
		System.out.println(eq2.resolveConstants());
		System.out.println(eq2.simplify());
		System.out.println(eq2.get());
		System.out.println(eq2.resolveConstants().get());
		System.out.println(eq2.simplify().get());
		
		System.out.println();
		Expression eq3 = parser.parse("(((((4*3)/3)*1*1*4)/1)/6)-((4/6)/2)");
		System.out.println(eq3);
		System.out.println(eq3.resolveConstants());
		System.out.println(eq3.simplify());
		System.out.println(eq3.get());
		System.out.println(eq3.resolveConstants().get());
		System.out.println(eq3.simplify().get());
	}
}
