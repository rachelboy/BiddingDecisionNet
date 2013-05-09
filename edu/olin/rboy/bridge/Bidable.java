package edu.olin.rboy.bridge;

import java.util.Set;


/**Interface to be used with Bidder.
 * 
 * @author rboy
 *
 * @param <T>
 */
public interface Bidable<T> {
	
	public Set<Bid> getActions();
	
	public Set<T> getApplicableChildren(GameState state);

}
