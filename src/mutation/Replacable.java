package mutation;

import ast.Node;
import ast.Program;

public interface Replacable {

	public Node getRandomReplacement(Program possibleKids);
	
}
