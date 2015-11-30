package world;

import java.util.Random;

import ast.ProgramImpl;
import ast.Rule;
import world.Critter;
import world.Hex;

public class ActionMate {
	
	
	/** A critters attempt to mate
	 * Creates: a new critter assuming mating is successful and places that critter somewhere on the
	 * 		world after mutating it
	 * Effect: the energy of the critters involved decrease by the proper amount depending on whether or not
	 * 		the mating was successful. An unsuccessful mating constitutes any mating where a child is not formed.
	 * 		Thus, if a critter has enough energy to attempt a mate, but not to perform it, the critter doesn't die*/
	public static void matewith(Critter c) {
		c.mem[4] -= c.mem[3];//put an if not death line here
		if (!Crittermethods.death(c)) {
			c.matingdance = true;
			Hex there = c.w.getHex(Crittermethods.dircoords(c,true)[0], Crittermethods.dircoords(c,true)[1]);
			if (there instanceof Critter) {
				Critter specific = (Critter) there;
				success(c,specific);
			}
		}
	}
	
	
	/** describes what happens when there is another critter for c to mate with.
	 * Effect: Creates a new critter and places it in the world after mutating it.
	 * @param c
	 * @param specific
	 */
	private static void success(Critter c, Critter specific) {
		if (specific.matingdance) {
			if (Crittermethods.checkempty(c, false) || Crittermethods.checkempty(specific, false)) {
				int one = c.mem[4] + c.mem[3] - c.w.MATE_COST * Crittermethods.complexitycalc(c);
				int two = specific.mem[4] + specific.mem[3] - specific.w.MATE_COST * Crittermethods.complexitycalc(specific);
				if (one > 0 && two > 0) { //one & two are the hypothetical energy quantities of the critters
					c.mem[4] = one;
					specific.mem[4] = two;
					Critter baby = makenewcritter(c, specific);
					mutate(baby);
					place(baby,c, specific);
					baby.name = "childof(" + c.name + ", " + specific.name + ")"; 
					c.w.addMidStep(baby);
				}
			}
		}
	}

	/** finds an open hex to place the baby critter and places it there
	 * Effect: places the baby critter in the world if there is space, does nothing otherwise
	 * Returns: true if the baby is successfully placed.
	 * Invariant: There exists an empty place behind one of the two parents to put the new critter
	 */
	private static void place(Critter baby, Critter c, Critter k) {
		Critter firstpar = c;
		Critter secondpar = k;
		if (c.r.nextBoolean()) {
			firstpar = k;
			secondpar = c;
		}
		if (babysit(baby, firstpar)) {}
		else {babysit(baby,secondpar);}
	}
	
	/**Effect: places a baby if the space behind parent is empty. Does nothing otherwise
	 * Returns: true if the baby was placed, false otherwise.
	 * @param baby
	 * @param parent
	 * @return
	 */
	private static boolean babysit(Critter baby, Critter parent) {
		if (Crittermethods.checkempty(parent, false)) {
			int [] p = Crittermethods.dircoords(parent, false);
			baby.w.replace(baby, baby.w.getHex(p[0], p[1]));
			return true;
		}
		return false;
	}

	/** makes a baby critter. Right now I choose a parent and then copy attibutes 0-2 from it TODO 
	 * make sure this is right...maybe?
	 * Effects: initiates the babycritter without mutations.*/
	private static Critter makenewcritter(Critter c, Critter specific) {
		Critter inqu = c.r.nextBoolean() ? c : specific;
		int [] m = makemem(inqu);
		ProgramImpl ruleblend = findrules (c,specific);
		return new Critter(m, c.r, ruleblend, c.w);
	}
	
	
	/** finds the rule blend for mating. Make sure it works when the mating rule list is of the same length
	 * Returns: the programImpl generating by mixing the rules of the critter parents according to the spec*/
	private static ProgramImpl findrules(Critter c, Critter specific) {
		Rule [] bigger;
		Rule [] smaller;
		Rule [] rules;
		if (c.genes.children.length > specific.genes.children.length) {
			bigger = (Rule []) c.genes.children;
			smaller = (Rule []) specific.genes.children;
		}
		else {
			smaller = (Rule []) c.genes.children;
			bigger = (Rule []) specific.genes.children;
		}
		if (c.r.nextBoolean()) {
			rules = new Rule[bigger.length];
			fillinalternating(smaller, bigger, rules, c.r);
			complete(bigger, rules, smaller.length);
		}
		else {
			rules = new Rule[smaller.length];
			fillinalternating(smaller, bigger, rules, c.r);
		}
		return new ProgramImpl(rules);
	}

	/**Effect: Copies rules from the bigger array past index length to the added []*/
	private static void complete(Rule[] bigger, Rule [] added, int length) {
		for (int place = length; place < bigger.length; place ++) {
			added[place] = (Rule) bigger[place].copy();
		}
		
	}

	/**Effect: Adds rules to added up to smaller.length*/
	private static void fillinalternating(Rule[] smaller, Rule[] bigger, Rule [] added, Random r) {
		for (int place = 0; place < smaller.length; place ++) {
			added[place] = r.nextBoolean() ? (Rule) smaller[place].copy() : (Rule) bigger[place].copy();
		}
		
	}


	/**Sets up the mem array for the child critter*/
	private static int[] makemem(Critter c) {
		int [] th = new int [c.mem[0]];
		th[0] = c.mem[0];
		th[1] = c.mem[1];
		th[2] = c.mem[2];
		finishsetup(th, c);
		return th;
	}
	
	/**Effect: sets up the common mem fields of budding and mating */
	private static void finishsetup (int [] mem, Critter c) {
		mem[3] = 1;
		mem[4] = c.w.INITIAL_ENERGY;
		mem[5] = 1;
		mem[6] = 0;
		mem[7] = 0;
		for (int place = 8; place < mem[0]; place ++) {
			mem[place] = 0;
		}
	}
	
	/** effect: mutates critter c's AST according to the spec.*/
	private static void mutate(Critter c) {
		
		String original = new StringBuilder(c.genes.toString()).toString();
		while (c.r.nextInt(4) == 1) {
			c.genes.mutate();
			while (original.toString().equals(c.genes.toString())) { //This is supposed to make sure that
				c.genes.mutate(); //A mutation actually occurs when the first loop guard is true.
			}
		}
	}
	
	
	/** creates and places a new critter in the world according to laws of budding*/
	public static void alone(Critter c) {
		c.mem[4] -= c.w.BUD_COST * Crittermethods.complexitycalc(c);
		if (!Crittermethods.death(c)) {
			ProgramImpl p = (ProgramImpl) c.genes.copy();
			int [] mem = new int [c.mem[0]];
			mem[0] = c.mem[0];
			mem[1] = c.mem[1];
			mem[2] = c.mem[2];
			finishsetup(mem, c);
			Critter k = new Critter(mem, c.r, p, c.w);
			k.name = "budof(" + c.name + ")";
			mutate(k);
			if (Crittermethods.checkempty(c, false)) {
				int [] loc = Crittermethods.dircoords(c, false);
				c.w.replace(k, c.w.getHex(loc[0], loc[1]));
				c.w.addMidStep(k);
			}
		}
	}
	
}
