/**
 * 
 */
package edu.olin.rboy.bridge.networks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.Util;
import edu.olin.rboy.bridge.constraints.Constraint;

/**Class for newly learned nodes that haven't been analyzed yet. (Keeps every example bid that falls into a given node)
 * @author rboy
 *
 */
public class LearningNode implements LearningNodeInterface {
	private Map<Bid,Set<GameState>> learnedActions = 
			new HashMap<Bid,Set<GameState>>();
	private Set<LearningNodeInterface> prefChildren = 
			new HashSet<LearningNodeInterface>();
	private Set<LearningNodeInterface> normalChildren = 
			new HashSet<LearningNodeInterface>();
	private Set<Constraint> constraints = new HashSet<Constraint>();
	
	
	public LearningNode(){
		
	}
	
	public LearningNode(Set<LearningNodeInterface> parents){
		for (LearningNodeInterface node : parents){
			constraints.addAll(node.getConstraints());
		}
	}
	
	public LearningNode(DecisionNodeInterface baseNode) {
		constraints.addAll(baseNode.getConstraints());
		for (DecisionNodeInterface child : baseNode.getRegularChildren()){
			prefChildren.add(new LearningNode(child));
		}
		for (DecisionNodeInterface child : baseNode.getElseChildren()){
			normalChildren.add(new LearningNode(child));
		}
	}

	@Override
	public Set<Bid> getActions() {
		return learnedActions.keySet();
	}

	@Override
	public Set<LearningNodeInterface> getApplicableChildren(GameState state) {
		Set<LearningNodeInterface> appChildren = new HashSet<LearningNodeInterface>();
		for (LearningNodeInterface child : prefChildren){
			if (child.satisfiesConstraints(state)){
				appChildren.add(child);
			}
		}
		if (appChildren.size() == 0){
			for (LearningNodeInterface child : normalChildren){
				if (child.satisfiesConstraints(state)){
					appChildren.add(child);
				}
			}
		}
		return appChildren;
	}

	@Override
	public boolean satisfiesConstraints(GameState state) {
		return Util.checkConstraints(constraints, state);
	}

	@Override
	public void addLearningInstance(GameState state, Bid action) {
		if (learnedActions.containsKey(action)){
			learnedActions.get(action).add(state);
		}
		else {
			Set<GameState> stateSet = new HashSet<GameState>();
			stateSet.add(state);
			learnedActions.put(action, stateSet);
		}
		
	}

	@Override
	public void addChild(LearningNodeInterface child) {
		normalChildren.add(child);
		
	}

	@Override
	public Set<Constraint> getConstraints() {
		return constraints;
	}

	@Override
	public Map<Bid, Set<GameState>> getLearningInstances() {
		return learnedActions;
	}

	@Override
	public Set<LearningNodeInterface> getRegularChildren() {
		return prefChildren;
	}

	@Override
	public Set<LearningNodeInterface> getElseChildren() {
		return normalChildren;
	}

}
