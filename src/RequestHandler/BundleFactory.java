package RequestHandler;

import ast.Program;
import world.Critter;
import world.World;

/**Creates a bundle factory which bundles information into specific classes*/
public class BundleFactory {
	World w;
	
	public BundleFactory(World w) {
		this.w = w;
	}
	
	
	/**Makes a new critListBundle*/
	public critListBundle getCritListBundle() {
		return new critListBundle();
	}
	
	/**Makes a new Critter bundle*/
	public critBundle getCritBundle(int critID) {
		return new critBundle(critID);
	}
	
	/**Gets the world or the subsection of the world since some number of timesteps
	 * if the specifications are the world boundaries (0, w.rowmax, 0, w.colmax) this will
	 * return the dif for the whole world (hopefully)*/
	public worldBundle getWorldBundle(int rowinit, int rowfin, int colinit, int colfin, int ts) {
		return null;
		
	}
	
	private class critListBundle {
		//Linkedlist to hold the critters?
		critBundle [] cb; //this is probably incompatible with the info given in the API
		public critListBundle() { //TODO get critterlist from world.
		}
	}
	
	private class critBundle extends inhabitants {
		int id;
		String specieis_id;
		Program program;
		int row;
		int col;
		int direction;
		int [] mem;
		int recently_executed_rule; 
		public critBundle(int critID) {
			//TODO form the bundle based on the critter ID.
		}
	}
	
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
		inhabitants [] state;
		public worldBundle(int rone, int rtwo, int cone, int ctwo, int timeinterval) {
			//TODO get world dif over this time
		}
	}
	
	private class rock extends inhabitants {
		//TODO complete
	}
	
	private class food extends inhabitants {
		//TODO complete
	}
	
	private class empty extends inhabitants {
		//TODO complete
	}
	
	
	/**A general class for the inhabitants. It has all the fields
	 * that any inhabitant would need, so all inhabitants can be
	 * unpacked using this class*/
	private class inhabitants {
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
	}

}
