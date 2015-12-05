package world;

import java.util.Comparator;
import java.util.PriorityQueue;

/**Used to traverse the rest of the world for food given a starting coordinate, 
 * a starting direction, and a number of turns used thus far.
 * @author rafaelhaber
 *
 */
public class Traverser {
	
	PriorityQueue<Hex> pq;
	int turns;
	World w;
	Food lowestFood; //lowest food is the foodhex with the lowest distance encountered thus far
	int maxdistance;

	public Traverser(World w) {
		this.w = w;
		pq = new PriorityQueue<Hex>(60, new comp());
	}
	
	/**Traverses the world for a row, col, dir, turns. Invariants--hexes start with -1 distance,
	 * and the lowestFood field is null and pq is empty
	 * @param row	The starting row
	 * @param col	The starting col
	 * @param dir	The starting direction
	 * @param turns	The number of turns already executed when traverse is called.
	 * @return	The closest food hex found.
	 */
	public Food traverse(Hex h) {
		int dist = h.distance;
		reset();
		h.distance = dist; //avoid clearing due to the reset
		pq.offer(h);
		while (!pq.isEmpty()) {
			Hex info = pq.remove();
			getsurrounding(1,0,0,info);
			getsurrounding(1,1,1,info);
			getsurrounding(0,1,2,info);
			getsurrounding(-1,0,3,info);
			getsurrounding(-1,-1,4,info);
			getsurrounding(0,-1,5,info);
		}
		return lowestFood;
	}


	/**Clears values so that the invariants for food traverse are met*/
	private void reset() {
		w.clearHexDist();
		lowestFood = null;
		maxdistance = w.MAX_SMELL_DISTANCE;
		pq.clear();
	}


	/*private void labelAhead(int row, int col, int dir, int turns) {
		dir = dir  >= 3 ? -dir : dir;
		int tempdir = dir % 3;
		if (tempdir < 2){
			row += dir;
		}
		if (tempdir == 1 || tempdir == 2){
			col += dir;
		}
		Hex h = w.getHex(row, col);
		if (h.distance > turns) {
			h.distance = turns;
		}
		if (h.getNumRep() < w.ROCK_VALUE) {
			if (lowestFood == null) {
				lowestFood = (Food) h;
			}
			else if (lowestFood.distance > h.distance) {
				lowestFood = (Food) h;
			}
		}
	}*/
	
	
	/**Note, this is inconsistent with equals I think*/
	private class comp implements Comparator<Hex> {

		@Override
		public int compare(Hex o1, Hex o2) {
			return o1.distance - o2.distance;
		}

		
	}
	
	
	private void getsurrounding(int drow, int dcol, int dir, Hex n) {
		Hex h = w.getHex(n.row + drow, n.col + dcol);

		if (h.getNumRep() == 0 || h.getNumRep() < w.ROCK_VALUE) {
			if (h.distance == -1) {
				h.distance = n.distance + rotations(n, dir) + 1;
				// +1 because n.distance is distance to face n
				h.direct = dir;
				if (h.getNumRep() == 0 && h.distance < maxdistance) {
					pq.offer(h);
				}
			} else {
				//h.distance = min(n.distance + rotations(n, dir) + 1, h.distance);
				int plaus = n.distance + rotations(n, dir) + 1;
				if (h.distance > plaus) {
					h.distance = plaus;
					h.direct = dir;
					if (h.getNumRep() == 0) {
						pq.remove(h);
						pq.offer(h);
					}
				}
			}
			if (h.getNumRep() < w.ROCK_VALUE) {
				Food f = (Food) h;
				if (lowestFood == null) {
					lowestFood = f;
				}
				else if (lowestFood.distance > h.distance) {
					lowestFood = f;
				}
			}
		}
	}
	
	private int min(int i, int distance) {
		
		return i <= distance ? i : distance;
	}

	/**returns the number of turns required to rotate the critter so its
	 * facing a certain direction
	 * @param z
	 * @param dir
	 * @return
	 */
	private int rotations(Hex z, int dir) {
		int dif = z.direct - dir > 0 ? z.direct - dir : dir - z.direct;
		return dif > 3 ? dif * 2 % 3 : dif;
		//return dif < 0 ? (dif * - 1) % 3 : dif % 3;
	}


}
