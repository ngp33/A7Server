package ast;

import java.util.Random;

import world.Critter;
import mutation.Mutation;
import mutation.MutationFactory;
import mutation.MutationImpl;
import mutation.ParentConsciousMutation;

import java.util.Random;

/**
 * A data structure representing a critter program.
 *
 */
public class ProgramImpl extends Manykids implements Program {

	/**
	 * Initializes the programimpl
	 * @param args	the rules that will go into the ProgramImpl.
	 */
	public String Mutationtype = ""; //this is what we will print for the type of mutation.
	
	public ProgramImpl(Rulesll r){
		children = r.toarray();
	}

    public ProgramImpl() {}

	/*@Override
    public Program mutate() {
		Mutation [] muttypes = MutationFactory.allMuts(this);
		String Mutationtype = ""; //this is what we will print for the type of mutation. 
		Random R = new Random();
        
		int p = R.nextInt(size());
		MutationFactory.randMutation(muttypes);
		int place = 0;
		while (!usedMutate(p, muttypes[place])) {
			place++;
		}
		//TODO update the Mutationtype string
		Mutationtype = muttypes[place].type();
        return this;
    }*/
	
	public ProgramImpl(Rule[] rules) {
		children = rules;
	}

	@Override
    public Program mutate() {
		Mutation [] muttypes = MutationFactory.allMuts(this);
		Random R = new Random();
        
		//int p = R.nextInt(size());
		MutationFactory.randMutation(muttypes);
		
		for (int p : getRandomSearchOrder()) {
			for (int i = 0; i < muttypes.length; i++) {
				if (usedMutate(p, muttypes[i])) {
					Mutationtype = muttypes[i].type();
					return this;
				}
			}
		}
		
		return null;
    }

    @Override
    public Program mutate(int index, Mutation m) {
    	if (usedMutate(index, m)) {
    		return this;
    	}
        return null;
    }
    
    /** executes mutation m on the node and returns true if the mutation was legal*/
    public boolean usedMutate(int index, Mutation m) {
    	if (m instanceof ParentConsciousMutation) {
    		((ParentConsciousMutation) m).findparent(this, index);
    	}
    	return m.Mutate(nodeAt(index));
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        for (Node them : children){
        	them.prettyPrint(sb);
        	sb.append("\n");
        }
        return sb;
    }
    
   /* @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }*/
    
    /**Returns the list of all the nodes in the AST. The idea is that
     * this will help when it comes time to randomly pick a node to mutate
     * @return
     * 		Node[]
     */
    private Node [] thelist(){
    	Node [] n = new Node [size()];
    	for (int place = 0; place < size(); place ++){
    		n[place] = nodeAt(place);
    	}
    	return n;
    }

	@Override
	Manykids getRootCopy() {
		return new ProgramImpl();
	}
	
	public int[] getRandomSearchOrder() {
		int size = this.size();
		int[] indexOrder = new int[size];
		
		for (int i = 0; i < size; i++) {
			indexOrder[i] = i;
		}
		
		Random rand = new Random();
        
        int j;
        int temp;
        for (int i = size-1; i > 0; i--) {
        	j = rand.nextInt(i);
        	temp = indexOrder[j];
        	indexOrder[j] = indexOrder[i];
        	indexOrder[i] = temp;
        }
        
        return indexOrder;
	}
	
	public Node getRandomNode(Class<?> type) {
		int[] indexOrder = getRandomSearchOrder();

        for (int i : indexOrder) {
        	Node n = this.nodeAt(i);
        	
        	if (type.isInstance(n)) {
        		return n;
        	}
        }
		
		return null;
	}
	
	private boolean notMemberOf(Node[] arr, Node n) {
		for (Node a : arr) {
			if (a == n) {
				return false;
			}
		}
		return true;
	}
	
	public Node getRandomNode(Class<?> type, Node[] ignoreList) {
		int[] indexOrder = getRandomSearchOrder();
		
		for (int i : indexOrder) {
        	Node n = this.nodeAt(i);
        	
        	if (type.isInstance(n) && notMemberOf(ignoreList, n)) {
        		return n;
        	}
        }
		
		return null;
	}
	
	/**Effect: Evaluates the program of a critter.
	 * Returns: true if a rule encountered committed an action
	 * @param c
	 * @return
	 */
	public boolean eval(Critter c){
		for (int place = 0; place < children.length; place ++) {
			if (((Rule) children[place]).perform(c)){
				c.mostrecentrule = (Rule) children[place];
				return true;
			}
		}
		return false;
	}


}
