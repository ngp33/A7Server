package ast;

import java.util.Random;

import ast.Action.Hamlet;
import world.Critter;

/**
 * A representation of a binary Boolean condition: 'and' or 'or'
 *
 */
public class BinaryCondition extends TwokidsSameType implements Condition, mutation.Removable {

    /**
     * Create an AST representation of l op r.
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
        //TODO
    	left = l;
    	right = r;
    	link = op;
    	symbol = makenice();
    }

    public BinaryCondition() {}
    
    
    public StringBuilder prettyPrint(StringBuilder sb) {
    	sb.append("{ ");
    	super.prettyPrint(sb);
    	sb.append("} ");
    	
    	return sb;
    }

    /*@Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }*/
    
    /** represents the operator in a nice printable way*/
    public String makenice(){
    	return (link == Operator.OR) ? "or" : "and";
    }

    /**
     * An enumeration of all possible binary condition operators.
     */
    public enum Operator {
        OR, AND;
    }

	@Override
	Twokids getRootCopy() {
		return new BinaryCondition();
	}
	
	public Node getReplacement() {
		Random rand = new Random();
		
		return rand.nextBoolean() ? left : right;
	}

	@Override
	public void transform() {
		Random rand = new Random();
		
		link = Operator.values()[rand.nextInt(2)];
		symbol = makenice();
	}
	
	@Override
	public boolean fillInMissingKids(Program possibleKids) {
		if (left == null) {
			left = possibleKids.getRandomNode(Condition.class);
			if (left == null) return false;
		} else if (right == null) {
			right = possibleKids.getRandomNode(Condition.class);
			if (right == null) return false;
		}
		
		return true;
	}

	@Override
	public Boolean getval(Critter c) {
    	if (link.equals(Operator.AND)){
    		if (((Condition) left).getval(c) && ((Condition) right).getval(c)){
    			return true;
    		}
    		return false;
    	}
    	else{
    		if (((Condition) left).getval(c) || ((Condition) right).getval(c)){
    			return true;
    		}
    		return false;
    	}
	}
	
}
