/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**Number of Jacks.
 * @author rboy
 *
 */
public class JacksConstraint extends HandConstraintA {

	/**
	 * @param min
	 * @param max
	 * @param suit
	 */
	public JacksConstraint(Integer min, Integer max, Integer suit) {
		super(min, max, suit);
	}
	
	public JacksConstraint(Integer suit) {
		this(0, 1, suit);
	}
	
	public JacksConstraint(Integer min, Integer max) {
		this(min, max, -1);
	}
	
	public JacksConstraint() {
		this(0, 4, -1);
	}

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.HandConstraintA#constraintValue(java.util.List, int)
	 */
	@Override
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card == 11) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public String hash() {
		return new StringBuilder("JacksConstraint").append(suit).toString();
	}


}
