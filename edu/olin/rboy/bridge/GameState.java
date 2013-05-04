/**
 * 
 */
package edu.olin.rboy.bridge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author rboy
 *
 */
public class GameState {
	List<List<Integer>> hand;
	List<Bid> bidding;

	public GameState(List<List<Integer>> hand, List<Bid> bidding) {
		if (hand.size() == 4){
			this.hand = hand;
		}
		else {
			throw new RuntimeException("Not a valid hand");
		}
		this.bidding = bidding;
	}
	
	public GameState() {
		this(new ArrayList<List<Integer>>(4),new LinkedList<Bid>());
		newDeal();
	}

	/**
	 * 
	 */
	private void newDeal() {
		Random generator = new Random();
		for (int i=0; i<4; i++){
			hand.set(i, new ArrayList<Integer>());
		}
		for (int val=2; val<15; val++){
			for (int suit = 0; suit<4; suit++){
				if (cardsInHand() == 13){
					break;
				}
				if (generator.nextInt(4) == 0) {
					hand.get(suit).add(val);
				}
			}
		}
	}

	private int cardsInHand(){
		int tot = 0;
		for (List<Integer> suit : hand) {
			tot += suit.size();
		}
		return tot;
	}
	
	public void addBid(Bid bid) {
		bidding.add(0,bid);
	}
	
	public List<Bid> getBidding() {
		return bidding;
	}
	
	public List<List<Integer>> getHand() {
		return hand;
	}

}
