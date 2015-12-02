package ast;

import ast.Action.Hamlet;
import world.Critter;
import world.Hex;

import java.math.*;
import java.util.ArrayList;

import RequestHandler.HexUpdate;

public class Actionpacked {
	
	/** Effect: commits the action and updates fields (mostly energy, for the critter which
	 * commits the action
	 * @param c
	 * @param type
	 */
	public static void themove(Critter c, Hamlet type, ArrayList<HexUpdate> updateLogEntry){
		switch(type){
		case attack:
			c.attack(updateLogEntry);
			break;
		case backward:
			c.movement(false, updateLogEntry);
			break;
		case bud:
			c.bud(updateLogEntry);
			break;
		case eat:
			c.consume(updateLogEntry);
			break;
		case forward:
			c.movement(true, updateLogEntry);
			break;
		case grow:
			c.grow(updateLogEntry);
			break;
		case left:
			c.turn(true, updateLogEntry);
			break;
		case mate: 
			c.mate(updateLogEntry);
			break;
		case right:
			c.turn(false, updateLogEntry);
			break;
		case wait:
			c.waiting();
			break;
		default:
			break;
		
		}
	}
}
