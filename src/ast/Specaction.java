package ast;

import java.util.Random;

import world.Critter;

public class Specaction extends Action {
	public Expr eval;

	public Specaction(Hamlet input, Expr e) {
		super(input);
		eval = e;
	}
	
	public StringBuilder prettyPrint(StringBuilder sb){
		super.prettyPrint(sb);
		sb.append("[ ");
		eval.prettyPrint(sb);
		sb.append("] ");
		return sb;
	}
	
	public int size(){
		return eval.size()+1;
	}
	
	public Node nodeAt(int index){
		if (index == 0){
			return this;
		}
		return (eval.nodeAt(index-1));
	}
	
	public Node copy() {
		return new Specaction(type, (Expr) eval.copy());
	}

	@Override
	public void transform() {
		Random rand = new Random();
		
		type = Hamlet.values()[rand.nextInt(2)+10];
	}
	
	public void replaceKid(Node old, Node replacement) {
		eval = (Expr) replacement;
	}
	
	public void commit(Critter c){
		if (type.equals(Hamlet.serve)) {
			c.serve(eval.value(c));
		}
		else if (type.equals(Hamlet.tag)) {
			c.youreit(eval.value(c));
		}
	}
	
}
