package edu.olin.rboy.bridge;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.HandConstraint;
import edu.olin.rboy.bridge.networks.DecisionNode;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.LearningNode;
import edu.olin.rboy.bridge.networks.LearningNodeInterface;

/**Induction algorithm for generalizing learning instances.
 * 
 * @author rboy
 *
 */
public class ID3 implements BridgeConstants{

	/**Turns a learning tree with examples into a decision tree
	 * containing inferences from those examples using ID3 algorithm.
	 * 
	 * @param node
	 * @return
	 */
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
		if (newConstraint == null) {
			DecisionNodeInterface res = new DecisionNode(constraints,actions);
			addChildren(node, res);
			nodes.add(res);
			return nodes;
		}
		System.out.println(newConstraint.hash());
		/*System.out.println(newConstraint.min);
		System.out.println(newConstraint.max);*/

		List<Constraint> newConstraints = makeDivisions(newConstraint,learned);
		
		for (Constraint constraint : newConstraints) {
			System.out.println(constraint.hash());
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
				//System.out.println(childActions.size());
				LearningNodeInterface redo = new LearningNode(childConstraints);
				{
					for (Constraint c : redo.getConstraints()){
						if (c.hash().equals(constraint.hash())){
							System.out.println("good");
						}
					}
				}
				for (Bid action : learned.keySet()){
					for (GameState state : learned.get(action)){
						if (redo.satisfiesConstraints(state)){
							redo.addLearningInstance(state, action);
						}
					}
				}
				//System.out.println(redo.getActions().size());
				System.out.println("subdividing");
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

	/**Choose the constraint to divide a node with multiple actions along.
	 * Chooses the constraint that is not currently used that has the
	 * lowest average entropy between the possible bids, normalized to
	 * the number of possible values of the constraint.
	 * 
	 * @param learned
	 * @param constraints
	 * @return
	 */
	private Constraint chooseDividingConstraint(
			Map<Bid, Set<GameState>> learned, Set<Constraint> constraints) {
		Set<Constraint> unusedConstraints = findUnusedConstraints(constraints);
		if (unusedConstraints.size() == 0){
			return null;
		}
		//Will only store the most recent constraint to have a given level of entropy
		Map<Float, Constraint> entropy = new HashMap<Float,Constraint>();
		for (Constraint constraint : unusedConstraints){
			if (constraint instanceof HandConstraint){
				entropy.put(findConstraintEntropy((HandConstraint) constraint,learned), constraint);
			}
		}
		
		Constraint newConstraint = entropy.get(Collections.min(entropy.keySet()));
		if (newConstraint == null){
			System.out.println("null!");
		}
		Constraint res = newConstraint.newInstance();
		return res;
	}

	/**Returns constraints that divide the node in a way that 
	 * minimizes the average entropy of each subnode.
	 * 
	 * @param newConstraint
	 * @param learned
	 * @return
	 */
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
	
	/**Find the average entropy for the distribution of constraint
	 * values for each possible bid, normalized to the size of the 
	 * constraint.
	 * 
	 * @param constraint
	 * @param learned
	 * @return
	 */
	public static Float findConstraintEntropy(HandConstraint constraint,
			Map<Bid, Set<GameState>> learned) {
		float totalStates = 0f;
		float totEntropy = 0f;
		float range = constraint.max - constraint.min;
		Map<Object, Integer> dist;
		for (Bid bid : learned.keySet()){
			 dist = new HashMap<Object, Integer>();
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
			float entropy = 0f;
			for (Object key : dist.keySet()) {
				float p = dist.get(key)/totalStates;
				entropy -= p*Math.log(p);
			}
			totEntropy += entropy/range;
		}
		
		
		return totEntropy/learned.keySet().size();
	}

	/**Find the entropy of the distribution of bids associated
	 * with example game states that fulfill a constraint.
	 * 
	 * @param constraint
	 * @param learned
	 * @return
	 */
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

	/**Find all constraints that aren't currently used.
	 * 
	 * @param constraints
	 * @return
	 */
	public static Set<Constraint> findUnusedConstraints(Set<Constraint> constraints) {
		Set<Constraint> unusedConstraints = new HashSet<Constraint>(allHandConstraints);

		for (Constraint newConstraint : allHandConstraints){
			for (Constraint oldConstraint : constraints) {
				if (oldConstraint.hash().equals(newConstraint.newInstance().hash())){
					unusedConstraints.remove(newConstraint);
				}
			}
		}
		return unusedConstraints;
	}

	/**Recurse on all the learning node's children, and add them
	 * to the decision node.
	 * 
	 * @param node
	 * @param res
	 */
	private void addChildren(LearningNodeInterface node,
			DecisionNodeInterface res) {
		//Doesn't check that child stuff satisfies parent constraints at the moment - could be an issue?
		System.out.println("adding Children");
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
