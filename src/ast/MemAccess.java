package ast;

import java.util.Random;

import ast.MathOp.MathOperator;
import world.Critter;

public class MemAccess extends MemToUpdate implements Expr, mutation.Removable, mutation.Insertable,
mutation.Reparentable {
	
	public MemAccess(Expr e) {
		super(e);
	}
	
	public MemAccess() {}
	
	public MemAccess(MemToUpdate mem){
		only = mem.only;
	}
	
	@Override
	Onekid getRootCopy() {
		return new MemAccess();
	}
	
	public Node getReplacement() {
		return only;
	}
	
	public boolean fillInMissingKids(Program possibleKids) {
		if (only == null) {
			only = (Expr) possibleKids.getRandomNode(Expr.class);
			if (only == null) return false;
		}
		return true;
	}

	//I hate copy and pasting code but I don't want to spend too much time changing stuff around.
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
			newParent = new MemAccess();
			((MemAccess) newParent).only = this;
		} else {
			newParent = new Sensespace(rand.nextInt(4) + 1, this);
		}
		
		return newParent;
	}
	
	public Node getRandomReplacement(Program possibleKids) {
		return possibleKids.getRandomNode(Expr.class);
	}


}
