/**
 * 
 */
package edu.olin.rboy.bridge;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.networks.DecisionTree;
import edu.olin.rboy.bridge.networks.LearningNode;
import edu.olin.rboy.bridge.networks.LearningNodeInterface;
import edu.olin.rboy.bridge.networks.LearningTree;

/** 
 * @author rboy
 *
 */
public class Learner implements BridgeConstants {
	LearningTree strategy;
	Set<LearningNode> newNodes = new HashSet<LearningNode>();
	Bidder<LearningNodeInterface> bidder;

	/**
	 * 
	 */
	public Learner(DecisionTree strategy) {
		this.strategy = new LearningTree(strategy);
		bidder = new Bidder<LearningNodeInterface>(this.strategy);
	}
	
	public void learn(GameState state, Bid action){
		Set<LearningNodeInterface> nodes = bidder.findMostSpecifcNodes(state);
		
		if (nodes.size() > 1) {
			LearningNodeInterface newNode = new LearningNode(nodes);
			newNode.addLearningInstance(state, action);
			for (LearningNodeInterface node : nodes){
				node.addChild(newNode);
			}
		}
		
		else {
			for (LearningNodeInterface node : nodes){
				node.addLearningInstance(state, action);
			}
		}
	}
	
	public LearningTree getStrategy(){
		return strategy;
	}

}
