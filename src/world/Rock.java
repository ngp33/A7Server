package world;

public class Rock extends Hex {
	
	public Rock() {}
	

	@Override
	int getNumRep() {
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
