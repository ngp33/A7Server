package mutation;

import ast.Node;
import ast.Program;

public class MutReplace extends ParentConsciousMutation {
	
	public void initiate(Program tree) {
		type = MutationType.replace;
		AST = tree;
	}
	
	public MutReplace() {
		type = MutationType.replace;
	}

	@Override
	public boolean Mutate(Node n) {
		if (n instanceof Replacable) {
			Node replacement = ((Replacable) n).getRandomReplacement(AST);
			
			if (replacement == null) {
				return false;
			}
			
			parent.replaceKid(n, replacement.copy());
			
			return true;
		}
		
		return false;
	}
	
	/*public void loadAncestors(int place) {
		ancestors = getAncestors(place);
	}
	
	public Node [] getAncestors(int place) {
		if (place == 0){
			return new Node[] {AST};
		}
		Node [] now = getAncestors(findparent(AST,place));
		Node [] next = new Node [now.length + 1];
		for (int there = 0; there < now.length; there ++){
			next[there] = now[there];
		}
		next[now.length] = AST.nodeAt(place);
		return next;
	}*/

	@Override
	public String type() {
		return "The node and its children were replaced by another node in the AST";
	}
	
}
