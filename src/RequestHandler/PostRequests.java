package RequestHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import RequestHandler.AdminBundles.numSteps;
import RequestHandler.AdminBundles.runRate;
import RequestHandler.BundleFactory.CritPlacementBundle;
import RequestHandler.BundleFactory.Inhabitant;
import RequestHandler.BundleFactory.Placement;
import ast.ProgramImpl;
import console.Console;
import parse.ParserImpl;
import world.Critter;
import world.Food;
import world.Rock;
import world.World;

public class PostRequests {
	HashMap<String, String> LevelPassword; //A mapping of levels to passwords
	HashMap<Integer, String> sessIDAccessLevel; //A mapping of sessionIDs to AccessLevels so that
	Gson gson;
	Random rando;
	/** Version of the world running on the server. Increments when<br>
	 * 	    - The world steps<br>
	 * 	    - A critter is added<br>
	 *      - Many critters are added at once at random locations<br>
	 * 	    - A critter is removed<br>
	 * 	    - A new world is loaded<br>
	 */
	int version;
	Timer timer;
	boolean running;
	ParserImpl pi = new ParserImpl();
	World w;
	WorldLog log;
	/**invariant: This will always have the rate at which the world is running*/
	double rate;
	
	/**Effect: If the user successfully provides proper login information, this
	 * assigns them a sessionID and ensures that they will receive all the rights
	 * that their clearance level affords them. <br>
	 * Effect: Updates the response according to the spec*/
	public void handleLogin(AdminBundles.login info, HttpServletResponse r) {
		if (info.password.equals(LevelPassword.get(info.level))) {
			r.addHeader("Content-Type", "application/json");
			r.setStatus(200);
			PrintWriter pw;
			try {
				pw = r.getWriter();
				int sessID = sessIDAccessLevel.size();
				String s = gson.toJson(new AdminBundles.SessID(sessID));
				pw.append(s);
				switch (info.level) {
				case "read":
					sessIDAccessLevel.put(sessID, "read");
					break;
				case "write":
					sessIDAccessLevel.put(sessID, "write");
					break;
				case "admin":
					sessIDAccessLevel.put(sessID, "admin");
					break;
				}
				pw.flush();
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			r.setStatus(401);
		}
		
	}
	/**Effect: Given that the user who calls this method has proper clearance, this creates
	 * a new Entity (rock or food) at the specified location. <br>
	 * Effect: updates the response according to the spec.
	 * @param obj
	 * @param sessionID
	 * @param response
	 * @throws IOException
	 */
	public void handleEntity(Inhabitant obj, int sessionID, HttpServletResponse response) throws IOException {
		response.addHeader("Content-Type", "text/plain");
		if (writeOrAdmin(sessionID)) {
			if (w.getNumRep(new int[] { obj.row, obj.col }) == 0) { //checks that the hex is empty
				LogEntry logEntry = new LogEntry();
				
				if (obj.type.equals("rock")) {
					w.replace(new Rock(), w.getHex(obj.row, obj.col), logEntry.updates);
				}
				else {
					w.replace(new Food(obj.amount), w.getHex(obj.row, obj.col), logEntry.updates);
				}
				
				version++;
				logEntry.version = version;
				log.log.add(logEntry);
				
				response.setStatus(201);
				response.getWriter().append("Ok");
			} else {
				response.setStatus(406);
			}
		} else {
			response.setStatus(401);
		}

	}
	
	/**Invariant: The bundle is not malformatted (ie. it doesn't have both positions and num) <br>
	 * Invariant: Num is a valid number<br>
	 * Effect: Uses the critterPlacementbundle to load new critters either randomly or to specific
	 * locations in the world, given that the person who makes the call has write or admin access.<br>
	 * Effect: updates the servletresponse*/
	public void handleLoadNewCritters(CritPlacementBundle cpb, int sessionID, HttpServletResponse r) {
		if (writeOrAdmin(sessionID)) {
			LogEntry logEntry = new LogEntry();
			
			Critter c = new Critter(cpb.mem, rando, (ProgramImpl) pi.parse(new StringReader(cpb.program)), w);
			c.name = cpb.species_id == null ? c.genes.toString() : cpb.species_id; 
			//The above line ensures that all critters have a species.
			c.godId = sessionID;
			if (cpb.positions == null) {
				Console.randomPlacement(w, c, cpb.num, c.r);

			} else {
				for (Placement p : cpb.positions) {
					if (w.isInGrid(p.row, p.col)) {
						Critter k = c.copy();
						k.row = p.row;
						k.col = p.col;
						k.direction = 0;
						w.addCritter(k);
						w.replace(k, w.getHex(k.row, k.col), logEntry.updates);
					} else {
						// TODO notify user of failure somehow
					}
				}
			}
			
			version++;
			logEntry.version = version;
			log.log.addFirst(logEntry);
			
			r.setStatus(201);
		}
		
	}
	
	/**Effect: Runs the world at the specified rate given that the person who makes the call
	 * has write or admin status. <br>
	 * Effect: updates the response
	 * @param Rate
	 * @param sessionID
	 * @param response
	 * @throws IOException
	 */
	public void handleRun(runRate Rate, int sessionID, HttpServletResponse response) throws IOException {
		response.addHeader("Content-Type", "text/plain");
		if (writeOrAdmin(sessionID)) {
			if (Rate.rate > 0) {
				running = true;
				runworld(Rate.rate);
				response.setStatus(200);
				response.getWriter().append("Ok");
			}
			else if (Rate.rate == 0) {
				running = false;
				runworld(Rate.rate);
				response.setStatus(200);
				response.getWriter().append("Ok");
			} else {
				response.setStatus(406);
			}
		} else {
			response.setStatus(401);
		}
		
	}
	
	/**Effect: runs the world at the specified rate*/
	private void runworld(double rate) {
		this.rate = rate;
		if (rate == 0d) {
			timer.cancel();
			return;
		}
		
		long period = (long) (1000d/rate + .5d); //Round
		
		timer = new Timer(true);
		timer.schedule(new TimerStepHandler(), 0l, period);
	}
	
	private class TimerStepHandler extends TimerTask {

		@Override
		public void run() {
			synchronized(w) {
				LogEntry logEntry = w.advance();
				version++;
				logEntry.version = version;
				log.log.addFirst(logEntry);
			}
		}
		
	}

	/**Effect: If the user has write or admin access and the world is not currently running, the world
	 * steps once. <br>
	 * Effect: response is updated accordingly
	 * @param numSteps
	 * @param sessionID
	 * @param response
	 */
	public void handleStep(numSteps numSteps, int sessionID, HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		if (writeOrAdmin(sessionID)) {
			if (!running) { 
				int counter = numSteps.getStepnum();
				
				LogEntry logEntry = w.advanceTime(counter);
				version++;
				logEntry.version = version;
				log.log.addFirst(logEntry);
				
				response.setStatus(200);
				try {
					response.getWriter().append("Ok");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				response.setStatus(406);
			}
		} else {
			response.setStatus(401);
		}

	}

	
	
	
	/**Returns true if the sessionID corresponds to write/admin access*/
	private boolean writeOrAdmin(int SessionID) {
		return !sessIDAccessLevel.get(SessionID).equals("read");
	}
	
	public String accessLevel(int SessionID) {
		return sessIDAccessLevel.get(SessionID);
	}

}
