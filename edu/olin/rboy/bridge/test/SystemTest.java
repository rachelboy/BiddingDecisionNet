package edu.olin.rboy.bridge.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.Bidder;
import edu.olin.rboy.bridge.BridgeConstants;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.ID3;
import edu.olin.rboy.bridge.Learner;
import edu.olin.rboy.bridge.Util;
import edu.olin.rboy.bridge.constraints.BalancedConstraint;
import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.HCPConstraint;
import edu.olin.rboy.bridge.constraints.SuitLengthConstraint;
import edu.olin.rboy.bridge.networks.DecisionNode;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.DecisionTree;
import edu.olin.rboy.bridge.networks.LearningNodeInterface;
import edu.olin.rboy.bridge.networks.LearningTree;

import org.junit.Test;

public class SystemTest implements BridgeConstants{

	@Test
	public void test() {
		DecisionTree startTree = new DecisionTree();
		{
			DecisionNodeInterface ntChild;
			DecisionNodeInterface hChild;
			
			{
				Set<Constraint> ntConstraints = new HashSet<Constraint>();
				ntConstraints.add(new BalancedConstraint(0,1));
				ntConstraints.add(new HCPConstraint(15,17));
				Set<Bid> ntActions = new HashSet<Bid>();
				ntActions.add(BIDS[0][NT]);
				ntChild = new DecisionNode(ntConstraints, ntActions);
			}
			
			{
				Set<Constraint> hConstraints = new HashSet<Constraint>();
				hConstraints.add(new SuitLengthConstraint(5,13,HEARTS));
				hConstraints.add(new HCPConstraint(11,23));
				Set<Bid> hActions = new HashSet<Bid>();
				hActions.add(BIDS[0][HEARTS]);
				hChild = new DecisionNode(hConstraints, hActions);
			}
			Collection<DecisionNodeInterface> children = new HashSet<DecisionNodeInterface>();
			children.add(hChild);
			children.add(ntChild);
			startTree.addChildren(children);
		}
		
		Learner learner = new Learner(startTree);
		GameState nt1;
		GameState nt2;
		GameState h1;
		
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{Q,A}));
			hand.add(Util.arrayToList(new Integer[]{2,3,K}));
			hand.add(Util.arrayToList(new Integer[]{4,5,6,7,8}));
			hand.add(Util.arrayToList(new Integer[]{2,K,A}));
			nt1 = new GameState(hand, null);
			learner.learn(nt1, BIDS[0][NT]);
		}
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{2,5,K}));
			hand.add(Util.arrayToList(new Integer[]{4,7,A}));
			hand.add(Util.arrayToList(new Integer[]{4,5,J,Q,A}));
			hand.add(Util.arrayToList(new Integer[]{2,K}));
			nt2 = new GameState(hand, null);
			learner.learn(nt2, BIDS[0][NT]);
		}
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{Q}));
			hand.add(Util.arrayToList(new Integer[]{2,3,Q,K}));
			hand.add(Util.arrayToList(new Integer[]{3,4,J,Q,K}));
			hand.add(Util.arrayToList(new Integer[]{2,6,Q}));
			h1 = new GameState(hand, null);
			learner.learn(h1, BIDS[0][HEARTS]);
		}

		DecisionTree endTree = new DecisionTree();
		ID3 id3 = new ID3();
		endTree.addChildren(id3.run(learner.getStrategy()));
		
		Bidder<DecisionNodeInterface> bidder = new Bidder<DecisionNodeInterface>(endTree);
		assertTrue(bidder.findBids(h1).contains(BIDS[0][HEARTS]));
		assertFalse(bidder.findBids(h1).contains(BIDS[0][NT]));
		assertFalse(bidder.findBids(nt1).contains(BIDS[0][HEARTS]));
		assertTrue(bidder.findBids(nt1).contains(BIDS[0][NT]));
		assertFalse(bidder.findBids(nt2).contains(BIDS[0][HEARTS]));
		assertTrue(bidder.findBids(nt2).contains(BIDS[0][NT]));
	}

}
