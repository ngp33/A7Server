package world;

import java.util.ArrayList;

import RequestHandler.HexUpdate;

public class Rulehandler {
	
	/**Effect: alters the critters instance variables according to its rules
	 * Invariant: c.mem[5] = 1*/ //TODO make sure it performs a wait after all other rule evaluations if no action selected
	public static void altercritter(Critter c, ArrayList<HexUpdate> updateLogEntry){
		boolean finished = false;
		while (c.mem[5] <= c.w.MAX_RULES_PER_TURN && !(finished = evaluateprogram(c, updateLogEntry))){
			c.mem[5] ++;
		}
		if (!finished) {
			c.waiting();
		}
		c.mem[5] = 1;
	}
	
	
	/**Effect: Evaluates the program of a critter.
	 * Returns true if the rule committed terminates the rule evaluation phase (ie. it commits an action).
	 * @param c
	 * @return
	 */
	private static boolean evaluateprogram(Critter c, ArrayList<HexUpdate> updateLogEntry){
		return c.genes.eval(c, updateLogEntry);
	}


}
