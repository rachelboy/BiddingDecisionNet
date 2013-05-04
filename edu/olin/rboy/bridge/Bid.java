/**
 * 
 */
package edu.olin.rboy.bridge;

/**
 * @author rboy
 *
 */
public class Bid implements BridgeConstants, Comparable<Bid>{
	
	int suit;
	int level;
	
	/*Any suit at level 0 is a pass.
	 * 
	 */
	public Bid(int level, int suit) {
		this.level = level;
		this.suit = suit;
	}
	
	public int getSuit(){
		return suit;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bid) {
			Bid that = (Bid) obj;
			return (that.suit == this.suit && that.level == this.level);
		}
		return false;
	}

	@Override
	public int compareTo(Bid that) {
		if (this.level == 0){
			return 1;
		}
		else if (that.level == 0){
			return -1;
		}
		else if (this.level == that.level) {
			return this.suit - that.suit;
		}
		return this.level - that.level;
	}

}
