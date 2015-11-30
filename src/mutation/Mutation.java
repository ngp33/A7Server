package mutation;

import ast.Node;
import ast.Program;

/**
 * A mutation to the AST
 */
public interface Mutation {
	
    /**
     * Compares the type of this mutation to {@code m}
     * 
     * @param m
     *            The mutation to compare with
     * @return Whether this mutation is the same type as {@code m}
     */
    boolean equals(Mutation m);
    
    /** Mutates a node*/
    boolean Mutate(Node n);
    
    public void initiate(Program p);
    
    public String type();
    
}
