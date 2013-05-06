package edu.olin.rboy.bridge;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.ConstraintFactory;
import edu.olin.rboy.bridge.constraints.ControlsConstraint;
import edu.olin.rboy.bridge.constraints.HCConstraint;
import edu.olin.rboy.bridge.constraints.HandConstraint;
import edu.olin.rboy.bridge.networks.DecisionNode;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.LearningNode;
import edu.olin.rboy.bridge.networks.LearningNodeInterface;

public class ID3 implements BridgeConstants{

	public Set<DecisionNodeInterface> run(LearningNodeInterface node) {
		Set<Bid> actions = node.getActions();
		Map<Bid, Set<GameState>> learned = node.getLearningInstances();
		Set<Constraint> constraints = node.getConstraints();
		Set<DecisionNodeInterface> nodes = new HashSet<DecisionNodeInterface>();
		
		if (actions.size() <= 1){
			DecisionNodeInterface res = new DecisionNode(constraints, actions);
			addChildren(node, res);
			nodes.add(res);
			return nodes;
		}
		
		HandConstraint newConstraint = (HandConstraint) chooseDividingConstraint(learned,
				constraints);

		List<Constraint> newConstraints = makeDivisions(newConstraint,learned);
		
		for (Constraint constraint : newConstraints) {
			Set<Constraint> childConstraints = new HashSet<Constraint>(constraints);
			childConstraints.add(constraint);
			Set<Bid> childActions = new HashSet<Bid>();
			for (Bid action : learned.keySet()) {
				for (GameState state : learned.get(action)){
					if (constraint.satisfiesConstraints(state)) {
						childActions.add(action);
						break;
					}
				}
			}
			if (childActions.size()>1){
				System.out.println(childActions.size());
				LearningNodeInterface redo = new LearningNode(childConstraints);
				for (Bid action : learned.keySet()){
					for (GameState state : learned.get(action)){
						if (redo.satisfiesConstraints(state)){
							redo.addLearningInstance(state, action);
						}
					}
				}
				System.out.println(redo.getActions().size());
				Set<DecisionNodeInterface> nextDepth = run(redo);
				for (DecisionNodeInterface subnode : nextDepth) {
					addChildren(node, subnode);
					nodes.add(subnode);
				}
			}
			else {
				DecisionNodeInterface res = new DecisionNode(childConstraints,childActions);
				addChildren(node, res);
				nodes.add(res);
			}
		}
		
		return nodes;
	}

	private Constraint chooseDividingConstraint(
			Map<Bid, Set<GameState>> learned, Set<Constraint> constraints) {
		Set<Constraint> unusedConstraints = findUnusedConstraints(constraints);
		//Will only store the most recent constraint to have a given level of entropy
		Map<Float, Constraint> entropy = new HashMap<Float,Constraint>();
		for (Constraint constraint : unusedConstraints){
			entropy.put(findConstraintEntropy(constraint,learned), constraint);
		}
		
		Constraint newConstraint = entropy.get(Collections.min(entropy.keySet()));
		return newConstraint.newInstance();
	}

	public static List<Constraint> makeDivisions(Constraint newConstraint,
			Map<Bid, Set<GameState>> learned) {
		List<List<Constraint>> possDivisions = newConstraint.getPossDivisions(learned.keySet().size()-1);
		Map<Float, List<Constraint>> aveEntropy = new HashMap<Float, List<Constraint>>();
		
		for (List<Constraint> division : possDivisions){
			Float totEntropy = 0f;
			for (Constraint constraint : division){
				totEntropy += findActionEntropy(constraint, learned);
			}
			Float e = totEntropy;
			if (aveEntropy.containsKey(e)){
				if (division.size() < aveEntropy.get(e).size()){
					aveEntropy.put(e, division);
				}
			}
			else {
				aveEntropy.put(e, division);
			}
		}
		return aveEntropy.get(Collections.min(aveEntropy.keySet()));
	}
	
	public static Float findConstraintEntropy(Constraint constraint,
			Map<Bid, Set<GameState>> learned) {
		float totalStates = 0f;
		Map<Object, Integer> dist = new HashMap<Object, Integer>();
		for (Bid bid : learned.keySet()){
			for (GameState state : learned.get(bid)){
				if (constraint.satisfiesConstraints(state)){
					totalStates += 1;
					Object val = constraint.getValue(state);
					if (dist.containsKey(val)){
						dist.put(val, dist.get(val)+1);
					}
					else {
						dist.put(val, 1);
					}
				}
			}
		}
		
		float entropy = 0f;
		for (Object key : dist.keySet()) {
			float p = dist.get(key)/totalStates;
			entropy -= p*Math.log(p);
		}
		return entropy;
	}

	public static Float findActionEntropy(Constraint constraint,
			Map<Bid, Set<GameState>> learned) {
		float totalStates = 0f;
		Map<Bid, Integer> dist = new HashMap<Bid, Integer>();
		for (Bid bid : learned.keySet()){
			for (GameState state : learned.get(bid)){
				if (constraint.satisfiesConstraints(state)){
					totalStates += 1;
					if (dist.containsKey(bid)){
						dist.put(bid, dist.get(bid)+1);
					}
					else {
						dist.put(bid, 1);
					}
				}
			}
		}
		
		float entropy = 0f;
		for (Object key : dist.keySet()) {
			float p = dist.get(key)/totalStates;
			entropy -= p*Math.log(p);
		}
		return entropy;
	}

	private Set<Constraint> findUnusedConstraints(Set<Constraint> constraints) {
		Set<Constraint> unusedConstraints = new HashSet<Constraint>();
		Set<String> usedHashes = new HashSet<String>();
		for (Constraint constraint : constraints){
			usedHashes.add(constraint.hash());
		}
		for (Constraint constraint : allHandConstraints){
			if (!usedHashes.contains(constraint.hash())){
				unusedConstraints.add(constraint);
			}
		}
		/*for (Constraint constraint : ConstraintFactory.makeNullBiddingConstraints(1)){
			if (!usedHashes.contains(constraint.hash())){
				unusedConstraints.add(constraint);
			}
		}*/
		return unusedConstraints;
	}

	private void addChildren(LearningNodeInterface node,
			DecisionNodeInterface res) {
		//Doesn't check that child stuff satisfies parent constraints at the moment - could be an issue?
		Set<DecisionNodeInterface> regularChildren = new HashSet<DecisionNodeInterface>();
		Set<DecisionNodeInterface> elseChildren = new HashSet<DecisionNodeInterface>();
		for (LearningNodeInterface child : node.getRegularChildren()){
			
			regularChildren.addAll(run(child));
		}
		for (LearningNodeInterface child : node.getElseChildren()){
			elseChildren.addAll(run(child));
		}
		res.addChildren(regularChildren);
		res.addChildren(elseChildren);
	}
	


}
