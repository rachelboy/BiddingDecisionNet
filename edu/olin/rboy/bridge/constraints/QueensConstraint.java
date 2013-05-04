/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**Number of Queens.
 * @author rboy
 *
 */
public class QueensConstraint extends HandConstraintA {

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public QueensConstraint(int min, int max, int suit) {
		super(min, max, suit);
	}
	
	public QueensConstraint(int suit) {
		this(0, 1, suit);
	}
	
	public QueensConstraint(int min, int max) {
		this(min, max, -1);
	}
	
	public QueensConstraint() {
		this(0, 4, -1);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintA#constraintValue(java.util.List, int)
	 */
	@Override
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card == 12) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public String hash() {
		return new StringBuilder("QueensConstraint").append(suit).toString();
	}

}
