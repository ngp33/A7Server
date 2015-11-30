package ast;

import java.io.FileNotFoundException;
import java.io.FileReader;
import parse.Parser;
import parse.ParserImpl;


public class Main {
	public static void main(String args[]){
		StringBuilder s = new StringBuilder();
		if (args.length == 1){
			Program q = open(args[0]);
			if (q != null){
				System.out.println(q.prettyPrint(s));
			}
			 //TODO make sure this catches invalid syntax in some manageable way
		}
		else if (args[0].equals("--mutate")){
			//TODO make it print out what happened in the mutation. ie. what type of mutation at least.
			Program g = open(args[2]);
			if (g != null) {
				System.out.println("ORIGINAL:");
				System.out.println(g.prettyPrint(s));
				s = new StringBuilder();
				
				for (int i = 0; i < Integer.parseInt(args[1]); i++){
					g.mutate();
					System.out.println("MUTATION " + i + ":");
					System.out.println(g.prettyPrint(s));
					System.out.println(((ProgramImpl) g).Mutationtype);
					s = new StringBuilder();
				}
			} else {
				System.out.println("The file contains a syntax error.");
			}
		}
		
	}
	
	
	
	private static Program open(String filename){
		FileReader f;
		Program q = null;
		try {
			f = new FileReader(filename);
			Parser p = new ParserImpl();
			q = p.parse(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return q;
	}

}
