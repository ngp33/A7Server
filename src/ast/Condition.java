package ast;

import world.Critter;

/**
 * An interface representing a Boolean condition in a critter program.
 *
 */
public interface Condition extends Node, mutation.Insertable {
	
	/**The idea is to return whether the condition is true or not*/
	Boolean getval(Critter c);
}
