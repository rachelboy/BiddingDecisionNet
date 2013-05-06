package edu.olin.rboy.bridge.constraints;

import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.GameState;

public interface Constraint {
	
	public boolean satisfiesConstraints(GameState state);
	
	public String hash();
	
	public Object getValue(GameState state);

	/* Return a set containing all the possible divisions of the constraint
	 * from no division up to every possible
	 * division into i constraints.
	 * 
	 */
	public List<List<Constraint>> getPossDivisions(int i);
	
	public Constraint newInstance();

}
