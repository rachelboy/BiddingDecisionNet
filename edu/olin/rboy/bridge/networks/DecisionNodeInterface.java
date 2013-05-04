package edu.olin.rboy.bridge.networks;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.Bidable;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.constraints.Constraint;

public interface DecisionNodeInterface extends Bidable<DecisionNodeInterface> {
	
	public boolean satisfiesConstraints(GameState state);
	
	Set<Constraint> getConstraints();

	Set<DecisionNodeInterface> getRegularChildren();

	Set<DecisionNodeInterface> getElseChildren();
	
	public void addChildren(Collection<DecisionNodeInterface> children);

}
