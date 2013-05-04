package edu.olin.rboy.bridge.constraints;

import java.util.List;
import java.util.Set;

import edu.olin.rboy.bridge.Bid;
import edu.olin.rboy.bridge.GameState;

public class BiddingConstraint implements Constraint{
	
	List<Bid> biddingSequence;
	
	public BiddingConstraint(List<Bid> biddingSequence) {
		this.biddingSequence = biddingSequence;
	}

	@Override
	public boolean satisfiesConstraints(GameState state) {
		List<Bid> actual = state.getBidding();
		if (biddingSequence.size() < actual.size()) {
			return false;
		}
		int i = 0;
		for(Bid bid : actual) {
			if (bid == null) {
				i++;
				continue;
			}
			if (bid != biddingSequence.get(i)) {
				return false;
			}
			i++;
		}
		return true;
	}

	@Override
	public String hash() {
		return new StringBuilder("BiddingConstraint").append(biddingSequence.size()).toString();
	}
	
	public List<Bid> getValue(GameState state){
		return state.getBidding();
	}

	@Override
	public Set<Set<Constraint>> getPossDivisions(int i) {
		// TODO Auto-generated method stub
		return null;
	}



}
