/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

import edu.olin.rboy.bridge.GameState;

/**How balanced the hand is - 0=balanced, 6=freak.
 * @author rboy
 *
 */
public class BalancedConstraint extends HandConstraint {

	/**
	 * @param min
	 * @param max
	 */
	public BalancedConstraint(int min, int max) {
		super(min, max);
	}
	
	public BalancedConstraint() {
		this(0,7);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraint#constraintValue(edu.olin.rboy.bridge.GameState)
	 */
	@Override
	public int constraintValue(GameState state) {
		List<List<Integer>> hand = state.getHand();
		int tot = 0;
		
		for (int i = 0; i<3; i++){
			for (int j = i+1; j<4; j++){
				tot += Math.abs(hand.get(i).size() - hand.get(j).size());
			}
		}
		
		return tot/6;
	}

	@Override
	public String hash() {
		return "BalancedConstraint";
	}

}
