package ast;

import java.util.Random;

import ast.MathOp.MathOperator;
import world.Critter;

public class Num extends Nokids implements Expr, mutation.Transformable, mutation.Reparentable {
	
	int val;
	
	public Num(int anum){
		val = anum;
	}
	
	/*@Override
	public int value(){
		return val;
		
	}*/

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(val + " ");
		return sb;
	}

	@Override
	public Node copy() {
		return new Num(val);
	}
	
	@Override
	public void transform() {
		Random rand = new Random();
		int div = rand.nextInt();
		
		while (div == 0) {
			div = rand.nextInt();
		}
		
		int newVal = Integer.MAX_VALUE/div;
		if (newVal < 0) return;
		
		val = newVal;
	}
	
	//Cringe.
	@Override
	public mutation.Insertable getNewParent() {
		mutation.Insertable newParent;
		Random rand = new Random();
		
		int selector = rand.nextInt(3);
		if (selector == 0) {
			newParent = new MathOp();
			((MathOp) newParent).op = MathOperator.values()[rand.nextInt(5)];
			if (rand.nextBoolean()) {
				((MathOp) newParent).left = this;
			} else {
				((MathOp) newParent).right = this;
			}
			((MathOp) newParent).symbol = ((MathOp) newParent).makenice();
		} else if (selector == 1) {
			newParent = new MemAccess(this);
		} else {
			newParent = new Sensespace(rand.nextInt(4) + 1, this);
		}
		
		return newParent;
	}

	@Override
	public int value(Critter c) {
		return val;
	}

}
