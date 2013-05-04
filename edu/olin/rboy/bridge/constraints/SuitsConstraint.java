/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

import edu.olin.rboy.bridge.GameState;

/**Number of suits longer than 3.
 * @author rboy
 *
 */
public class SuitsConstraint extends HandConstraint {

	/**
	 * @param min
	 * @param max
	 */
	public SuitsConstraint(int min, int max) {
		super(min, max);
		// TODO Auto-generated constructor stub
	}
	
	public SuitsConstraint(){
		this(1,3);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraint#constraintValue(edu.olin.rboy.bridge.GameState)
	 */
	@Override
	public int constraintValue(GameState state) {
		List<List<Integer>> hand = state.getHand();
		int count = 0;
		
		for (List<Integer> suit : hand){
			if (suit.size() > 3) {
				count++;
			}
		}
		
		return count;
	}
	
	@Override
	public String hash() {
		return "SuitsConstraint";
	}

}
