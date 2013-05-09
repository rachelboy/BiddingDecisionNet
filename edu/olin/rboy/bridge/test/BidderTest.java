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

public class BidderTest implements BridgeConstants{
	GameState bidHearts;
	GameState bidNT;
	GameState NTorH;
	DecisionTree tree;
	DecisionNode ntNode;
	DecisionNode hNode;

	@Before
	public void setUp() throws Exception {
		{
			tree = new DecisionTree();
			
			Set<Constraint> constraintSet = new HashSet<Constraint>();
			constraintSet.add(new BalancedConstraint(0,1));
			constraintSet.add(new HCPConstraint(15,17));
			Set<Bid> bidSet = new HashSet<Bid>();
			bidSet.add(BIDS[NT][0]);
			ntNode = new DecisionNode(constraintSet, bidSet);
			
			constraintSet = new HashSet<Constraint>();
			constraintSet.add(new SuitLengthConstraint(5,13,HEARTS));
			constraintSet.add(new HCPConstraint(11,23));
			bidSet = new HashSet<Bid>();
			bidSet.add(BIDS[HEARTS][0]);
			hNode = new DecisionNode(constraintSet, bidSet);
			
			List<DecisionNodeInterface> children = new ArrayList<DecisionNodeInterface>();
			children.add(ntNode);
			children.add(hNode);
			tree.addChildren(children);
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
	}

	@Test
	public void testFindBids() {
		Bidder<DecisionNodeInterface> bidder = new Bidder<DecisionNodeInterface>(tree);
		Set<Bid> bids = bidder.findBids(bidHearts);
		assertTrue(bids.size() == 1);
		assertTrue(bids.contains(BIDS[HEARTS][0]));
		
		bids = bidder.findBids(bidNT);
		assertTrue(bids.size() == 1);
		assertTrue(bids.contains(BIDS[NT][0]));
		
		bids = bidder.findBids(NTorH);
		assertTrue(bids.size() == 2);
		assertTrue(bids.contains(BIDS[NT][0]));
		assertTrue(bids.contains(BIDS[HEARTS][0]));
		
	}
	
	@Test
	public void testFindMostSpecificNodes() {
		Bidder<DecisionNodeInterface> bidder = new Bidder<DecisionNodeInterface>(tree);
		
		Set<DecisionNodeInterface> nodes = bidder.findMostSpecifcNodes(bidHearts);
		assertTrue(nodes.size() == 1);
		assertTrue(nodes.contains(hNode));
		
		Set<Constraint> constraintSet = new HashSet<Constraint>();
		constraintSet.add(new SuitLengthConstraint(5,13,HEARTS));
		constraintSet.add(new BalancedConstraint(0,1));
		constraintSet.add(new HCPConstraint(15,17));
		Set<Bid> bidSet = new HashSet<Bid>();
		bidSet.add(BIDS[0][CLUBS]);
		DecisionNode cNode = new DecisionNode(constraintSet, bidSet);
		
		Set<DecisionNodeInterface> cSet = new HashSet<DecisionNodeInterface>();
		cSet.add(cNode);
		hNode.addChildren(cSet);
		ntNode.addChildren(cSet);
		
		nodes = bidder.findMostSpecifcNodes(NTorH);
		assertTrue(nodes.size() == 1);
		assertFalse(nodes.contains(hNode));
		assertFalse(nodes.contains(ntNode));
		assertTrue(nodes.contains(cNode));
		
	}

}
