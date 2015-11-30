package mutation;

import ast.Program;

public interface Insertable {
	
	public boolean fillInMissingKids(Program possibleKids);
	
}
