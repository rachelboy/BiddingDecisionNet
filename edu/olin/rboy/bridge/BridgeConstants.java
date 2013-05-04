package edu.olin.rboy.bridge;

import java.util.Set;

import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.ConstraintFactory;

public interface BridgeConstants {
	
	int CLUBS = 0;
	int DIAMONDS = 1;
	int HEARTS = 2;
	int SPADES = 3;
	int NT = 4;
	
	int A = 14;
	int K = 13;
	int Q = 12;
	int J = 11;
	
	Bid[] HeartsBids = {new Bid(1,HEARTS), new Bid(2,HEARTS), new Bid(3,HEARTS), new Bid(4,HEARTS),
			new Bid(5,HEARTS), new Bid(6,HEARTS), new Bid(7,HEARTS)};
	Bid[] SpadesBids = {new Bid(1,SPADES), new Bid(2,SPADES), new Bid(3,SPADES), new Bid(4,SPADES),
			new Bid(5,SPADES), new Bid(6,SPADES), new Bid(7,SPADES)};
	Bid[] DiamondsBids = {new Bid(1,DIAMONDS), new Bid(2,DIAMONDS), new Bid(3,DIAMONDS), 
			new Bid(4,DIAMONDS), new Bid(5,DIAMONDS), new Bid(6,DIAMONDS), new Bid(7,DIAMONDS)};
	Bid[] ClubsBids = {new Bid(1,CLUBS), new Bid(2,CLUBS), new Bid(3,CLUBS), 
			new Bid(4,CLUBS), new Bid(5,CLUBS), new Bid(6,CLUBS), new Bid(7,CLUBS)};
	Bid[][] BIDS = {ClubsBids, DiamondsBids, HeartsBids, SpadesBids};
	Bid PASS = new Bid(0,0);
	
	Set<Constraint> allHandConstraints = ConstraintFactory.makeAllHandConstraints();

}
