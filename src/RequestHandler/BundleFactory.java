package RequestHandler;

import java.util.Collection;
import java.util.Iterator;

import ast.Program;
import world.Critter;
import world.Food;
import world.Hex;
import world.World;

/**Creates a bundle factory which bundles information into specific classes*/
public class BundleFactory {
	World w;
	
	public BundleFactory(World w) {
		this.w = w;
	}

	
	/**Gets the world or the subsection of the world since some number of timesteps
	 * if the specifications are the world boundaries (0, w.rowmax, 0, w.colmax) this will
	 * return the dif for the whole world (hopefully)*/
	public worldBundle getWorldBundle(int rowinit, int rowfin, int colinit, int colfin, int ts) {
		return null;
		
	}
	
	//Used in getting the critterList I assume
//<<<<<<< HEAD
	public static class CritListBundle {
		Inhabitant [] cb;
		int place;
		public CritListBundle(World w) {
			Collection<Critter> critterList = w.critters.values();
			cb = new Inhabitant[critterList.size()];
			place = 0;
		}
		/**Effect: Adds an inhabitant to the critterlist*/
		public void addInhabitant(Inhabitant i) {
			cb[place] = i;
			place ++;
		}
	}
/*=======
	private class CritListBundle {
		Inhabitant [] cb;
		public CritListBundle() {
			Collection<Critter> critterList = w.critters.values();
			cb = new Inhabitant[critterList.size()];
			int i = 0;
			for (Critter c : critterList) {
				cb[i] = new Inhabitant(c);
			}
>>>>>>> origin/master
		}
	}*/
	
	//probably not necessary. An inhabitant constructor could probably do the same
	/*
	private class CritBundle extends Inhabitant {
		public CritBundle(int critID) {
			//TODO form the bundle based on the critter ID.
			Critter c= w.critters.get(critID);
			id = critID;
			row = c.row;
			col = c.col;
			species_id = c.name;
			direction = c.direction;
			mem = c.mem;
			String [] str = c.genes.toString().split("\n");
			recently_executed_rule = c.mostrecentruleplace;
		}
	}*/
	
	/**Will be used in getting the world I assume...*/
	public class worldBundle {
		int current_timestep;
		int current_version_number;
		//int update_since not really sure what to do about this.
		double rate;
		String name;
		int population;
		int rows;
		int cols;
		int [] dead_critters;
		Inhabitant [] state;
		public worldBundle(int rone, int rtwo, int cone, int ctwo, int oldVersion, PostRequests pr, int sesid, String al) {

		}
		public worldBundle(int rone, int rtwo, int cone, int ctwo, PostRequests pr, int sesid, String al) {
			this(rone, rtwo, cone, ctwo, 0, pr, sesid, al);
		}
		public worldBundle(int oldVersion, PostRequests pr, int sesid, String al) {
			
		}
		public worldBundle(PostRequests pr, int sesid, String al) {
			this(0, pr, sesid, al);
		}
		
		private void fillInBundle(LogEntry le, PostRequests pr, int sessionID, String accesslevel) {
			current_timestep = w.getTime();
			current_version_number = le.version;
			rate = pr.rate;
			name = w.name;
			population = w.critters.size();
			rows = w.ROWS;
			cols = w.COLUMNS;
			int place = 0;
			Iterator <Integer> i = le.deadCritters.iterator();
			while (i.hasNext()) {
				dead_critters[place] = i.next();
			}
			Iterator <HexUpdate> hexups = le.updates.iterator();
			while(hexups.hasNext()) {
				Inhabitant.getInhabitant(hexups.next().updatedValue, pr.w, accesslevel, sessionID);
			}
		}
	}
	
	/**A general class for the inhabitants. It has all the fields
	 * that any inhabitant would need, so all inhabitants can be
	 * unpacked using this class. Used so far in create_entity among other things*/
	public static class Inhabitant {
		int row;
		int col;
		String type;
		int id;
		String species_id;
		int direction;
		int [] mem;
		int value;
		int recently_executed_rule;
		String program; //should this be a string?
		int amount;
		/**makes a critter bundle with the critter's: <br>
		 * id, row, col, direction, species_id, mem, program, recently_executed_rule
		 * @param c
		 */
		public Inhabitant() {}
		public Inhabitant (Critter c) {
			id = c.id;
			row = c.row;
			col = c.col;
			species_id = c.name;
			direction = c.direction;
			mem = c.mem;
			program = c.genes.toString();
			recently_executed_rule = c.mostrecentruleplace;
		}
		
		public static Inhabitant getInhabitant(Hex hex, World w, String access, int sessionID) {
			Inhabitant i;
			if (hex.getNumRep() > 0) {
				Critter c = (Critter) hex;
				if (access.equals("admin") || sessionID == c.godId) {
					i = GetRequests.getFullCritterBundle(c);
				} else {
					i = GetRequests.getWeakCritterBundle(c);
				}
				i.type = "critter";
			} else {
				i = new Inhabitant();
				i.row = hex.row;
				i.col = hex.col;
				if (hex.getNumRep() == 0) {
					i.type = "nothing";
				} else if (hex.getNumRep() == w.ROCK_VALUE) {
					i.type = "rock";
				}
				else {
					i.type = "food";
					i.amount = ((Food) hex).getAmount();
				}
			}
			return i;
		}
	}
	
	/**A bundle of critters which is used in the Post method*/
	public class CritPlacementBundle {
		String species_id;
		String program;
		int [] mem;
		Placement [] positions;
		int num;
		private CritPlacementBundle(Critter c) {
			species_id = c.name;
			program = c.genes.toString();
			mem = c.mem;
		}
		/**Makes a placementbundle where location of placement is specified for each critter*/
		public CritPlacementBundle(Critter c, Placement [] pos) {
			this(c);
			positions = pos;
		}
		/**Makes a placementbundle where there is a specified number of randomly placed crits*/
		public CritPlacementBundle(Critter c, int number) {
			this(c);
			num = number;
		}
		
	}
	
	public class Placement {
		int row;
		int col;
	}
	

}
