/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**Count of Aces, Kings, and Queens.
 * @author rboy
 *
 */
public class HCConstraint extends HandConstraintA {

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public HCConstraint(int min, int max, int suit) {
		super(min, max, suit);
	}
	
	public HCConstraint(int suit) {
		this(0, 3, suit);
	}
	
	public HCConstraint(int min, int max) {
		this(min, max, -1);
	}
	
	public HCConstraint() {
		this(0, 12, -1);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintA#constraintValue(java.util.List, int)
	 */
	@Override
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card > 11) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String hash() {
		return new StringBuilder("HCConstraint").append(suit).toString();
	}

}
