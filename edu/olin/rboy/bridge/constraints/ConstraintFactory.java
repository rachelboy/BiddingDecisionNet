package edu.olin.rboy.bridge.constraints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.Bid;

public class ConstraintFactory {

	public static Set<Constraint> makeAllHandConstraints() {
		Set<Constraint> bah = new HashSet<Constraint>();
		bah.add(new AcesConstraint());
		bah.add(new ControlsConstraint());
		bah.add(new HCConstraint());
		bah.add(new HCPConstraint());
		bah.add(new HonorsConstraint());
		bah.add(new JacksConstraint());
		bah.add(new KingsConstraint());
		bah.add(new QueensConstraint());
		bah.add(new TensConstraint());
		for (int i=0; i<4; i++){
			bah.add(new AcesConstraint(i));
			bah.add(new ControlsConstraint(i));
			bah.add(new HCConstraint(i));
			bah.add(new HCPConstraint(i));
			bah.add(new HonorsConstraint(i));
			bah.add(new JacksConstraint(i));
			bah.add(new KingsConstraint(i));
			bah.add(new QueensConstraint(i));
			bah.add(new TensConstraint(i));
			bah.add(new RKCBConstraint(i));
			bah.add(new SuitLengthConstraint(i));
		}
		bah.add(new BalancedConstraint());
		bah.add(new SuitsConstraint());
		
		return bah;
	}
	
	/*Make a list of BiddingConstraints of a sequence of null bids from 1 to n bids long.
	 * 
	 */
	public static List<Constraint> makeNullBiddingConstraints(int n) {
		List<Bid> nullBids = new ArrayList<Bid>();
		List<Constraint> nullBiddingConstraints = new ArrayList<Constraint>();
		for (int i=0; i<n; i++){
			nullBids.add(null);
			nullBiddingConstraints.add(new BiddingConstraint(nullBids));
		}
		return nullBiddingConstraints;
	}

}
