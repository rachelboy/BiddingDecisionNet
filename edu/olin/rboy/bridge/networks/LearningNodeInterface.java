package edu.olin.rboy.bridge.networks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.Bidable;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.constraints.Constraint;

public interface LearningNodeInterface extends Bidable<LearningNodeInterface>{

	public void addLearningInstance(GameState state, Bid action);
	
	public void addChild(LearningNodeInterface child);

	Set<LearningNodeInterface> getApplicableChildren(GameState state);

	boolean satisfiesConstraints(GameState state);

	public Set<Constraint> getConstraints();
	
	public Map<Bid, Set<GameState>> getLearningInstances();

	public Set<LearningNodeInterface> getRegularChildren();

	public Set<LearningNodeInterface> getElseChildren();
}
