package tfc.equation_solver.sorting;

import java.util.ArrayList;
import java.util.Arrays;

//reverse sort is needed (sort end to start)
public class StepSorter {
	private final ArrayList<String> stepGroupings = new ArrayList<>();
	
	public StepSorter(String groupings) {
		stepGroupings.addAll(Arrays.asList(groupings.split(" ")));
	}
	
	// TODO: sort method
	
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
	public boolean shouldGoUp(KeyOperator s1, KeyOperator s2) {
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
