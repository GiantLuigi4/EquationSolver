package tfc.equation_solver.sorting;

import tfc.equation_solver.ExpressionParser;

import java.util.ArrayList;
import java.util.Arrays;

//reverse sort is needed (sort end to start)
public class StepSorter {
	private final ArrayList<String> stepGroupings = new ArrayList<>();
	
	public StepSorter(String groupings) {
		stepGroupings.addAll(Arrays.asList(groupings.split(" ")));
	}
	
	public String group(ArrayList<Step> steps, ExpressionParser parser) {
		StringBuilder eq = new StringBuilder("(");
		int parenCount = 0;
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
			if (step.key != ' ' && g < g1) {
				eq.append(")");
				parenCount--;
			}
			eq.append(step1.key);
		}
		Step step = steps.get(steps.size() - 1);
		eq.append(step.value.toString(parser));
		eq.append(")");
		for (int i = 0; i < parenCount; i++) eq.append(")");
		return eq.toString();
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
