/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

import edu.olin.rboy.bridge.GameState;

/**Length of the suit.
 * @author rboy
 *
 */
public class SuitLengthConstraint extends HandConstraintS {

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public SuitLengthConstraint(int min, int max, int suit) {
		super(min, max, suit);
	}
	
	public SuitLengthConstraint(int suit) {
		this(0,13,suit);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintS#constraintValue(edu.olin.rboy.bridge.GameState)
	 */
	@Override
	public int constraintValue(GameState state) {
		List<List<Integer>> hand = state.getHand();
		
		return hand.get(suit).size();
	}
	
	@Override
	public String hash() {
		return new StringBuilder("SuitLengthConstraint").append(suit).toString();
	}

}
