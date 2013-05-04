package edu.olin.rboy.bridge.constraints;

import java.util.Set;

import edu.olin.rboy.bridge.GameState;

public interface Constraint {
	
	public boolean satisfiesConstraints(GameState state);
	
	public String hash();
	
	public Object getValue(GameState state);

	/* Return a set containing all the possible divisions of the constraint
	 * from every possible division into two constraints up to every possible
	 * division into i constraints.
	 * 
	 */
	public Set<Set<Constraint>> getPossDivisions(int i);

}
