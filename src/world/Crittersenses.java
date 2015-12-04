package world;


public class Crittersenses {
	/** returns the value of the hex ahead according to the spec
	 * Not quite sure what to do if ahead isn't on the map. Will look it up TODO
	 * @param c
	 * @param where
	 * @return
	 */
	public static int spacesahead(Critter c, int where){
		if (where <= 0){
			return c.getNumRep(c);
		}
		else{
			return pseudomove(c,where);
		}
	}
	
	/** It moves the critter without actually expending energy, then it uses nearby to sense
	 * and finally it moves the critter back. Note, this should only be used for positive values of where
	 * The critter may not examine itself using pseudomove.
	 */
	public static int pseudomove(Critter c, int where) {
		int [] coords = new int [] {c.row, c.col};
		for (int place = 0; place < where - 1; place ++){
			int [] newplace = Crittermethods.dircoords(c,true);
			c.row = newplace[0];
			c.col = newplace[1];
		}
		int returned = nearby(c, 0); //changed to 0 from c.direction based on the change in nearby
		c.row = coords[0]; c.col = coords[1];
		return returned;
		
	}
	
	/**returns the information on the specified hex. In accordance with the project spec
	 * the direction given is the direction relative to the Critters current direction. (See section 6).
	 * @param c
	 * @param direction
	 * @return
	 */
	public static int nearby(Critter c, int direction) {
		if (direction < 0) {
			direction = - direction;
		}
		direction = direction % 6;
		int remember = c.direction;
		c.direction = (c.direction + direction) % 6;
		int [] place = Crittermethods.dircoords(c,true);
		int then = c.w.getNumRep(place);
		if (then > 0 ){
			return ((Critter) c.w.getHex(place[0], place[1])).getNumRep(c);
		}
		c.direction = remember;
		return then;
	}
	
	
	/** implements the random according to the spec.*/
	public static int rando(Critter c, int bound) {
		return bound < 2 ? 0 : c.r.nextInt(bound);
	}
	public static int smell(Critter critter) {
		Traverser t = new Traverser (critter.w);
		return foodSearch(t,critter);
	}

	private static int foodSearch(Traverser trav, Critter c) {
		int [] potents = new int [6];
		for (int it = 0; it < 6; it ++) {
			potents[it] = 100000;
		}
		Hex temp;
		for (int x = 0; x < 6; x ++ ) {
			c.direction = (c.direction + 1) % 6;
			int [] rowcol = Crittermethods.dircoords(c, true);
			temp = c.w.getHex(rowcol[0], rowcol[1]);
			temp.direct = c.direction;
			temp.distance = c.direction == 3 ? 3 : (c.direction) % 3;
			if (temp.getNumRep() < c.w.ROCK_VALUE) {
				System.out.println("here");
				potents[x] = temp.distance * 1000 + temp.direct;
			} else {
				temp.distance ++; //To get on the hex.
				temp = trav.traverse(temp);
				if (temp != null) {
					potents[x] = temp.distance * 1000 + temp.direct;
				}
			}

		}
		c.direction = (c.direction + 1) % 6;
		int min = 100000;
		for (int place = 0; place < 6; place ++) {
			if (potents[place] < min) {
				min = potents[place];
			}
			//System.out.println("potential " + place + ": " + potents[place]);
		}
		return min;
	}


	
}
