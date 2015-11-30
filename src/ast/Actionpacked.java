package ast;

import ast.Action.Hamlet;
import world.Critter;
import world.Hex;

import java.math.*;

public class Actionpacked {
	
	/** Effect: commits the action and updates fields (mostly energy, for the critter which
	 * commits the action
	 * @param c
	 * @param type
	 */
	public static void themove(Critter c, Hamlet type){
		switch(type){
		case attack:
			c.attack();
			break;
		case backward:
			c.movement(false);
			break;
		case bud:
			c.bud();
			break;
		case eat:
			c.consume();
			break;
		case forward:
			c.movement(true);
			break;
		case grow:
			c.grow();
			break;
		case left:
			c.turn(true);
			break;
		case mate: 
			c.mate();
			break;
		case right:
			c.turn(false);
			break;
		case wait:
			c.waiting();
			break;
		default:
			break;
		
		}
	}
}
