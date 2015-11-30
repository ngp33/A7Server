package ast;

import java.util.Random;

import world.Critter;

public class Updateact extends Manykids implements Node, mutation.Replacable {
	
	boolean hasAction;

	public Updateact(Update [] u, Action a){
		/*children = (a == null) ? new Node[u.length] : new Node [u.length + 1];
		for (int place = 0; place < u.length; place++){
			children[place] = u[place];
		}
		if (a != null){
			children[u.length] = a;
		}*/
		
		//If blocks are pretty slow so I just rewrote it with 1 instead of two
		
		if (a == null) {
			children = new Node[u.length];
			hasAction = false;
		} else {
			children = new Node[u.length+1];
			children[u.length] = a;
			hasAction = true;
		}
		
		for (int place = 0; place < u.length; place++) {
			children[place] = u[place];
		}
	}
	
	public Updateact(boolean hasAct) {
		hasAction = hasAct;
	}
	
	/*public Update[] getupdates(){
		return u;
	}
	public Action getaction(){
		return a;
	}
	
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		if (u.length != 0){
			for (Update them : u){
				them.prettyPrint(sb);
			}
		}
		if ( a != null){
			a.prettyPrint(sb);
		}
		return sb;
	}*/
		
	public StringBuilder prettyPrint(StringBuilder sb) {
		children[0].prettyPrint(sb);
		
		for (int i = 1; i < children.length; i++) {
			sb.append("\n\t"); //Adding spaces to line it up with the first update seems unnecessary to keep it pretty.
							   //I just added a tab.
			children[1].prettyPrint(sb);
		}
		
		sb.append(";");
		return sb;
	}

	Manykids getRootCopy() {
		return new Updateact(hasAction);
	}
	
	@Override
	public Node getRandomKidCopy() {
		if (!hasAction) {
			return super.getRandomKidCopy();
		}
		return null;
	}
	
	public void swapKids() {
		Random rand = new Random();
		
		int i1 = rand.nextInt(children.length);
		int i2 = rand.nextInt(children.length);
		
		if (children[i2] instanceof Action) {
			return;
		}
		
		swapKidsHelper(i1, i2);
	}
	
	public Node getRandomReplacement(Program possibleKids) {
		return possibleKids.getRandomNode(Updateact.class);
	}
	
	
	/**Effect: alters the instance variables of critter c according to the rules updates and action.
	 * returns: true if an action is committed
	 * Should only be called if the rule is supposed to happen.
	 * @param c
	 * @return
	 */
	public boolean operate(Critter c){
		for (int place = 0; place < children.length-1; place++){
			((Update) children[place]).updatemem(c);
		}
		if (hasAction){ //TODO make sure this calls a different commit if the action is attack
			((Action) children[children.length - 1]).commit(c);
		}
		else{ 
			((Update) children[children.length - 1]).updatemem(c);
		}
		return hasAction;
	}

}
		
