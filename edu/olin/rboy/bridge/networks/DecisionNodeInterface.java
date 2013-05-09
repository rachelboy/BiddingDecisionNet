package edu.olin.rboy.bridge.networks;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import edu.olin.rboy.bridge.Bidable;
import edu.olin.rboy.bridge.GameState;
import edu.olin.rboy.bridge.constraints.Constraint;

/**Interface for all nodes in the decision network
 * used for bidding.
 * 
 * @author rboy
 *
 */
public interface DecisionNodeInterface extends Bidable<DecisionNodeInterface>{
	
	/**Determine if a state satisfies all the constraints to
	 * be a member of the node.
	 * 
	 * @param state
	 * @return
	 */
	public boolean satisfiesConstraints(GameState state);
	
	Set<Constraint> getConstraints();

	/**Get some of the children - regular
	 * and else is an artifact of the original paper
	 * and the first half of my implementation.
	 * @return
	 */
	Set<DecisionNodeInterface> getRegularChildren();

	/**Get some of the children - regular
	 * and else is an artifact of the original paper
	 * and the first half of my implementation.
	 * @return
	 */
	Set<DecisionNodeInterface> getElseChildren();
	
	public void addChildren(Collection<DecisionNodeInterface> children);

}
