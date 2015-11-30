package world;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import parse.Parser;
import parse.ParserImpl;

public class World extends Observable {
	
	Hex[][] grid;
	ArrayList<Critter> critters;
	ArrayList<Critter> firstgencrits;
	ArrayList<Critter> crittersToRemove = new ArrayList<Critter>();
	
	String name; // Not sure what this is for yet but it's included in the world file.
	int time = 0;
	int rows;
	int columns;
	
	public final int BASE_DAMAGE;
	public final float DAMAGE_INC;
	public final int ENERGY_PER_SIZE;
	public final int FOOD_PER_SIZE;
	public final int MAX_SMELL_DISTANCE;
	public final int ROCK_VALUE;
	public final int COLUMNS;
	public final int ROWS;
	public final int MAX_RULES_PER_TURN;
	public final int SOLAR_FLUX;
	public final int MOVE_COST;
	public final int ATTACK_COST;
	public final int GROW_COST;
	public final int BUD_COST;
	public final int MATE_COST;
	public final int RULE_COST;
	public final int ABILITY_COST;
	public final int INITIAL_ENERGY;
	public final int MIN_MEMORY;
	
	
	public World() {
		name = "Untitled";
		critters = new ArrayList<Critter>();
		firstgencrits = new ArrayList<Critter>();
		
		// This would look cleaner in its own method, but I need to do it inside the constructor.
		try {
			FileReader f = new FileReader("constants.txt");
			
			BASE_DAMAGE = getNumberFromLine(f);
			DAMAGE_INC = getFloatFromLine(f);
			ENERGY_PER_SIZE = getNumberFromLine(f);
			FOOD_PER_SIZE = getNumberFromLine(f);
			MAX_SMELL_DISTANCE = getNumberFromLine(f);
			ROCK_VALUE = getNumberFromLine(f);
			COLUMNS = getNumberFromLine(f);
			ROWS = getNumberFromLine(f);
			MAX_RULES_PER_TURN = getNumberFromLine(f);
			SOLAR_FLUX = getNumberFromLine(f);
			MOVE_COST = getNumberFromLine(f);
			ATTACK_COST = getNumberFromLine(f);
			GROW_COST = getNumberFromLine(f);
			BUD_COST = getNumberFromLine(f);
			MATE_COST = getNumberFromLine(f);
			RULE_COST = getNumberFromLine(f);
			ABILITY_COST = getNumberFromLine(f);
			INITIAL_ENERGY = getNumberFromLine(f);
			MIN_MEMORY = getNumberFromLine(f);
		} catch (IOException e) {
			System.out.println("constants.txt does not exist or is not correct.");
			throw new RuntimeException(); // Not sure what exception to throw when constants.txt is bad.
		}
		
		setupGrid(ROWS, COLUMNS);
	}
	
