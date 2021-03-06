/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**
 * @author rboy
 *
 */
public class KingsConstraint extends HandConstraintA{

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public KingsConstraint(Integer min, Integer max, Integer suit) {
		super(min, max, suit);
	}
	
	public KingsConstraint(Integer suit) {
		this(0, 1, suit);
	}
	
	public KingsConstraint(Integer min, Integer max) {
		this(min, max, -1);
	}
	
	public KingsConstraint() {
		this(0, 4, -1);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintA#constraintValue(java.util.List, int)
	 */
	@Override
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card == 13) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public String hash() {
		return new StringBuilder("KingsConstraint").append(suit).toString();
	}

}
