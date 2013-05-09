/**
 * 
 */
package edu.olin.rboy.bridge;

import java.util.HashSet;
import java.util.Set;

/**Finds possible bids for states.
 * 
 * @author rboy
 *
 */
public class Bidder<T extends Bidable<T>> implements BridgeConstants {
	T strategy;
	
	public Bidder(T strategy){
		this.strategy = strategy;
	}
	
	/**Finds all bids recommended by the strategy.
	 * 
	 * @param state
	 * @return
	 */
	public Set<Bid> findBids(GameState state){
		Set<T> currentNodes = findMostSpecifcNodes(state);
		
		Set<Bid> possBids = new HashSet<Bid>();
		for(T node : currentNodes){
			possBids.addAll(node.getActions());
		}
		return possBids;
	}


	/**Find most specific nodes in the strategy that the
	 * given state belongs to.
	 * 
	 * @param state
	 * @return
	 */
	public Set<T> findMostSpecifcNodes(GameState state) {
		Set<T> currentNodes = new HashSet<T>();
		currentNodes.add(strategy);
		Set<T> nextNodes = strategy.getApplicableChildren(state);
		
		while (nextNodes.size() > 0) {
			currentNodes = nextNodes;
			nextNodes = new HashSet<T>();
			for(T node : currentNodes) {
				nextNodes.addAll(node.getApplicableChildren(state));
			}
		}
		return currentNodes;
	}

}
