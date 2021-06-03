package tfc.equation_solver.sorting;

import tfc.equation_solver.Equation;
import tfc.equation_solver.EquationParser;

import java.util.ArrayList;
import java.util.Arrays;

//reverse sort is needed (sort end to start)
public class StepSorter {
	private final ArrayList<String> stepGroupings = new ArrayList<>();
	
	public StepSorter(String groupings) {
		stepGroupings.addAll(Arrays.asList(groupings.split(" ")));
	}
	
	public String group(ArrayList<Step> steps, EquationParser parser) {
		StringBuilder eq = new StringBuilder("(");
		int parenCount = 0;
//		{
//			Step step = steps.get(0);
//			Step step1 = steps.get(1);
//			eq.append(step.value.toString(parser));
//			eq.append(step1.key);
//		}
//		for (int i = 1; i < steps.size(); i++) {
		for (int i = 0; i < steps.size() - 1; i++) {
			Step step = steps.get(i);
			Step step1 = steps.get(i + 1);
			int g = getGroupingNumber(step.key);
			int g1 = getGroupingNumber(step1.key);
			if (step.key != ' ' && g > g1) {
				eq.append("(");
				parenCount++;
			}
			eq.append(step.value.toString(parser));
			eq.append(step1.key);
		}
		Step step = steps.get(steps.size() - 1);
		eq.append(step.value.toString(parser));
		eq.append(")");
		for (int i = 0; i < parenCount; i++) eq.append(")");
		return eq.toString();
	}
	
	// TODO: convert this to javadoc
	// should s1 move up in the list
	
	// s1: +
	// s2: *
	// g+ > g*
	// s1 does not move up
	
	// s1 = +
	// s2 = -
	// g+ == g-
	// s1 does not move up
	public boolean shouldGoUp(Step s1, Step s2) {
		int g1 = getGroupingNumber(s1.key);
		int g2 = getGroupingNumber(s2.key);
		return g2 < g1;
	}
	
	public int getGroupingNumber(char symbol) {
		int i = 0;
		for (String s : stepGroupings) {
			if (s.contains(String.valueOf(symbol))) return i;
			i++;
		}
		return -1;
	}
}
