package ast;

import world.Critter;

public class Update extends Twokids implements Node, mutation.Removable {
	
	Update next;
	
	public Update(MemToUpdate andm, Expr ern){
		left = andm;
		right = ern;
		next = null;
		symbol = ":=";
	}
	
	public Update(Update n) {
		next = n;
	}

	@Override
	Twokids getRootCopy() {
		return new Update(next);
	}
	
	public Node getReplacement() {
		return null;
	}

	@Override
	public Node getRandomReplacement(Program possibleKids) {
		return possibleKids.getRandomNode(Update.class);
	}

	
	//TODO make sure this handles mem out of bounds.
	//TODO make sure this doesn't update mem which are immutable
	/** updates the mem field of a critter if that mem field is
	 * available for updating.*/
	public void updatemem(Critter c) {
		if (((MemToUpdate) left).inrange(c)){ 
			if (((MemToUpdate) left).only.value(c) >= 7){
				c.mem[((MemToUpdate) left).only.value(c)] = ((Expr) right).value(c);
			}
		}
		
	}

}
