package mutation;

/**
 * A factory that produces the public static Mutation objects corresponding to each
 * mutation
 */
import java.util.Random;

import ast.Program;

public class MutationFactory {
	
    public static Mutation getRemove() {
        return new MutRemove();
    }

    public static Mutation getSwap() {
        return new MutSwap();
    }

    public static Mutation getReplace() {
        return new MutReplace();
    }

    public static Mutation getTransform() {
        return new MutTransform();
    }

    public static Mutation getInsert() {
        return new MutInsert();
    }

    public static Mutation getDuplicate() {
    	return new MutDuplicate();
    }
    
    /** Returns an array of all the mutation objects*/
    public static Mutation[] allMuts(Program p){
    	Mutation mutone = getRemove();
    	Mutation muttwo = getSwap();
    	Mutation mutthree = getReplace();
    	Mutation mutfour = getTransform();
    	Mutation mutfive = getInsert();
    	Mutation mutsix = getDuplicate();
    	Mutation [] m = new Mutation [] {mutone, muttwo,
    			mutthree, mutfour, mutfive, mutsix
    	};
    	
    	for (int place = 0; place < 6; place ++){
    		m[place].initiate(p);
    	}
    	return m;
    	
    }
    
    /** Effect: randomizes an array of mutation objects using the in class algorithm*/
    public static void randMutation(Mutation [] total){
    	Random rand = new Random();
    	int temp;
    	Mutation holder;
    	for (int place = 0; place < total.length - 1; place ++){
    		temp = rand.nextInt(total.length - place - 1);
    		holder = total[place];
    		total[place] = total[temp];
    		total[temp] = holder;
    	}
    }
}
