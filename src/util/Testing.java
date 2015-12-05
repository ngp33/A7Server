package util;

import org.junit.*;
import static org.junit.Assert.*;

public class Testing {
	RingBuffer<String> rbf;

	@Before
	public void init() {
		rbf = new RingBuffer<String> (15);
	}
	
	@Test
	public void addoffer() {
		rbf.add("one");
		rbf.offer("anotheradd");
		assertTrue(rbf.remove().equals("one"));
		assertTrue(rbf.remove().equals("anotheradd"));
		//assertTrue(rbf.t_array[0] == null);
		//assertTrue(rbf.t_array[1] == null);
		assertTrue(rbf.head == 2);
		assertTrue(rbf.tail == 2);
	}
	
	@Test
	public void addAlot() {
		for (int x = 0; x < 15; x ++) {
			rbf.add("dfd");
		}
	}
	
	@Test
	public void addOffset() {
		rbf.head = 3;
		rbf.tail = 3;
		for (int x = 0; x < 14; x ++) {
			rbf.add("dfs");
		}
		for (int x = 0; x < 14; x ++) {
			rbf.remove();
		}
		assertTrue(rbf.isEmpty());
	}
	
	@Test
	public void empty() {
		assertTrue(rbf.isEmpty());
		rbf.add("hello.");
		assertFalse(rbf.isEmpty());
	}
	
	@Test
	public void equals() {
		rbf.add("hello");
		assertFalse(rbf.equals(new Object [2]));
		RingBuffer<String> bfg = new RingBuffer<String> (15);
		bfg.add("hello");
		assertTrue(rbf.equals(bfg));
		bfg.remove();
		bfg.add("hello");
		assertTrue(rbf.equals(bfg));
	}

	
}
