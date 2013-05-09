/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import edu.olin.rboy.bridge.GameState;

/**Hand Constraint that is either for a single suit, 
 * if given a suit, or for the entire hand.
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
	
	public List<Constraint> getDivision(int i){
		List<Constraint> res = new ArrayList<Constraint>(2);
		res.add(newInstance(min, i));
		res.add(newInstance(i+1, max));
		return res;
	}


	private HandConstraint newInstance(int bottom, int top) {
		Class cls[] = new Class[] {Integer.class, Integer.class, Integer.class};
		Constructor<? extends HandConstraint> construct;
		
		try {
			construct = this.getClass().getConstructor(cls);
			return construct.newInstance(bottom,top,suit);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	abstract public int constraintValue(List<List<Integer>> hand, int suit);

}
