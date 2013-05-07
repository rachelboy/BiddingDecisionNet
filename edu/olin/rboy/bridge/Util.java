package edu.olin.rboy.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.constraints.Constraint;

public class Util {
	
	public static boolean checkConstraints(Set<Constraint> constraints, GameState state){
		for (Constraint constraint : constraints){
			if (!constraint.satisfiesConstraints(state)){
				return false;
			}
		}
		return true;
	}

	public static List<Integer> arrayToList(Integer[] array){
		List<Integer> res = new ArrayList<Integer>();
		for (int i=0; i<array.length; i++) {
			res.add(array[i]);
		}
		return res;
	}

}