	public World(int numRows, int numCols, String n) {
		name = n;
		critters = new ArrayList<Critter>();
		firstgencrits = new ArrayList<Critter>();
		
		try {
			FileReader f = new FileReader("constants.txt");
			
			BASE_DAMAGE = getNumberFromLine(f);
			DAMAGE_INC = getFloatFromLine(f);
			ENERGY_PER_SIZE = getNumberFromLine(f);
			FOOD_PER_SIZE = getNumberFromLine(f);
			MAX_SMELL_DISTANCE = getNumberFromLine(f);
			ROCK_VALUE = getNumberFromLine(f);
			COLUMNS = getNumberFromLine(f);
			ROWS = getNumberFromLine(f);
			MAX_RULES_PER_TURN = getNumberFromLine(f);
			SOLAR_FLUX = getNumberFromLine(f);
			MOVE_COST = getNumberFromLine(f);
			ATTACK_COST = getNumberFromLine(f);
			GROW_COST = getNumberFromLine(f);
			BUD_COST = getNumberFromLine(f);
			MATE_COST = getNumberFromLine(f);
			RULE_COST = getNumberFromLine(f);
			ABILITY_COST = getNumberFromLine(f);
			INITIAL_ENERGY = getNumberFromLine(f);
			MIN_MEMORY = getNumberFromLine(f);
		} catch (IOException e) {
			System.out.println("constants.txt does not exist or is not correct.");
			throw new RuntimeException(); // Not sure what exception to throw when constants.txt is bad.
		}
		
		if (numRows < numCols/2 + 1) {
			setupGrid(ROWS, COLUMNS);
		}
		setupGrid(numRows, numCols);
	}
	
	
	private void setupGrid(int numRows, int numCols) {
		rows = numRows;
		columns = numCols;
		
		System.out.println(COLUMNS);
		System.out.println(ROWS);
		
		//grid = new Hex[numCols][numRows - (numCols + 1) / 2];
		grid = new Hex[numCols][numRows - numCols/2];
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				Hex empty = new Food(0);
				empty.col = i;
				empty.row = j + (i + 1)/2;
				grid[i][j] = empty; // The hexes in the grid array that aren't part of the world will be
									// made rocks by getHex.
			}
		}
	}
	
	public int getTime() {
		return time;
	}
	
	public int getNumCritters() {
		return critters.size();
	}
	
	public int getNumColumns() {
		return columns;
	}
	
	public int getNumRows() {
		return rows;
	}
	
	private int getNumberFromLine(FileReader f) throws IOException {
		String num = "";
		char c = 0;
		
		while (c != '\n') {
			c = (char) f.read();
			if ((c > 47 && c < 58) || c == 45) { //this didnt handle negatives so I added an or for -
				num = num + c;
			}
		}
		
		return Integer.parseInt(num);
	}
	
	private float getFloatFromLine(FileReader f) throws IOException {
		String num = "";
		char c = 0;
		
		while (c != '\n') {
			c = (char) f.read();
			if ((c > 47 && c < 58) || c == 45 || c == 46) { //this didnt handle negatives so I added an or for -
				num = num + c;
			}
		}
		
		return Float.parseFloat(num);
	}
	
	/*private float getFloatFromLine(FileReader f) throws IOException {
		int one = getNumberFromLine(f);
		int two = getNumberFromLine(f);
		String num = one + "." + two;
		return Float.parseFloat(num);
	}*/
	
	public Hex getHex(int row, int col) {
		if (isInGrid(row, col)) {
			row -= (col + 1)/2; //I changed this. It was row -= row/2
			
			return grid[col][row];
		} else {
			return new Rock();
		}
	}
	
	/**returns the dimensions of the grid array. TODO update it so it handles different
	 * types of worlds correctly (if the columns are odd in number vs even)
	 * @return
	 */
	public int [] worlddim() {
		int [] rowcol = new int [2];
		rowcol[0] = grid[0].length;
		rowcol[1] = grid.length;
		return rowcol;
	}
	
	public void setHex(int row, int col, Hex h) {
		if (isInGrid(row, col)) {
			h.col = col;
			h.row = row;
			
			row -= (col + 1)/2; //I changed this too. It was row -= row/2
			
			grid[col][row] = h;
		}
	}
	
	public boolean isInGrid(int row, int col) {
		int i = 2*row - col;
		return (col >= 0 && col < columns && i >= 0 && i < 2*rows - columns);
	}
	
	/**returns the number representing the object at row, col using the critter
	 * (not grid) coordinate system.
	 * @param rowcommacol
	 * @return
	 */
	public int getNumRep(int [] rowcommacol) {
		if (isInGrid(rowcommacol[0], rowcommacol[1])) {
			return getHex(rowcommacol[0], rowcommacol[1]).getNumRep();
		}
		return -1;
	}
	
	public Hex[] getEmptyHexes() {
		ArrayList<Hex> hexes = new ArrayList<Hex>();
		boolean oddNumCols = grid.length % 2 == 1;
		
		for (int i = 0; i < grid.length; i++) {
			if (oddNumCols && i % 2 == 1) {
				for (int j = 0; j < grid[0].length - 1; j++) {
					Hex candidate = grid[i][j];
					if (candidate.getNumRep() == 0) {
						hexes.add(grid[i][j]);
					}
				}
			} else {
				for (int j = 0; j < grid[0].length; j++) {
					Hex candidate = grid[i][j];
					if (candidate.getNumRep() == 0) {
						hexes.add(grid[i][j]);
					}
				}
			}
		}
		
		return hexes.toArray(new Hex[hexes.size()]);
	}
	
	/**Clears a hex of whatever was on it before and puts a certain amount of food on it
	 * Note, putfood -1, rowcommacol returns a
	 * 
	 * @param amount
	 * @param rowcommacol
	 */
	public void putFood(int amount, int [] rowcommacol) {
		replace(new Food(amount), getHex(rowcommacol[0], rowcommacol[1]));
	}
	
	/**Effect: Makes a new hex without any food on it. 
	 * Invariant: There exists a hex at rowcommacol */
	public void putEmpty(int [] rowcommacol) {
		replace(new Food(-1), getHex(rowcommacol[0], rowcommacol[1]));
	}
	
	/**Effect: Swaps the position of hexes one and two. It updates their
	 * internal row/col information as well as the world's 
	 * row/col information of them
	 * @param one
	 * @param two
	 */
	public void swap(Hex one, Hex two) {
		int temprow = one.row;
		int tempcol = one.col;
		one.row = two.row;
		one.col = two.col;
		two.row = temprow;
		two.col = tempcol;
		setHex(one.row, one.col, one);
		setHex(two.row, two.col, two);
	}
	
	/**Effect: It replaces the hex goner with one. Again, this updates,
	 * the hex position pointers within hex one and within the world. Hex one
	 * need not have an initialized row, col.
	 * @param one
	 * @param goner
	 */
	public void replace(Hex one, Hex goner) {
		one.row = goner.row;
		one.col = goner.col;
		setHex(one.row, one.col, one);
		// Has the hex already been checked to be of type Food?
		//Food f = (Food) getHex(rowcommacol[0], rowcommacol[1]);
		//f.addFood(amount);
	}
	
	/**Adds a critter to the world's list of critters. Invariant, critter list is not being
	 * iterated through (as is the case while a timestep is executed
	 * @param c
	 */
	public void addCritter(Critter c) {
		critters.add(c);
	}
	
	/**Adds a critter mid-time step*/
	public void addMidStep(Critter c) {
		firstgencrits.add(c);
	}
	
	public void advance() {
		for (Critter c : critters) {
			c.timestep(); // Executes critter's program?
		}
		for (Critter c : firstgencrits) {
			c.timestep();
			addCritter(c);
		}
		firstgencrits.clear();
		time++;
		for (Critter c : crittersToRemove) {
			critters.remove(c);
		}
		crittersToRemove.clear();
		setChanged();
		notifyObservers();
	}
	
	public void advanceTime(int amount) {
		for (int i = 0; i < amount; i++) {
			advance();
		}
	}
	
	public StringBuilder getInfo() {
		StringBuilder result = new StringBuilder();
		result.append("Time elapsed: " + time + "\n");
		result.append("Critters alive: " + critters.size() + "\n");
		appendASCIIMap(result);
		
		return result;
	}
	
	private void appendASCIIMap(StringBuilder sb) {
		boolean evenNumRows = grid.length % 2 == 0;
		boolean even = evenNumRows;
		int i = grid[0].length - 1;
		
		while (i >= 0) {
			if (even) {
				for (int j = 1; j < grid.length; j+=2) {
					sb.append("  ");
					sb.append(grid[j][i].getASCIIRep());
					sb.append(" ");
				}
				sb.append("\n");
			} else {
				sb.append(grid[0][i].getASCIIRep());
				for (int j = 2; j < grid.length; j+=2) {
					sb.append("   ");
					sb.append(grid[j][i].getASCIIRep());
				}
				sb.append("\n");
				i--;
			}
			even = !even;
		}
	}

}
