package edu.olin.rboy.bridge.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.Bidder;
import edu.olin.rboy.bridge.BridgeConstants;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.Learner;
import edu.olin.rboy.bridge.constraints.BalancedConstraint;
import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.HCPConstraint;
import edu.olin.rboy.bridge.constraints.SuitLengthConstraint;
import edu.olin.rboy.bridge.networks.DecisionNode;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.DecisionTree;
import edu.olin.rboy.bridge.networks.LearningNodeInterface;
import edu.olin.rboy.bridge.networks.LearningTree;

public class LearnerTest implements BridgeConstants{
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
	public void testLearn() {

		Learner learner = new Learner(tree);
		learner.learn(NTorH, BIDS[CLUBS][0]);
		Bidder<LearningNodeInterface> bidder = new Bidder<LearningNodeInterface>(learner.getStrategy());
		Set<LearningNodeInterface> children = bidder.findMostSpecifcNodes(NTorH);
		assertTrue(children.size() == 1);
		Set<Bid> bids = bidder.findBids(NTorH);
		assertTrue(bids.size() == 1);
		assertTrue(bids.contains(BIDS[CLUBS][0]));
	}

	@Test
	public void testGetExamples() {
		Learner learner = new Learner(new DecisionTree());
		Map<GameState, Bid> data = null;
		try {
			data = learner.getExamples("/home/rboy/AI/BridgeBidding/src/edu/olin/rboy/bridge/test/example.data");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(data.size() == 2);
		for (GameState state : data.keySet()) {
			int clubsLen = state.getHand().get(0).size();
			assertTrue(clubsLen == 3 || clubsLen == 2);
			if (clubsLen == 3){
				assertTrue(data.get(state) == BIDS[DIAMONDS][0]);
			}
			else {
				assertTrue(data.get(state) == BIDS[SPADES][0]);
			}
		}
		
	}
}
