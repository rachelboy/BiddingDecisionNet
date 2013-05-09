package edu.olin.rboy.bridge.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.BridgeConstants;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.constraints.BalancedConstraint;
import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.HCPConstraint;
import edu.olin.rboy.bridge.constraints.SuitLengthConstraint;
import edu.olin.rboy.bridge.networks.DecisionNode;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.DecisionTree;

public class DecisionTreeTest implements BridgeConstants{
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
			bidSet.add(BIDS[0][NT]);
			ntNode = new DecisionNode(constraintSet, bidSet);
			
			constraintSet = new HashSet<Constraint>();
			constraintSet.add(new SuitLengthConstraint(5,13,HEARTS));
			constraintSet.add(new HCPConstraint(11,23));
			bidSet = new HashSet<Bid>();
			bidSet.add(BIDS[HEARTS][0]);
			hNode = new DecisionNode(constraintSet, bidSet);
			
			Collection<DecisionNodeInterface> children = new HashSet<DecisionNodeInterface>();
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
	public void testSatisfiesConstraints() {
		assertTrue(tree.satisfiesConstraints(bidHearts));
		assertTrue(tree.satisfiesConstraints(bidNT));
		
		assertTrue(ntNode.satisfiesConstraints(bidNT));
		assertFalse(ntNode.satisfiesConstraints(bidHearts));
	}
	
	@Test
	public void testGetApplicableChildren() {
		Set<DecisionNodeInterface> children = tree.getApplicableChildren(bidHearts);
		assertTrue(children.size() == 1);
		assertTrue(children.contains(hNode));
		assertFalse(children.contains(ntNode));
		
		children = tree.getApplicableChildren(bidNT);
		assertTrue(children.size() == 1);
		assertTrue(children.contains(ntNode));
		assertFalse(children.contains(hNode));
		
		children = tree.getApplicableChildren(NTorH);
		assertTrue(children.size() == 2);
		assertTrue(children.contains(ntNode));
		assertTrue(children.contains(hNode));
		
	}
	
	@Test
	public void testAddChildren() {
		DecisionTree tree2 = new DecisionTree();
		DecisionNodeInterface node1 = new DecisionNode(null, null);
		Set<DecisionNodeInterface> children = new HashSet<DecisionNodeInterface>();
		children.add(node1);
		tree2.addChildren(children);
		assertTrue(tree2.getRegularChildren().size() == 1);
	}

}
