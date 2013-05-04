package edu.olin.rboy.bridge;

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


}
