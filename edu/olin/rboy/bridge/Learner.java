/**
 * 
 */
package edu.olin.rboy.bridge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	
	public Map<GameState, Bid> getExamples(String filename) throws FileNotFoundException{
		Map<GameState, Bid> res = new HashMap<GameState, Bid>();
		
		FileReader fr = null;
		fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		
		List<Integer> clubs;
		List<Integer> diamonds;
		List<Integer> hearts;
		List<Integer> spades;
		List<List<Integer>> hand;
		Bid bid;
		
		String line = null;
		try {
			while ((line = br.readLine())!= null) {
				clubs = new ArrayList<Integer>();
				diamonds = new ArrayList<Integer>();
				hearts = new ArrayList<Integer>();
				spades = new ArrayList<Integer>();
				hand = new ArrayList<List<Integer>>();
				
				writeSuit(clubs, line);
				line = br.readLine();
				writeSuit(diamonds, line);
				line = br.readLine();
				writeSuit(hearts, line);
				line = br.readLine();
				writeSuit(spades, line);
				line = br.readLine();
				bid = getBid(line);
				line = br.readLine(); //should be a blank line between examples
				
				hand.add(clubs); hand.add(diamonds); 
				hand.add(hearts); hand.add(spades);
				res.put(new GameState(hand, null), bid);
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("corrupt file (number of lines)");
		} catch (IndexOutOfBoundsException e) {
			System.err.println("corrupt file (format of bid)");
		}
		
		return res;
	}

	private Bid getBid(String line) {
		line = line.trim();
		String[] b = line.split(" ");
		return BIDS[Integer.parseInt(b[0])][Integer.parseInt(b[1])];
	}

	private void writeSuit(List<Integer> clubs, String line) {
		line = line.trim();
		String[] c = line.split(" ");
		for (String card : c){
			clubs.add(Integer.parseInt(card));
		}
	}

}
