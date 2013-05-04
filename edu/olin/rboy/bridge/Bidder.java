/**
 * 
 */
package edu.olin.rboy.bridge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author rboy
 *
 */
public class Bidder<T extends Bidable<T>> implements BridgeConstants {
	T strategy;
	
	public Bidder(T strategy){
		this.strategy = strategy;
	}
	

	
	public Set<Bid> findBids(GameState state){
		Set<T> currentNodes = findMostSpecifcNodes(state);
		
		Set<Bid> possBids = new HashSet<Bid>();
		for(T node : currentNodes){
			possBids.addAll(node.getActions());
		}
		return possBids;
	}



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
