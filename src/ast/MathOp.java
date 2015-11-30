package ast;

import java.util.Random;

import world.Critter;

public class MathOp extends TwokidsSameType implements Expr, mutation.Removable {

	MathOperator op;
	
	public MathOp(Expr one, Expr two, MathOperator o){
		link = o; //This is so that MathOp still has a link for copying purposes 
		left = one;
		right = two;
		op = (MathOperator) link;
		symbol = makenice();
	}
	
	public MathOp() {}
	
    public StringBuilder prettyPrint(StringBuilder sb) {
    	sb.append("( ");
    	super.prettyPrint(sb);
    	sb.append(") ");
    	
    	return sb;
    }
	
	public String makenice() {
		switch (op) {
			case add: return "+";
			case sub: return "-";
			case mult: return "*";
			case div: return "/";
			case mod: return "mod";
		}
		return null;
	}
	
	public enum MathOperator {
		add, sub, mult, div, mod
	}

	@Override
	Twokids getRootCopy() {
		return new MathOp();
	}
	
	public Node getReplacement() {
		Random rand = new Random();
		
		return rand.nextBoolean() ? left : right;
	}
	
	@Override
	public void transform() {
		Random rand = new Random();
		
		link = MathOperator.values()[rand.nextInt(5)];
		symbol = makenice();
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
	public Node getRandomReplacement(Program possibleKids) {
		return possibleKids.getRandomNode(Expr.class);
	}
	
	@Override
	public Node copy() {
		
		MathOp clone = new MathOp();
		
		clone.left = left.copy();
		clone.right = right.copy();
		clone.link = link;
		clone.symbol = symbol;
		clone.op = op;
		return clone;
	}

	@Override
	public int value(Critter c) {
		Expr left = (Expr) this.left;
		Expr right = (Expr) this.right;
		switch (op) {
			case add: return left.value(c) + right.value(c);
			case sub: return left.value(c) - right.value(c);
			case mult: return left.value(c) * right.value(c);
			case div: return right.value(c) == 0 ? 0 : left.value(c) / right.value(c);
			case mod: return right.value(c) == 0 ? 0 : left.value(c) % right.value(c);
			default:
				throw new UnsupportedOperationException(); //This should never be an issue because
		}//The problem would have come up when the sentence was parsed in the first place. This would be a programmer
	}//error
	
}
