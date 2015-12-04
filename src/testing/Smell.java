package testing;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;
import org.junit.*;
import static org.junit.Assert.*;

import RequestHandler.HexUpdate;
import ast.ProgramImpl;
import parse.ParserImpl;
import world.Critter;
import world.Food;
import world.World;

public class Smell {
	World w;
	ProgramImpl p;
	ParserImpl parsing;
	StringReader str;
	Critter c;
	ArrayList<HexUpdate> al;
	
	@Before
	public void init() {
		parsing = new ParserImpl();
		p = (ProgramImpl) parsing.parse(new StringReader ("1 = 1 --> wait;"));
		w = new World (7,5, "yre");
		//w.emptyworld();
		c = new Critter(new int [] {8,2,2,2,200,1,0,10}, new Random(), p, w);
		al = new ArrayList<HexUpdate>();
		w.replace(c, w.getHex(3, 3), al);
		c.direction = 0;
		}
	

	@Ignore
	@Test
	public void smell() {
		w.replace(new Food(100), w.getHex(4, 4), al);
		assertTrue(c.smell() == 1001);
		
		//w.replace(new Food(100), w.getHex(4, 2));
		//System.out.println(c.smell());
	}

	@Ignore
	@Test
	public void smelltwo() {
		w.replace(new Food(100), w.getHex(2, 3), al);
		System.out.println(c.smell());
		assertTrue(c.smell() == 3003);
	}
	
	@Test
	public void smellthree() {
		w.replace(new Food(100), w.getHex(2, 2), al);
		int ans = c.smell();
		System.out.println(c.smell());
		//assertTrue(c.smell() == 2004);
	}
}
