package ast;

import java.util.Random;

import ast.Action.Hamlet;
import world.Critter;

public class Booly extends TwokidsSameType implements Condition {
	
	public Booly(Expr one, Expr two, equalities e){
		left = one;
		right = two;
		link = e;
		symbol = makenice();
	}
	
	public Booly() {}
	

	private String makenice() {
		if (link.equals(equalities.EQ)){
			return "=";
		}
		else if (link.equals(equalities.LT)){
			return "<";
		}
		else if (link.equals(equalities.LE)){
			return "<=";
		}
		else if (link.equals(equalities.GT)){
			return ">";
		}
		else if (link.equals(equalities.GE)){
			return ">=";
		}
		else if (link.equals(equalities.NE)){
			return "!=";
		}
		return null;
	}


	/*@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		// TODO Auto-generated method stub
		left.prettyPrint(sb);
		if (link.equals(equalities.LE) || link.equals(equalities.LT)){
			sb.append("<");
		}
		else if (link.equals(equalities.GE) || link.equals(equalities.GT)){
			sb.append(">");
		}
		else if (link.equals(equalities.NE)){
			sb.append("!");
		}
		if (!(link.equals(equalities.LT) || link.equals(equalities.GT))){
			sb.append("= ");
		}
		else{
			sb.append(" ");
		}
		right.prettyPrint(sb);
		return sb;
	}*/

	@Override
	public Boolean getval(Critter c) {
		Expr left = (Expr) this.left;
		Expr right = (Expr) this.right;
		if (link.equals(equalities.LT)){
			return (left.value(c) < right.value(c));
		}
		else if (link.equals(equalities.LE)){
			return (left.value(c) <= right.value(c));
		}
		else if (link.equals(equalities.EQ)){
			return (left.value(c) == right.value(c));
		}
		else if (link.equals(equalities.GT)){
			return (left.value(c) > right.value(c));
		}
		else if (link.equals(equalities.GE)){
			return (left.value(c) >= right.value(c));
		}
		else if (link.equals(equalities.NE)){
			return (left.value(c) != right.value(c));
		}
		System.out.println("what did you input for the rel value? It's not right");
		return false; // TODO make more useful.
	}
	
	public enum equalities{
		LT, LE, EQ, GT, GE, NE;
	}


	@Override
	Twokids getRootCopy() {
		return new Booly();
	}

	@Override
	public void transform() {
		Random rand = new Random();
		
		link = equalities.values()[rand.nextInt(6)];
		symbol = makenice();
	}

	
}
