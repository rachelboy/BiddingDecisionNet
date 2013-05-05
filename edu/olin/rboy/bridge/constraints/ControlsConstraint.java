/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.util.List;

/**
 * @author rboy
 *
 */
public class ControlsConstraint extends HandConstraintA {

	/**
	 * @param min
	 * @param max
	 */
	public ControlsConstraint(Integer min, Integer max, Integer suit) {
		super(min, max, suit);
	}
	
	public ControlsConstraint(Integer suit) {
		this(0, 3, suit);
	}
	
	public ControlsConstraint(Integer min, Integer max) {
		this(min, max, -1);
	}
	
	public ControlsConstraint() {
		this(0, 12, -1);
	}

	@Override
	public int constraintValue(List<List<Integer>> hand, int suit) {
		int count = 0;
		for (Integer card : hand.get(suit)){
			if (card > 12) {
				count += card-12;
			}
		}
		return count;
	}

	@Override
	public String hash() {
		return new StringBuilder("ControlsConstraint").append(suit).toString();
	}

}
