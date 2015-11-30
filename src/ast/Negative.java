package ast;

import java.util.Random;

import ast.MathOp.MathOperator;
import world.Critter;

public class Negative extends Onekid implements Expr, mutation.Reparentable, mutation.Insertable {
	
	public Negative(Expr e){
		only = e;
	}
	
	public Negative() {}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append("-");
		only.prettyPrint(sb);
		return sb;
	}

	@Override
	Onekid getRootCopy() {
		return new Negative();
	}
	
	public Node getRandomReplacement(Program possibleKids) {
		return possibleKids.getRandomNode(Expr.class);
	}

	@Override
	public boolean fillInMissingKids(Program possibleKids) {
		if (only == null) {
			only = (Expr) possibleKids.getRandomNode(Expr.class);
			if (only == null) return false;
		}
		return true;
	}

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
		return only.value(c) * (-1);
	}

}
