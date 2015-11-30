package ast;

import java.util.Random;

import world.Critter;

public class Sensespace extends Senses implements mutation.Removable, mutation.Insertable {
	
	public Sensespace(int which, Expr where) {
		super(which);
		only = where;
	}
	
	public Sensespace() {
		super(null);
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb){
		sb.append(pres.toString() + "[ ");
		only.prettyPrint(sb);
		sb.append("] ");
		return sb;
		
	}
	
	public Node nodeAt(int index){
		if (index == 0){
			return this;
		}
		return (only.nodeAt(index-1));
	}
	
	public int size(){
		return only.size() + 1;
	}
	
	public Node getReplacement() {
		return only;
	}
	
	@Override
	public void transform() {
		Random rand = new Random();
		
		pres = six.values()[rand.nextInt(3)];
	}
	
	public boolean fillInMissingKids(Program possibleKids) {
		if (only == null) {
			only = (Expr) possibleKids.getRandomNode(Expr.class);
			if (only == null) return false;
		}
		return true;
	}
	
	public int value(Critter c){
		return Sensespacked.sniff(c, this);
	}

}
