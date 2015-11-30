package ast;

import world.Critter;

/**
 * A critter program expression that has an integer value.
 */
public interface Expr extends Node {
	
	/** returns the integral value of the node*/
	int value(Critter c);
	
	
}
