/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**Constraint on high card points in suit, if given a suit, or in the whole hand otherwise.
 * @author rboy
 *
 */
public class HCPConstraint extends HandConstraintA {
	int suit;

	/**
	 * @param min
	 * @param max
	 */
	public HCPConstraint(Integer min, Integer max, Integer suit) {
		super(min, max, suit);
	}
	
	public HCPConstraint(Integer suit) {
		this(0, 10, suit);
	}
	
	public HCPConstraint(Integer min, Integer max) {
		this(min, max, -1);
	}
	
	public HCPConstraint() {
		this(0, 37, -1);
	}
	
	public int getSuit() {
		return suit;
	}
	
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card > 10) {
				count += card-10;
			}
		}
		return count;
	}
	
	@Override
	public String hash() {
		return new StringBuilder("HCPConstraint").append(suit).toString();
	}

}
