/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

import edu.olin.rboy.bridge.GameState;

/**Hand Constraint that is either for a single suit, if given a suit, or for the entire hand.
 * @author rboy
 *
 */
abstract public class HandConstraintA extends HandConstraint {
	
	int suit;

	/**
	 * @param min
	 * @param max
	 */
	public HandConstraintA(Integer min, Integer max, Integer suit) {
		super(min, max);
		this.suit = suit;
	}
	

	public int constraintValue(GameState state){
		int count = 0;
		List<List<Integer>> hand = state.getHand();
		if (suit == -1) {
			for (int i = 0; i<4; i++){
				count += constraintValue(hand, i);
			}
		}
		else {
			count = constraintValue(hand, suit);
		}
		return count;
	}
	
	public int getSuit() {
		return suit;
	}
	
	abstract public int constraintValue(List<List<Integer>> hand, int suit);

}
