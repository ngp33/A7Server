package ast;

public class Rulesll {
	
	public Rule head = new Rule(null,null);
	public Rule last = head;
	
	
	public Rulesll(){
	}
	/**
	 * appends the new node to the end of the linked list
	 * @param n
	 * 		the node to be added at the end
	 */
	public void add(Rule n){
		if (head == null){
			head = n;
		}
		last.next = n;
		last = last.next;
	}
	
	public int size(){
		Rule current = head.next;
		int s = 0;
		while (current!=null){
			s++;
			current = current.next;
		}
		return s;
	}
	
	public Rule [] toarray(){
		Rule [] r = new Rule[size()];
		Rule now = head.next;
		int place = 0;
		while (now != null){
			r[place] = now;
			now = now.next;
			place++;
		}
		return r;
	}
}
