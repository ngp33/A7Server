package ast;

import java.util.Random;

import world.Critter;

public class Action extends Nokids implements Node, mutation.Removable, mutation.Transformable {
	
	Hamlet type;
	
	public Action(Hamlet input){
		type = input;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(type.toString() + " ");
		return sb;
	}
	
	public enum Hamlet {
		wait, forward, backward, left, right, eat, attack,
		grow, bud, mate, tag, serve
	}

	@Override
	public Node copy() {
		return new Action(type);
	}
	
	public Node getReplacement() {
		return null;
	}

	@Override
	public void transform() {
		Random rand = new Random();
		
		type = Hamlet.values()[rand.nextInt(10)];
	}
	
	@Override
	public Node getRandomReplacement(Program possibleKids) {
		return possibleKids.getRandomNode(Action.class);
	}

	public void commit(Critter c) {
		Actionpacked.themove(c,type);
		//assert that mem[4] went down?
		assert c.mem[4] <= c.mem[3] * c.w.ENERGY_PER_SIZE;
		assert c.mem[4] > 0 || (!c.w.getHex(c.row, c.col).equals(c));
		assert c.w.isInGrid(c.row, c.col);
		// TODO Auto-generated method stub
	}

}
