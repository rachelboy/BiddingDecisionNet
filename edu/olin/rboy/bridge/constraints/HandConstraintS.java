/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import edu.olin.rboy.bridge.GameState;

/**A constraint that applies only to a suit.
 * @author rboy
 *
 */
abstract public class HandConstraintS extends HandConstraint {

	int suit;
	
	/**
	 * @param min
	 * @param max
	 */
	public HandConstraintS(int min, int max, int suit) {
		super(min, max);
		this.suit = suit;
	}

	abstract public int constraintValue(GameState state);
	
	public int getSuit() {
		return suit;
	}

}
