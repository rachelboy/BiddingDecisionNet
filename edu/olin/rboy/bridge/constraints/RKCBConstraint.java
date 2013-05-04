/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

import edu.olin.rboy.bridge.GameState;

/**Count of Roman Key Cards.
 * @author rboy
 *
 */
public class RKCBConstraint extends HandConstraintS {

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public RKCBConstraint(int min, int max, int suit) {
		super(min, max, suit);
	}
	
	public RKCBConstraint(int suit) {
		this(0,5,suit);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintS#constraintValue(edu.olin.rboy.bridge.GameState)
	 */
	@Override
	public int constraintValue(GameState state) {
		List<List<Integer>> hand = state.getHand();
		int count = 0;
		
		for (List<Integer> length : hand) {
			int i = hand.indexOf(length);
			for(Integer card : length) {
				if (card == 14){
					count++;
				}
				else if (card ==13 && i==suit) {
					count++;
				}
			}
		}
		return count;
	}
	
	@Override
	public String hash() {
		return new StringBuilder("RKCBConstraint").append(suit).toString();
	}

}
