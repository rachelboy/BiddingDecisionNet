package edu.olin.rboy.bridge.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.Bidder;
import edu.olin.rboy.bridge.BridgeConstants;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.constraints.BalancedConstraint;
import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.HCPConstraint;
import edu.olin.rboy.bridge.constraints.SuitLengthConstraint;
import edu.olin.rboy.bridge.networks.DecisionNode;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.DecisionTree;
import edu.olin.rboy.bridge.networks.LearningNode;
import edu.olin.rboy.bridge.networks.LearningNodeInterface;
import edu.olin.rboy.bridge.networks.LearningTree;

public class LearningTreeTest implements BridgeConstants{
	GameState bidHearts;
	GameState bidNT;
	GameState NTorH;
	LearningTree tree;
	DecisionNode ntNode;
	DecisionNode hNode;

	@Before
	public void setUp() throws Exception {
		DecisionTree dtree = new DecisionTree();
		{			
			Set<Constraint> constraintSet = new HashSet<Constraint>();
			constraintSet.add(new BalancedConstraint(0,1));
			constraintSet.add(new HCPConstraint(15,17));
			Set<Bid> bidSet = new HashSet<Bid>();
			bidSet.add(BIDS[0][NT]);
			ntNode = new DecisionNode(constraintSet, bidSet);
			
			constraintSet = new HashSet<Constraint>();
			constraintSet.add(new SuitLengthConstraint(5,13,HEARTS));
			constraintSet.add(new HCPConstraint(11,23));
			bidSet = new HashSet<Bid>();
			bidSet.add(BIDS[0][HEARTS]);
			hNode = new DecisionNode(constraintSet, bidSet);
			
			List<DecisionNodeInterface> children = new ArrayList<DecisionNodeInterface>();
			children.add(ntNode);
			children.add(hNode);
			dtree.addChildren(children);
		}
		
		{
			List<Integer> h = new ArrayList<Integer>();
			for (int i = 3; i<15; i++){
				h.add(i);
				}
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			List<Integer> c = new ArrayList<Integer>();
			List<Integer> d = new ArrayList<Integer>();
			d.add(14);
			hand.add(c);
			hand.add(d);
			hand.add(h);
			hand.add(c);
			bidHearts = new GameState(hand,new LinkedList<Bid>());
		}
		
		{
			List<Integer> h = new ArrayList<Integer>();
			List<Integer> s = new ArrayList<Integer>();
			List<Integer> d = new ArrayList<Integer>();
			List<Integer> c = new ArrayList<Integer>();
			for (int i = 10; i<13; i++) {
				h.add(i);
				s.add(i);
				d.add(i);
				c.add(i);
			}
			s.add(14);
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(c);
			hand.add(d);
			hand.add(h);
			hand.add(s);
			bidNT = new GameState(hand, new LinkedList<Bid>());
		}
		
		{
			List<Integer> h = new ArrayList<Integer>();
			List<Integer> s = new ArrayList<Integer>();
			List<Integer> d = new ArrayList<Integer>();
			List<Integer> c = new ArrayList<Integer>();
			for (int i = 11; i<13; i++) {
				h.add(i);
				s.add(i);
				d.add(i);
				c.add(i);
			}
			c.add(2);
			d.add(2);
			h.add(3);
			h.add(2);
			h.add(14);
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(c);
			hand.add(d);
			hand.add(h);
			hand.add(s);
			NTorH = new GameState(hand, new LinkedList<Bid>());
		}
		
		tree = new LearningTree(dtree);
	}

	@Test
	public void testSatisfiesConstraints() {
		assertTrue(tree.satisfiesConstraints(bidHearts));
		assertTrue(tree.satisfiesConstraints(bidNT));
		
		assertTrue(ntNode.satisfiesConstraints(bidNT));
		assertFalse(ntNode.satisfiesConstraints(bidHearts));
		assertTrue(hNode.satisfiesConstraints(bidHearts));
		assertFalse(hNode.satisfiesConstraints(bidNT));
	}

	@Test
	public void testGetApplicableChildren() {
		Set<LearningNodeInterface> children = tree.getApplicableChildren(bidHearts);
		assertTrue(children.size() == 1);
		for(LearningNodeInterface child : children) {
			assertTrue(child.satisfiesConstraints(bidHearts));
		}
		
		children = tree.getApplicableChildren(bidNT);
		assertTrue(children.size() == 1);
		for(LearningNodeInterface child : children) {
			assertTrue(child.satisfiesConstraints(bidNT));
		}
		
		children = tree.getApplicableChildren(NTorH);
		assertTrue(children.size() == 2);
		for(LearningNodeInterface child : children) {
			assertTrue(child.satisfiesConstraints(bidHearts) || child.satisfiesConstraints(bidNT));
		}
	}
	
	@Test
	public void testLearning() {
		LearningNode suspiciousClubs = new LearningNode(tree.getApplicableChildren(NTorH));
		suspiciousClubs.addLearningInstance(NTorH,BIDS[0][CLUBS]);
		for (LearningNodeInterface parent : tree.getApplicableChildren(NTorH)){
			parent.addChild(suspiciousClubs);
		}
		Bidder<LearningNodeInterface> bidder = new Bidder<LearningNodeInterface>(tree);
		Set<Bid> children = bidder.findBids(NTorH);
		assertTrue(children.size() == 1);
		assertTrue(children.contains(BIDS[0][CLUBS]));
	}

}
