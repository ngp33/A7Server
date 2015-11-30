package ast;

import java.util.Random;

public abstract class Manykids implements Node, mutation.Swappable {
	public Node [] children;
	
	
	public Node nodeAt (int there){
		int bigness = 0;
		int place = -1;
		if (there == 0){
			return this;
		}
		while (bigness < there){
			place ++;
			bigness += children[place].size();
		}
		bigness -= children[place].size();
		return children[place].nodeAt(there-bigness-1);
	}
	
	public int size(){
		int bigness = 1;
		for (Node them : children){
			bigness += them.size();
		}
		return bigness;
	}
	
	public Node getRandomKidCopy() {
		Random rand = new Random();
		int i = rand.nextInt(children.length);
		
		return children[i].copy();
	}
	
	public void addKid(Node n) {
		Node[] newChildren = new Node[children.length+1];
		
		for (int i = 0; i < children.length; i++) {
			newChildren[i] = children[i];
		}
		
		newChildren[children.length] = n;
		
		children = newChildren;
	}
	
	abstract Manykids getRootCopy();
	
	public Node copy() {
		Manykids clone = getRootCopy();
		clone.children = new Node[children.length];
		
		for (int i = 0; i < children.length; i++) {
			clone.children[i] = children[i].copy();
		}
		
		return clone;
	}
	
	//Precondition: old is a reference to an object that is referenced in children exactly once.
	public void replaceKid(Node old, Node replacement) {
		if (replacement == null) {
			if (children.length == 1) {
				return;
			}
			
			Node[] newChildren = new Node[children.length-1];
			
			int j = 0;
			for (int i = 0; i < children.length; i++) {
				if (children[i] != old) {
					newChildren[j] = children[i];
					j++;
				}
			}
			
			children = newChildren;
		} else {
			for (int i = 0; i < children.length; i++) {
				if (children[i] == old) {
					children[i] = replacement;
				}
			}
		}
	}
	
	public void swapKids() {
		Random rand = new Random();
		
		int i1 = rand.nextInt(children.length);
		int i2 = rand.nextInt(children.length);
		
		/*while (i1 == i2) {
			i2 = rand.nextInt(children.length);
		}*/
		
		swapKidsHelper(i1, i2);
	}
	
	public void swapKidsHelper(int i1, int i2) {
		Node temp = children[i1];
		children[i1] = children[i2];
		children[i2] = temp;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return this.prettyPrint(sb).toString();
	}

}
