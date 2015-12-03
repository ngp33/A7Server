package world;

public class Rock extends Hex {
	
	public Rock() {}
	

	@Override
	public int getNumRep() {
		return -1;
	}

	@Override
	char getASCIIRep() {
		return '#';
	}

	@Override
	public String getHexInfo() {
		return "A rock.";
	}

}
