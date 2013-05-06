package edu.olin.rboy.bridge.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.BridgeConstants;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.ID3;
import edu.olin.rboy.bridge.constraints.AcesConstraint;
import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.HCConstraint;
import edu.olin.rboy.bridge.constraints.SuitLengthConstraint;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.LearningNode;
import edu.olin.rboy.bridge.networks.LearningNodeInterface;
import edu.olin.rboy.bridge.networks.LearningTree;

public class ID3Test implements BridgeConstants{
	
	private GameState bidHearts;
	private GameState bidNT;
	private GameState NTorH;


	@Before
	public void setUp() {
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
	public void testSingleAction() {
		List<Constraint> constraints = new ArrayList<Constraint>();
		constraints.add(new SuitLengthConstraint(4,13,HEARTS));
		LearningNodeInterface node = new LearningNode(constraints);
		node.addLearningInstance(bidHearts, BIDS[0][HEARTS]);
		
		ID3 id3 = new ID3();
		
		List<DecisionNodeInterface> res = new ArrayList(id3.run(node));
		assertTrue(res.get(0).satisfiesConstraints(bidHearts));
		assertFalse(res.get(0).satisfiesConstraints(bidNT));
		assertTrue(res.get(0).getActions().contains(BIDS[0][HEARTS]));
		assertTrue(res.get(0).getActions().size() == 1);
	}
	
	@Test
	public void testTwoActions() {
		List<Constraint> constraints = new ArrayList<Constraint>();
		constraints.add(new SuitLengthConstraint(3,13,HEARTS));
		LearningNodeInterface node = new LearningNode(constraints);
		node.addLearningInstance(bidHearts, BIDS[0][HEARTS]);
		node.addLearningInstance(bidNT, BIDS[0][NT]);
		
		ID3 id3 = new ID3();
		
		List<DecisionNodeInterface> res = new ArrayList(id3.run(node));
		assertTrue(res.size() == 2);
		
		DecisionNodeInterface n1 = res.get(0);
		DecisionNodeInterface n2 = res.get(1);
		assertTrue(n1.getActions().size() == 1);
		assertTrue(n2.getActions().size() == 1);
		assertFalse(n1.satisfiesConstraints(bidHearts) && n1.satisfiesConstraints(NTorH));
		assertFalse(n2.satisfiesConstraints(bidHearts) && n2.satisfiesConstraints(NTorH));
		assertTrue(n1.satisfiesConstraints(bidHearts) || n1.satisfiesConstraints(NTorH));
		assertTrue(n2.satisfiesConstraints(bidHearts) || n2.satisfiesConstraints(NTorH));
		assertFalse(n1.satisfiesConstraints(NTorH) && n2.satisfiesConstraints(NTorH));
		
		if (n1.satisfiesConstraints(bidHearts)) {
			assertTrue(n1.getActions().contains(BIDS[0][HEARTS]));
			assertTrue(n2.getActions().contains(BIDS[0][NT]));
		}
		else {
			assertTrue(n2.getActions().contains(BIDS[0][HEARTS]));
			assertTrue(n1.getActions().contains(BIDS[0][NT]));
		}
		
	}
	
	@Test
	public void testFindActionEntropy() {
		Constraint testConstraint = new AcesConstraint();
		Map<Bid,Set<GameState>> stateSet = new HashMap<Bid,Set<GameState>>();
		
		Set<GameState> temp = new HashSet<GameState>();
		temp.add(bidHearts);
		stateSet.put(BIDS[0][HEARTS], temp);
		assertTrue(ID3.findActionEntropy(testConstraint, stateSet) == 0);
		
		temp = new HashSet<GameState>();
		temp.add(bidNT);
		stateSet.put(BIDS[0][NT], temp);
		Float val = ID3.findActionEntropy(testConstraint, stateSet);
		assertTrue(val > -1*Math.log(.5)-.000001f);
		assertTrue(val < -1*Math.log(.5)+.000001f);
		
		temp.add(NTorH);
		stateSet.put(BIDS[0][NT], temp);
		val = ID3.findActionEntropy(testConstraint, stateSet);
		Float oneThird = 1/3f;
		Float twoThird = 2/3f;
		Float ans = (float) ((-1*(oneThird)*Math.log(oneThird))-((twoThird)*Math.log(twoThird)));
		assertTrue(val > ans - .000001f);
		assertTrue(val < ans + .000001f);
		
	}
	
	@Test
	public void testMakeDivisions() {
		Constraint constraint = new HCConstraint();
		Map<Bid,Set<GameState>> learned = new HashMap<Bid,Set<GameState>>();
		Set<GameState> temp = new HashSet<GameState>();
		temp.add(bidHearts);
		learned.put(BIDS[0][HEARTS], temp);
		temp = new HashSet<GameState>();
		temp.add(bidNT);
		learned.put(BIDS[0][NT], temp);
		
		List<Constraint> res = ID3.makeDivisions(constraint, learned);
		assertTrue(res.size()==2);
		assertTrue(res.get(0).satisfiesConstraints(bidHearts));
		assertFalse(res.get(0).satisfiesConstraints(bidNT));
		assertTrue(res.get(1).satisfiesConstraints(bidNT));
		assertFalse(res.get(1).satisfiesConstraints(bidHearts));
	}

}
