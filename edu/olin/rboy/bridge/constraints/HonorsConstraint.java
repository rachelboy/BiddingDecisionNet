/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**Count of cards higher than 9
 * @author rboy
 *
 */
public class HonorsConstraint extends HandConstraintA {

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public HonorsConstraint(Integer min, Integer max, Integer suit) {
		super(min, max, suit);
	}
	
	public HonorsConstraint(Integer suit) {
		this(0, 5, suit);
	}
	
	public HonorsConstraint(Integer min, Integer max) {
		this(min, max, -1);
	}
	
	public HonorsConstraint() {
		this(0, 13, -1);
	}


	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintA#constraintValue(java.util.List, int)
	 */
	@Override
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card > 9) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public String hash() {
		return new StringBuilder("HonorsConstraint").append(suit).toString();
	}

}
