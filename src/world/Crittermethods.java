package world;

//TODO make sure this handles energy consumption correctly. Energy should probably be consumed
//BEFORE the action occurs.
public class Crittermethods {
	
	/** Effect: Critter's energy increases by the amount of food in front of him in the following way
	 * 	1. When there is more food than the critter can consume, critter leaves extra food on the hex
	 * 	2. Critter consumes all the food on the hex when he can.
	 * @param c
	 */
	public static void consume(Critter c) {
		c.mem[4] -= c.mem[3];
		if (!death(c)) {
			int n = c.w.getNumRep(dircoords(c,true));
			if (n < c.w.ROCK_VALUE) {
				if (c.w.ENERGY_PER_SIZE * c.mem[3] < c.mem [4] + (c.w.ROCK_VALUE-n)) { //too much food on the hex to be fully consumed
					c.w.putFood((c.w.ROCK_VALUE-n) - (c.w.ENERGY_PER_SIZE * c.mem[3] - c.mem[4]), dircoords(c,true));
					c.mem[4] = c.w.ENERGY_PER_SIZE * c.mem[3];
				}
				else{
					c.mem[4] += (c.w.ROCK_VALUE - n);
					int [] place = dircoords(c,true);
					c.w.setHex(place[0], place[1], new Food());//(dircoords(c,true));
				}
			}
		}
	}

	/**Updates the energy of the attacked critter.
	 * @param attacker
	 * @param victim
	 */
	public static void attack(Critter attacker){
		attacker.mem[4] -= attacker.mem[3] * attacker.w.ATTACK_COST;
		if (!death(attacker)) {
			int [] c = dircoords(attacker, true);
			Hex victim = attacker.w.getHex(c[0], c[1]);
			if (victim instanceof Critter) {
				Critter v = (Critter) victim;
				double inside = attacker.w.DAMAGE_INC * (attacker.mem[3] * attacker.mem[2] - v.mem[3] * v.mem[1]);
				float damage = (float) (attacker.w.BASE_DAMAGE * attacker.mem[3] * pfunct(inside));
				int harm = Math.round(damage);
				v.mem[4] -= harm;
				if (v.mem[4] <= 0) {
					dies(v);
				}
			}
		}
	}
	
	/** used in calculating the attack damage*/
	private static double pfunct(double val){
		return 1/(1 + Math.exp(-val));
	}
	
	
	/** moves the critter
	 * Invariant: direction is between 0 and 5 inclusive*/
	public static void movement(Critter c, boolean forward) {
		c.mem[4] -= c.mem[3] * c.w.MOVE_COST;
		if (!death(c)) {
			int [] newplace = dircoords(c,forward);
			if (checkempty(c, forward)) {
				c.w.swap(c, c.w.getHex(newplace[0], newplace[1]));
			}
		}
	}
	
	
	/**
	 * 
	 * @param c
	 * @param ahead -- whether we are looking ahead or behind the critter (needed for movement)
	 * @return the coordinates of the space ahead of the critter (based on its
	 * orientation.
	 */
	public static int [] dircoords(Critter c, boolean ahead){
		int dir = ahead ? 1 : -1;
		dir = c.direction >= 3 ? -dir : dir;
		int tempdir = c.direction % 3;
		int row = c.row;
		int col = c.col;
		if (tempdir < 2){
			row += dir;
		}
		if (tempdir == 1 || tempdir == 2){
			col += dir;
		}
		return new int [] {row, col};
		
	}
	
	
	/**Turns the critter left or right.
	 * I don't think any additional check is required.*/
	public static void turn(Critter c, boolean left) {
		c.direction += 6;
		c.direction = left ? c.direction - 1 : c.direction + 1;
		c.direction = c.direction % 6;
		c.mem[4] -= c.mem[3];
		death(c);
	}
	
	/**returns the integer result of the complexity formula
	 * 
	 * @param c
	 * @return
	 */
	public static int complexitycalc(Critter c){
		return (c.genes.children.length) * c.w.RULE_COST + (c.mem[1] + c.mem[2]) * c.w.ABILITY_COST;
	}
	
	/**Effect: performs the wait action on c. Does nothing if the
	 * critter is at maximum energy level.
	 * @param c
	 */
	public static void wait(Critter c){
		c.mem[4] += c.w.SOLAR_FLUX * c.mem[3];
		c.mem[4] = c.mem[4] > c.mem[3] * c.w.ENERGY_PER_SIZE ? c.mem[3] * c.w.ENERGY_PER_SIZE : c.mem[4];
	}
	
	
	/** Checks to see if the space one ahead if {@code ahead = true} in the critter's direction
	 * 	or one behind if {@code ahead = false} is empty.
	 * 
	 * @param c
	 * @param ahead
	 */
	public static boolean checkempty(Critter c, boolean ahead){
		if (c.w.isInGrid(dircoords(c,ahead)[0], dircoords(c,ahead)[1])) {
			return c.w.getNumRep(dircoords(c,ahead)) == 0;
		}
		return false;
	}
	
	private static boolean checkfood(Critter c, boolean ahead){
		if (c.w.isInGrid(dircoords(c,ahead)[0], dircoords(c,ahead)[1])) {
			return (c.w.getNumRep(dircoords(c,ahead)) < c.w.ROCK_VALUE || checkempty(c,ahead));
		}
		return false;
	}
	
	
	
	
	/**Effect: Generates a new food hex with the proper amount of food in the place where the 
	 * critter was when it died.
	 * @param c
	 */
	public static void dies(Critter c){
		c.w.replace(new Food(c.w.FOOD_PER_SIZE * c.mem[3]), c);
		c.w.crittersToRemove.add(c);
	}
	
	/** Critter grows one unit bigger. Critter can't get bigger than size 99*/
	public static void grow(Critter c) {
		c.mem[4] -= c.mem[3] * complexitycalc(c) * c.w.GROW_COST;
		if (!death(c)) {
			if (c.mem[3] < 99) {
				c.mem[3] ++;
			}
		}
	}
	
	
	/** A critters attempt to mate 
	 * unsuccessful mating*/
	public static void mate(Critter c) {
		ActionMate.matewith(c);
	}
	
	public static void asexual(Critter c) {
		ActionMate.alone(c);
	}

	/**handles serve. Note, Critter energy may go below 0 in this action */
	public static void serve(Critter c, int quantity) {
		c.mem[4] -= c.mem[3];
		if (!death(c)) { //I was tempted not to include this line, but I think a critter might be able to spawn a rock
			if (checkfood(c, true)) { //without it
				int amount = c.mem[4] >= quantity ? quantity : c.mem[4];
				Food f = (Food) c.w.getHex(dircoords(c,true)[0], dircoords(c,true)[1]);
				f.addFood(amount);
				//c.w.putFood(amount, dircoords(c,true));
			}
			c.mem[4] -= quantity;
			death(c);
		}

	}

	/**Effect: tags a critter directly ahead*/
	public static void tag(Critter c, int num) {
		int [] place = dircoords(c, true);
		c.mem[4] -= c.mem[3];
		if (!death(c)) {
			if (c.w.getNumRep(place) > 0 && num >= 0 && num <= 99) {
				Critter other = (Critter) c.w.getHex(place[0], place[1]);
				other.mem[6] = num;
			}
		}
	}
	
	/**Effect: the critter dies if its energy is at or below 0.
	 * Returns: True if the critter dies, false otherwise.
	 * @param c
	 * @return
	 */
	public static boolean death(Critter c) { 
		if (c.mem[4] <= 0) {
			c.dies();
			return true;
		}
		return false;
	}
	
	
}