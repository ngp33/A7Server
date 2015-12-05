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
import world.Rock;
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
		w = new World (11,7, "yre");
		//w.emptyworld();
		c = new Critter(new int [] {8,2,2,2,200,1,0,10}, new Random(), p, w);
		al = new ArrayList<HexUpdate>();
		w.replace(c, w.getHex(3, 3), al);
		c.direction = 0;
		}
	


	@Test
	public void smell() {
		w.replace(new Food(100), w.getHex(4, 4), al);
		assertTrue(c.smell() == 1001);
		
		//w.replace(new Food(100), w.getHex(4, 2));
		//System.out.println(c.smell());
	}


	@Test
	public void smelltwo() {
		w.replace(new Food(100), w.getHex(2, 3), al);
		assertTrue(c.smell() == 3003);
	}
	
	//@Ignore
	@Test
	public void smellthree() {
		w.replace(new Food(100), w.getHex(2, 2), al);
		w.replace(new Food(100), w.getHex(6, 5), al);
		assertTrue(c.smell() == 2004);
	}

	@Test
	public void smellfour() {
		c.direction = 1;
		w.replace(new Food(100), w.getHex(5, 5), al);
		assertTrue(c.smell() == 1000);
	}

	@Test
	public void smellfiveobstruction() {
		c.direction = 1;
		w.replace(new Rock(), w.getHex(4, 4), al);
		w.replace(new Food(100), w.getHex(5, 4), al);
		assertTrue(c.smell() == 3005);
	}

	@Test
	public void smellsixObstruction() {
		c.direction = 3;
		w.replace(new Rock(), w.getHex(4, 4), al);
		w.replace(new Rock(), w.getHex(5, 4), al);
		w.replace(new Food(100), w.getHex(4, 5), al);
		assertTrue(c.smell() == 3005);
		
	}
}
