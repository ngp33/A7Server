package world;
import java.io.StringReader;
import java.util.Random;

import ast.ProgramImpl;
import ast.Rule;
import parse.ParserFactory;

public class Critter extends Hex {
	
	public ProgramImpl genes;
	public int direction;
	public World w;
	public int [] mem;
	public String name;
	public boolean matingdance;
	public Random r = new Random(); //this is useful to have for the random sense and I didn't want to keep generating random objects.
	public Rule mostrecentrule;
		
	/**Sets up the instance variables for a critter. Data is given in the order
	 * specified in 4.1 of the a5 spec.
	 * @param species
	 * @param data
	 * @param r
	 * @param rules
	 * @param w
	 */
	//The purpose of r is so that we don't have to keep generating new random objects
	public Critter(String species, int [] data, Random r, ProgramImpl rules, World w) { 
		this(data,r,rules,w);
		name = species;
		int [] mem = new int [data[0]];
		for (int place = 0; place < 5; place++) {
			mem[place] = data[place];
		}
		mem[5] = 1;
		mem[6] = 0;
		mem[7] = data[5];
		for (int them = 8; them < data[0]; them ++) {
			mem[them] = 0;
		}
		this.mem = mem;
		
	}
	
	public Critter(int [] mem, Random r, ProgramImpl genetics, World wrld) { //TODO Give the new critter a name
		genes = genetics;
		direction = r.nextInt(6);
		w = wrld;
		this.mem = mem;
		matingdance = false;
		this.r = r;
	}
	
	/**Effect: updates the critter's instance variables according to its rules. 
	 * It pretty much just handles everything that should happen to a critter when
	 * a time step passes.
	 */
	public void timestep(){
		matingdance = false;
		Rulehandler.altercritter(this);
	}
	
	/**This is the heavyweight getNumRep for critters which returns the detailed 
	 * appearance of the critter on the hex. The tradeoff is that this version
	 * requires knowledge of the observing critter.
	 * @param c
	 * @return
	 */
	public int getNumRep(Critter c) {
		int compdir = ((direction - c.direction) + 6) % 6;
		return mem[3] * 100000 + mem[6] * 1000 + mem[7] * 10 + compdir;
	}
	
	/**This is the lightweight getNumRep for critters which just returns a value
	 * which implies that the hex contains a critter, but no detail about the critter
	 */
	@Override
	public int getNumRep() {
		return 1;
	}
	
	public void consume(){
		Crittermethods.consume(this);
	}
	
	public void attack(){
		Crittermethods.attack(this);
	}
	
	public void movement(boolean forward){
		Crittermethods.movement(this,forward);
	}
	
	public void turn(boolean left) {
		Crittermethods.turn(this, left);
	}
	
	public void waiting(){
		Crittermethods.wait(this);
	}
	
	public void dies() {
		Crittermethods.dies(this);
	}
	
	public void grow() {
		Crittermethods.grow(this);
	}
	
	public void mate() {
		Crittermethods.mate(this);
	}
	
	public void bud() {
		Crittermethods.asexual(this);
	}
	
	public void serve(int amount) {
		Crittermethods.serve(this, amount);
	}
	
	public void youreit(int num) {
		Crittermethods.tag(this, num);
	}
	
	public int nearby(int num) {
		return Crittersenses.nearby(this, num);
	}
	
	public int ahead(int num) {
		return Crittersenses.spacesahead(this, num);
	}
	
	public int smell() {
		return 0;
	}
	
	public int random(int num) {
		return Crittersenses.rando(this, num);
	}

	@Override
	public String getHexInfo() {
		StringBuilder s = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		s.append(name + "\n");
		for (int them : mem) {
			s.append(them);
		}
		s.append("\n");
		s.append(genes.prettyPrint(sb) + "\n");
		sb = new StringBuilder();
		s.append(mostrecentrule != null ? mostrecentrule.prettyPrint(sb).toString() : "No rule executed yet");
		return s.toString();
	}
	
	@Override
	char getASCIIRep() {
		return (char) (direction+48);
	}
	
	/**Returns: A new critter with all instance variables the same as the old except matingdance is false,
	 * mostrecentrule is null, and direction is randomly chosen.
	 * @return
	 */
	public Critter copy() {
		int [] mem = this.mem.clone();
		ProgramImpl genetics = (ProgramImpl) ParserFactory.getParser().parse(new StringReader(this.genes.toString()));
		Critter after = new Critter(mem, this.r, genetics, this.w);
		after.direction = after.r.nextInt(6);
		after.name = this.name;
		after.matingdance = false;
		after.mostrecentrule = null;
		return after;
	}
	
}
