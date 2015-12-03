package RequestHandler;

import java.util.Collection;

import ast.Program;
import world.Critter;
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
	private class CritListBundle {
		CritBundle [] cb;
		public CritListBundle() {
			Collection<Critter> critterList = w.critters.values();
			cb = new CritBundle[critterList.size()];
			int i = 0;
			for (Critter c : critterList) {
				cb[i] = new CritBundle(c.id);
			}
		}
	}
	
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
	private class worldBundle {
		int current_timestep;
		int current_version_number;
		//int update_since not really sure what to do about this.
		float rate;
		String name;
		int population;
		int rows;
		int cols;
		int [] dead_critters;
		Inhabitant [] state;
		public worldBundle(int rone, int rtwo, int cone, int ctwo, int timeinterval) {
			//TODO get world dif over this time
		}
	}
	
	/**A general class for the inhabitants. It has all the fields
	 * that any inhabitant would need, so all inhabitants can be
	 * unpacked using this class. Used so far in create_entity among other things*/
	public class Inhabitant {
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
		private Inhabitant (Critter c) {
			id = c.id;
			row = c.row;
			col = c.col;
			species_id = c.name;
			direction = c.direction;
			mem = c.mem;
			String [] str = c.genes.toString().split("\n");
			recently_executed_rule = c.mostrecentruleplace;
			
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
