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

/**A constraint on the properties of the hand in a 
 * game state, where to satify the constraint some
 * property of hte hand must be between a minimum
 * and maximum value.
 * 
 * @author rboy
 *
 */
public abstract class HandConstraint implements Constraint {
	
	public int min;
	public int max;

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
	
	public List<Constraint> getDivision(int i){
		List<Constraint> res = new ArrayList<Constraint>(2);
		res.add(newInstance(min, i));
		res.add(newInstance(i+1, max));
		
		return res;
	}

	private HandConstraint newInstance(int bottom, int top) {
		Class cls[] = new Class[] {Integer.class, Integer.class};
		Constructor<? extends HandConstraint> construct;
		
		try {
			construct = this.getClass().getConstructor(cls);
			return construct.newInstance(bottom,top);
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
			Constructor<? extends HandConstraint> construct = this.getClass().getConstructor(new Class[]{});
			return construct.newInstance();
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
	
	@Override
	public List<List<Constraint>> getPossDivisions(int i){
		List<List<Constraint>> baseSet = new ArrayList<List<Constraint>>();
		List<Constraint> baseList = new ArrayList<Constraint>();
		baseList.add(this);
		baseSet.add(baseList);
		return getPossDivisionsHelper(i, baseSet);
	}
	
	private List<List<Constraint>> getPossDivisionsHelper(int i, List<List<Constraint>> prevDepth) {
		if (i<=0){
			return new ArrayList<List<Constraint>>();
		}
		List<List<Constraint>> nextDepth = new ArrayList<List<Constraint>>();
		for (List<Constraint> cSet : prevDepth) {
			HandConstraint con = (HandConstraint) cSet.get(0);
			for (int j = con.min; j < con.max; j++){
				List<Constraint> nextC = new ArrayList<Constraint>(cSet);
				List<Constraint> add = con.getDivision(j);
				nextC.set(0, add.get(0));
				nextC.add(add.get(1));
				nextDepth.add(nextC);
			}
		}
		prevDepth.addAll(nextDepth);
		prevDepth.addAll(getPossDivisionsHelper(i-1, nextDepth));
		return prevDepth;
	}

}
