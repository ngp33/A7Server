package world;

public class Food extends Hex { //Rock value is assumed to be -1 here. 
	
	int quantity;
	
	
	public Food(int init) {
		quantity = init;
	}
	
	public Food() {
		quantity = 0;
	}


	@Override
	int getNumRep() {
		return quantity == 0 ? 0 : -quantity-1;
	}
	
	void addFood(int qty) {
		quantity += qty;
	}

	@Override
	char getASCIIRep() {
		if (quantity == 0) {
			return '-';
		} else {
			return 'F';
		}
	}

	@Override
	public String getHexInfo() {
		if (quantity == 0) {
			return "An empty hex.";
		} else {
			return quantity + " units of food.";
		}
	}

}
