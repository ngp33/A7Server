package ast;

public abstract class Onekid implements Node, mutation.Replacable { 
	
	public Expr only;
	
	
	public Node nodeAt(int place) throws IndexOutOfBoundsException{
		if (place == 0){
			return this;
		}
		return only.nodeAt(place-1);
	}
	
	public int size(){
		return only.size() + 1;
	}
	
	abstract Onekid getRootCopy();
	
	public Node copy() {
		Onekid clone = getRootCopy();
		
		clone.only = (Expr) only.copy();
		
		return clone;
	}
	
	public void replaceKid(Node old, Node replacement) {
		only = (Expr) replacement;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return (this.prettyPrint(sb).toString());
	}
	
	/*public void fillInMissingKids(Program possibleKids) {
		if (only == null) {
			only = (Expr) possibleKids.getRandomNode(Expr.class);
		}*/

}
