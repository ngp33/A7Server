package util;
import org.junit.*;
import org.junit.Assert.*;

public class MultiThreadTesting {
	Thread one;
	Thread two;
	RingBuffer<Integer> rbi;
	
	@Before
	public void init() {
		rbi = new RingBuffer<Integer>(10);
	}
	
	@Test
	public void addRemove() {
		one = new Thread() {
			public void run() {
				for (int x = 0; x < 15; x ++) {
					rbi.add(x);
					//System.out.print(rbi.size());
					//System.out.println(" " + rbi.tail +"," + rbi.head);
					System.out.println(x);
				}
			}
		};
		one.start();

		for (int x = 0; x < 5; x ++) {
			rbi.remove();
			if (x == 3) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("there");
		}
		try {
			one.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
