package world;

public abstract class Hex {

	public int row;
	public int col;
	
	abstract int getNumRep();
	

	/**A method for getting the printable information for a hex in accordance
	 * with hex <column, row> of section 6 of the A5 pdf.
	 * @return
	 */
	public abstract String getHexInfo();
	
	//Might be a good move to put info for the hexes so its easy to build
	//The info in 6. I could do it easily for critter if you want

	abstract char getASCIIRep();

}
