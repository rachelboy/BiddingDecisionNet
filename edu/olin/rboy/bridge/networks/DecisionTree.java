/**
 * 
 */
package edu.olin.rboy.bridge.networks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.BridgeConstants;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.constraints.Constraint;

/**
 * @author rboy
 *
 */
public class DecisionTree implements DecisionNodeInterface, BridgeConstants {

	DecisionNode node;
	
	public DecisionTree(){
		Set<Bid> actionSet = new HashSet<Bid>();
		actionSet.add(PASS);
		node = new DecisionNode(new HashSet<Constraint>(), actionSet);
	}


	@Override
	public Set<DecisionNodeInterface> getApplicableChildren(GameState state) {
		return node.getApplicableChildren(state);
	}

	public void addChildren(Collection<DecisionNodeInterface> children) {
		node.addChildren(children);
	}

	@Override
	public Set<Bid> getActions() {
		return node.getActions();
	}

	@Override
	public boolean satisfiesConstraints(GameState state) {
		return true;
	}

	@Override
	public Set<Constraint> getConstraints() {
		return node.getConstraints();
	}

	@Override
	public Set<DecisionNodeInterface> getRegularChildren() {
		return node.getRegularChildren();
	}

	@Override
	public Set<DecisionNodeInterface> getElseChildren() {
		return node.getElseChildren();
	}


}
