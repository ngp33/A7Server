package RequestHandler;

import world.Hex;

public class HexUpdate {

	int row;
	int col;
	
	Hex updatedValue;
	
	public HexUpdate(int r, int c, Hex upVal) {
		row = r;
		col = c;
		updatedValue = upVal;
	}
	
}
