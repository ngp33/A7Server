package ast;

public abstract class Nokids implements Node, mutation.Replacable {

	public Node nodeAt(int n) throws IndexOutOfBoundsException{
		if (n == 0){
			return this;
		}
		throw new IndexOutOfBoundsException();
	}
	
	public int size(){
		return 1;
	}
	
	public void replaceKid(Node old, Node replacement) {
		throw new UnsupportedOperationException();
	}
	
	public Node getRandomReplacement(Program possibleKids) {
		return possibleKids.getRandomNode(Expr.class);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return this.prettyPrint(sb).toString();
	}
}
