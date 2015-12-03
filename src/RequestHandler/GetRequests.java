package RequestHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import RequestHandler.BundleFactory.CritListBundle;
import RequestHandler.BundleFactory.Inhabitant;
import world.Critter;
import world.World;

public class GetRequests {
	Gson gson = new Gson();
	/**Effect: Uploads to the response a list of all the critters the user has access to <br>
	 * Effect: sets the response status according to the spec.
	 */
	public void handleGetCritterList(int sessionID, String accessLevel, World w, HttpServletResponse r) {
		CritListBundle clb = new CritListBundle(w);
		if (accessLevel.equals("admin")) {
			for (Critter c : w.critters.values()) {
				clb.addInhabitant(getFullCritterBundle(c));
			}
		}
		else {
			for (Critter c : w.critters.values()) {
				Inhabitant i = c.godId == sessionID ? getFullCritterBundle(c) : getWeakCritterBundle(c);
				clb.addInhabitant(i);
			}
		}
		try {
			r.getWriter().append(gson.toJson(clb));
		} catch (IOException e) {
			e.printStackTrace();
		}
		r.addHeader("Content-Type", "application/json");
		r.setStatus(200);
		
	}
	
	/**Effect: gets a single critter's information*/
	public void handleGetCritter(int sessionID, String accessLevel, World w, HttpServletResponse r, Critter c) {
		Inhabitant i;
		i = accessLevel.equals("admin") || sessionID == c.godId ? getFullCritterBundle(c) : getWeakCritterBundle(c);
		try {
			r.getWriter().append(gson.toJson(i));
		} catch (IOException e) {
			e.printStackTrace();
		}
		r.addHeader("Content-Type", "application/json");
		r.setStatus(200);
	}
	
	/**Get's the limited version of the critter bundle*/
	public static Inhabitant getWeakCritterBundle(Critter c) {
		Inhabitant i = new Inhabitant(c);
		i.program = null;
		i.recently_executed_rule = -1; //I'm setting it to -1 to suggest that
		//there was no recently executed rule. This might not be a good way to
		//handle the situation though
		return i;
	}
	
	/**Gets the heavyweight critter bundle version*/
	public static Inhabitant getFullCritterBundle(Critter c) {
		return new Inhabitant(c);
	}

}
