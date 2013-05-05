/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**Number of Aces
 * @author rboy
 *
 */
public class AcesConstraint extends HandConstraintA {

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public AcesConstraint(Integer min, Integer max, Integer suit) {
		super(min, max, suit);
	}
	
	public AcesConstraint(Integer suit) {
		this(0, 1, suit);
	}
	
	public AcesConstraint(Integer min, Integer max) {
		this(min, max, -1);
	}
	
	public AcesConstraint() {
		this(0, 4, -1);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintA#constraintValue(java.util.List, int)
	 */
	@Override
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card == 14) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String hash() {
		return new StringBuilder("AcesConstraint").append(suit).toString();
	}

}
