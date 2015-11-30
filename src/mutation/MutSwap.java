package mutation;

import ast.Node;
import ast.Program;

public class MutSwap extends MutationImpl {
	
	public void initiate(Program tree) {
		type = MutationType.swap;
		AST = tree;
	}
	
	public MutSwap() {
		type = MutationType.swap;
	}

	@Override
	public boolean Mutate(Node n) {
		if (n instanceof Swappable) {
			((Swappable) n).swapKids();
			return true;
		}

		return false;
	}

	@Override
	public String type() {
		return "Two children of a node switched places with one another";
	}
	
	

}
