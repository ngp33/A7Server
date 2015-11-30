package ast;

public class Updatell {

	public Update head = new Update(null,null);
	public Update last = head;
	
	
	public Updatell(){
	}
	/**
	 * appends the new node to the end of the linked list
	 * @param n
	 * 		the node to be added at the end
	 */
	public void add(Update n){
		if (head == null){
			head = n;
		}
		last.next = n;
		last = last.next;
	}
	
	public int size(){
		Update current = head.next;
		int s = 0;
		while (current!=null){
			s++;
			current = current.next;
		}
		return s;
	}
	
	public Update [] toarray(){
		Update [] r = new Update[size()];
		Update now = head.next;
		int place = 0;
		while (now != null){
			r[place] = now;
			now = now.next;
			place++;
		}
		return r;
	}
}