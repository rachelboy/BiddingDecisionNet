/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import edu.olin.rboy.bridge.GameState;

/**A hand constraint that applies only to a suit.
 * @author rboy
 *
 */
abstract public class HandConstraintS extends HandConstraint {

	int suit;
	
	/**
	 * @param min
	 * @param max
	 */
	public HandConstraintS(Integer min, Integer max, Integer suit) {
		super(min, max);
		this.suit = suit;
	}

	abstract public int constraintValue(GameState state);
	
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
	
@Override
	
	public Constraint newInstance() {
		try {
			Class cls[] = new Class[] {Integer.class};
			Constructor<? extends HandConstraint> construct = this.getClass().getConstructor(cls);
			return construct.newInstance(suit);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
