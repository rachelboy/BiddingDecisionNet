/**
 * 
 */
package edu.olin.rboy.bridge.networks;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.constraints.Constraint;

/**
 * @author rboy
 *
 */
public class DecisionNode implements DecisionNodeInterface{
	Set<DecisionNodeInterface> regularChildren = new HashSet<DecisionNodeInterface>();
	Set<DecisionNodeInterface> elseChildren = new HashSet<DecisionNodeInterface>();
	Set<Bid> actions;
	Set<Constraint> constraints;
	
	public DecisionNode(Set<Constraint> constraints, Set<Bid> actions) {
		this.constraints = constraints;
		this.actions = actions;
	}
	
	@Override
	public boolean satisfiesConstraints(GameState state){
		for (Constraint constraint : constraints){
			if (!constraint.satisfiesConstraints(state)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Set<Bid> getActions() {
		return actions;
	}

	@Override
	public Set<DecisionNodeInterface> getApplicableChildren(GameState state) {
		Set<DecisionNodeInterface> appChildren = new HashSet<DecisionNodeInterface>();
		
		for (DecisionNodeInterface child : regularChildren){
			if (child.satisfiesConstraints(state)){
				appChildren.add(child);
			}
		}
		
		//if (appChildren.size() == 0){
			for (DecisionNodeInterface child : elseChildren){
				if (child.satisfiesConstraints(state)){
					appChildren.add(child);
				}
			}
		//}
		
		return appChildren;
	}

	@Override
	public Set<Constraint> getConstraints() {
		return constraints;
	}

	@Override
	public Set<DecisionNodeInterface> getRegularChildren() {
		return regularChildren;
	}

	@Override
	public Set<DecisionNodeInterface> getElseChildren() {
		return elseChildren;
	}
	
	public void addChildren(Collection<DecisionNodeInterface> children) {
		if (regularChildren.size() > 0){
			elseChildren.addAll(children);
		}
		else {
			regularChildren.addAll(children);
		}
	}
	
}
