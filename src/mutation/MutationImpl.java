package mutation;

import ast.Program;

public abstract class MutationImpl implements Mutation {
	
	Program AST;
	MutationType type;
	
	@Override
	public boolean equals(Mutation m) {
		return type.equals(((MutationImpl) m).type);
	}
	
	public enum MutationType {
		remove, swap, replace, transform, insert, duplicate
	}

}
