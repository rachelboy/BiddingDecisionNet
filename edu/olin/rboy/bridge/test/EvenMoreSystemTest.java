package edu.olin.rboy.bridge.test;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.Bidder;
import edu.olin.rboy.bridge.BridgeConstants;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.ID3;
import edu.olin.rboy.bridge.Learner;
import edu.olin.rboy.bridge.Util;
import edu.olin.rboy.bridge.networks.DecisionNodeInterface;
import edu.olin.rboy.bridge.networks.DecisionTree;


/**Not really a "unit" test, but whatever.
 * Test the entire system!!!
 * 
 */
public class EvenMoreSystemTest implements BridgeConstants {
	GameState bidSpades;
	GameState pass;
	private GameState bidHearts;
	private GameState bidDiamonds;
	private GameState bidClubs;

	@Before
	public void setUp() throws Exception {
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{Q,A}));
			hand.add(Util.arrayToList(new Integer[]{2,3,K}));
			hand.add(Util.arrayToList(new Integer[]{4,7,8}));
			hand.add(Util.arrayToList(new Integer[]{2,5,6,K,A}));
			bidSpades = new GameState(hand, null);
		}
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{4,A}));
			hand.add(Util.arrayToList(new Integer[]{2,3,10}));
			hand.add(Util.arrayToList(new Integer[]{4,7,8}));
			hand.add(Util.arrayToList(new Integer[]{2,5,6,9,J}));
			pass = new GameState(hand, null);
		}
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{Q,A}));
			hand.add(Util.arrayToList(new Integer[]{2,3,K}));
			hand.add(Util.arrayToList(new Integer[]{4,7,8,K,A}));
			hand.add(Util.arrayToList(new Integer[]{2,5,6}));
			bidHearts = new GameState(hand,new LinkedList<Bid>());
		}
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{Q,A}));
			hand.add(Util.arrayToList(new Integer[]{2,3,6,K,A}));
			hand.add(Util.arrayToList(new Integer[]{4,7,8}));
			hand.add(Util.arrayToList(new Integer[]{2,5,6}));
			bidDiamonds = new GameState(hand,new LinkedList<Bid>());
		}
		{
			List<List<Integer>> hand = new ArrayList<List<Integer>>();
			hand.add(Util.arrayToList(new Integer[]{2,5,Q,K,A}));
			hand.add(Util.arrayToList(new Integer[]{2,3,K}));
			hand.add(Util.arrayToList(new Integer[]{4,7,8}));
			hand.add(Util.arrayToList(new Integer[]{2,A}));
			bidClubs = new GameState(hand,new LinkedList<Bid>());
		}
	}

	/**Test learning pass and typical one of a suit bids.
	 * 
	 */
	@Test
	public void test() {
		Learner learner = new Learner(new DecisionTree());
		Map<GameState, Bid> examples = null;
		try {
			examples = learner.getExamples("/home/rboy/AI/BridgeBidding/src/P1S.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (GameState state : examples.keySet()){
			learner.learn(state, examples.get(state));
		}
		
		ID3 id3 = new ID3();
		DecisionTree learnedTree = new DecisionTree();
		learnedTree.addChildren(id3.run(learner.getStrategy()));
		
		Bidder<DecisionNodeInterface> bidder = new Bidder<DecisionNodeInterface>(learnedTree);
		assertTrue(bidder.findBids(bidSpades).size() == 1);
		assertTrue(bidder.findBids(bidSpades).contains(BIDS[SPADES][0]));
		assertTrue(bidder.findBids(pass).size() == 1);
		assertTrue(bidder.findBids(pass).contains(PASS));
		
		learner = new Learner(learnedTree);
		examples = null;
		try {
			examples = learner.getExamples("/home/rboy/AI/BridgeBidding/src/P1Suit.data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (GameState state : examples.keySet()){
			learner.learn(state, examples.get(state));
		}
		
		learnedTree = new DecisionTree();
		learnedTree.addChildren(id3.run(learner.getStrategy()));
		
		bidder = new Bidder<DecisionNodeInterface>(learnedTree);
		assertTrue(bidder.findBids(bidSpades).contains(BIDS[SPADES][0]));
		System.out.println("spades");
		for (Bid bid : bidder.findBids(bidSpades)){
			System.out.println(bid.getSuit());
		}
		assertTrue(bidder.findBids(pass).contains(PASS));
		System.out.println("pass");
		for (Bid bid : bidder.findBids(pass)){
			System.out.println(bid.getSuit());
		}
		assertTrue(bidder.findBids(bidHearts).contains(BIDS[HEARTS][0]));
		System.out.println("hearts");
		for (Bid bid : bidder.findBids(bidHearts)){
			System.out.println(bid.getSuit());
		}
		assertTrue(bidder.findBids(bidDiamonds).contains(BIDS[DIAMONDS][0]));
		System.out.println("diamonds");
		for (Bid bid : bidder.findBids(bidDiamonds)){
			System.out.println(bid.getSuit());
		}
		assertTrue(bidder.findBids(bidClubs).contains(BIDS[CLUBS][0]));
		System.out.println("clubs");
		for (Bid bid : bidder.findBids(bidClubs)){
			System.out.println(bid.getSuit());
		}
		
	}

}
