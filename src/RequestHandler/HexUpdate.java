package RequestHandler;

import world.Hex;

public class HexUpdate {

	public int row;
	public int col;
	
	public Hex updatedValue;
	
	public HexUpdate(int r, int c, Hex upVal) {
		row = r;
		col = c;
		updatedValue = upVal;
	}
	
}
