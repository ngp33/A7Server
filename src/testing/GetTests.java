package testing;

import java.util.Random;

import RequestHandler.GetRequests;
import world.Critter;
import world.World;

public class GetTests {
	
	GetRequests gr;
	World w;
	Critter c;
	
	public void init() {
		w = new World(15,5,"name");
		c = new Critter(new int [] {8,1,1,1,200,0,0,0}, new Random(), )
		w.addCritter();
	}

}
