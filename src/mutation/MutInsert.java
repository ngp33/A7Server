package mutation;

import ast.Node;
import ast.Program;

public class MutInsert extends ParentConsciousMutation {
	
	public void initiate(Program tree) {
		type = MutationType.insert;
		AST = tree;
	}
	
	public MutInsert() {
		type = MutationType.insert;
	}

	@Override
	public boolean Mutate(Node n) {
		if (n instanceof Reparentable) {
			Insertable toInsert = ((Reparentable) n).getNewParent();
			if (toInsert.fillInMissingKids(AST)) {
				parent.replaceKid(n, (Node) toInsert);
			
				return true;
			}
			
			return false;
		}
		
		return false;
	}

	@Override
	public String type() {
		return "A random node is inserted as the parent of the mutated node";
	}

}
