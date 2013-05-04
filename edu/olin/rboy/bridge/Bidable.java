package edu.olin.rboy.bridge;

import java.util.Set;

public interface Bidable<T> {
	
	public Set<Bid> getActions();
	
	public Set<T> getApplicableChildren(GameState state);

}
