package ast;

import world.Critter;

public class Sensespacked {
	public static int sniff(Critter c, Sensespace s){
		switch (s.pres){
		case ahead:
			return c.ahead(s.only.value(c));
		case nearby:
			return c.nearby(s.only.value(c));
		case random:
			return c.random(s.only.value(c));
		default:
			return 0;
		}
	}
}