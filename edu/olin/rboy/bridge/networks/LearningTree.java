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
import edu.olin.rboy.bridge.constraints.Constraint;

/**Base node of a learning node tree - all states satisfy it.
 * @author rboy
 *
 */
public class LearningTree implements LearningNodeInterface {
	private LearningNodeInterface learningNode;
	
	
	public LearningTree(){
		learningNode = new LearningNode();
	}
	
	public LearningTree(DecisionNodeInterface baseNode) {
		learningNode = new LearningNode(baseNode);
	}

	@Override
	public Set<Bid> getActions() {
		return learningNode.getActions();
	}

	@Override
	public Set<LearningNodeInterface> getApplicableChildren(GameState state) {
		return learningNode.getApplicableChildren(state);
	}

	@Override
	public boolean satisfiesConstraints(GameState state) {
		return true;
	}

	@Override
	public void addLearningInstance(GameState state, Bid action) {
		learningNode.addLearningInstance(state, action);		
	}

	@Override
	public void addChild(LearningNodeInterface child) {
		learningNode.addChild(child);		
	}

	@Override
	public Set<Constraint> getConstraints() {
		return null;
	}

	@Override
	public Map<Bid, Set<GameState>> getLearningInstances() {
		return learningNode.getLearningInstances();	
	}

	@Override
	public Set<LearningNodeInterface> getRegularChildren() {
		return learningNode.getRegularChildren();
	}

	@Override
	public Set<LearningNodeInterface> getElseChildren() {
		return learningNode.getElseChildren();
	}

}
