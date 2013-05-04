/**
 * 
 */
package edu.olin.rboy.bridge.constraints;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.GameState;

/**
 * @author rboy
 *
 */
public abstract class HandConstraint implements Constraint {
	
	int min;
	int max;

	/**
	 * 
	 */
	public HandConstraint(Integer min, Integer max) {
		this.min = min;
		this.max = max;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	abstract public int constraintValue(GameState state);

	/* (non-Javadoc)
	 * @see edu.olin.rboy.bridge.Constraint#satisfiesConstraints(edu.olin.rboy.bridge.GameState)
	 */
	@Override
	public boolean satisfiesConstraints(GameState state) {
		int val = constraintValue(state);
		return (val >= min && val <= max);
	}
	
	public Integer getValue(GameState state) {
		return constraintValue(state);
	}
	
	public List<Integer> getRange(){
		List<Integer> res = new ArrayList<Integer>(2);
		res.add(min);
		res.add(max);
		return res;
	}
	
	public Set<HandConstraint> getDivision(int i){
		Set<HandConstraint> res = new HashSet<HandConstraint>(2);
		Class cls[] = new Class[] {Integer.class, Integer.class};
		Constructor<? extends HandConstraint> construct;
		
		try {
			construct = this.getClass().getConstructor(cls);
			res.add(construct.newInstance(min,i));
			res.add(construct.newInstance(i+1,max));
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	

}
