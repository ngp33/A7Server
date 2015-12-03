package RequestHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
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

@WebServlet("/")
public class ServerRequestHandler extends HttpServlet {
	
	World w;
	HashMap<String, String> LevelPassword; //A mapping of levels to passwords
	HashMap<Integer, String> sessIDAccessLevel; //A mapping of sessionIDs to AccessLevels so that
	//the mapping is easily changed and the code is more readable.
	BundleFactory bf;
	AdminBundles ab;
	Gson gson = new Gson(); //I sort of don't like giving values to the instance variables here...
	Random rando = new Random(); //Initialize at some point TODO
	ParserImpl pi = new ParserImpl();
	/**invariant--is true when the world is running continuously and false otherwise.*/
	boolean running;
	Timer timer;
	PostRequests pr;
	
	/** Version of the world running on the server. Increments when:<br>
	 * 	    - The world steps<br>
	 * 	    - A critter is added<br>
	 *      - Many critters are added at once at random locations<br>
	 * 	    - A critter is removed<br>
	 * 	    - A new world is loaded<br>
	 */
	int version;
	WorldLog log;
	
	/** The session ID to assign to the next client that connects. Increments by 1 with each client.
	 * Assume it never reaches -1.
	 */
	int nextSessionID = 1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	class URIInfo { 
		
		String path;
		HashMap<String, String> queryParams = new HashMap<String, String>();
		
		public URIInfo(String URI) {
			String query = null;
			int queryStart = URI.indexOf("?");
			
			if (queryStart == -1 || queryStart == URI.length()-1) {
				path = URI;
				return;
			}
			
			query = URI.substring(queryStart + 1);
			for (String param : query.split("&")) {
				int eqIndex = param.indexOf("=");
				if (eqIndex == -1 || eqIndex == param.length()-1 || eqIndex == 0) {
					continue;
				}
				
				String key = query.substring(0, eqIndex);
				String value = query.substring(eqIndex+1);
				
				queryParams.put(key, value);
			}
		}

	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		URIInfo reqStringInfo = new URIInfo(request.getRequestURI());
		String URIPath = reqStringInfo.path;
		System.out.println(URIPath);
		int sessionID = Integer.parseInt(reqStringInfo.queryParams.get("session_id"));
		switch (splice(URIPath,2)) {
		case "CritterWorld/critters": //list all critters
			handleCritterList(sessionID, response);
			break;
		case "CritterWorld/critter": //retrieve a critter
			break;
		case "CritterWorld/world": //Get world or subsection of the world
			break;
		default:
			break;
		}
		System.out.println("GET sent to " + URIPath);
	}
	
	private void handleCritterList(int sessionID, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		URIInfo reqStringInfo = new URIInfo(request.getRequestURI());
		String URIPath = reqStringInfo.path;
		BufferedReader br = request.getReader();
		int sessionID;
		switch (splice(URIPath,2)) {
		case "CritterWorld/login":
			pr.handleLogin(gson.fromJson(br, AdminBundles.login.class), response);
			break;
		case "CritterWorld/critters":
			sessionID = Integer.parseInt(reqStringInfo.queryParams.get("session_id"));
			pr.handleLoadNewCritters(gson.fromJson(br, BundleFactory.CritPlacementBundle.class), sessionID, response);
			break;
		case "CritterWorld/world":
			w = new World(); //A little confused on the json for reading from a new world
			//It looks like we might need to make use of loadworld from console.
			bf = new BundleFactory(w);
			pr.handleNewWorld();
			//FROM THE FILE??? TODO
			break;
		case "CritterWorld/step":
			sessionID = Integer.parseInt(reqStringInfo.queryParams.get("session_id"));
			pr.handleStep(gson.fromJson(br, AdminBundles.numSteps.class), sessionID, response);
			break;
		case "CritterWorld/run":
			sessionID = Integer.parseInt(reqStringInfo.queryParams.get("session_id"));
			pr.handleRun(gson.fromJson(br, AdminBundles.runRate.class), sessionID, response);
			break;
		default:
			if (splice(URIPath, 2).equals("CritterWorld/world/create_entity")) {
				sessionID = Integer.parseInt(reqStringInfo.queryParams.get("session_id"));
				handleEntity(gson.fromJson(br, BundleFactory.Inhabitant.class), sessionID, response);
			}
			break;
		}
		System.out.println("POST sent to " + URIPath);
	}
	

	/*private void handleEntity(Inhabitant obj, int sessionID, HttpServletResponse response) throws IOException {
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

	private void handleRun(runRate Rate, int sessionID, HttpServletResponse response) throws IOException {
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
	/*private void runworld(double rate) {
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

	private void handleStep(numSteps numSteps, int sessionID, HttpServletResponse response) {
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

	private void handleNewWorld() {
		// TODO Auto-generated method stub
		
	}

	/**Invariant: The bundle is not malformatted (ie. it doesn't have both positions and num)
	 * Invariant: Num is a valid number*/
	/*private void handleLoadNewCritters(CritPlacementBundle cpb, int sessionID, HttpServletResponse r) {
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

	/**handles the login. <br>
	 * invariant: the level of access must be one of read, write, admin.
	 * @param info
	 * @param r
	 */
	/*private void handleLogin(AdminBundles.login info, HttpServletResponse r) {
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
		
	}*/

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		URIInfo reqStringInfo = new URIInfo(request.getRequestURI());
		String URIPath = reqStringInfo.path;
		
		System.out.println("DELETE sent to " + URIPath);
		
		if (URIPath.substring(0, 22).equals("/CritterWorld/critter/")) {
			String critterIdStr = URIPath.substring(22);
			int critterId = 0;
			try {
				critterId = Integer.parseInt(critterIdStr);
			} catch (NumberFormatException e) {
				response.setStatus(400);
				return;
			}
			
			String sessionIdStr = reqStringInfo.queryParams.get("session_id");
			if (sessionIdStr == null) {
				response.setStatus(400);
				return;
			}
			int sessionId = 0;
			try {
				sessionId = Integer.parseInt(sessionIdStr);
			} catch (NumberFormatException e) {
				response.setStatus(400);
				return;
			}
			
			Critter selected = w.getCritterById(critterId);
			String permLevel = sessIDAccessLevel.get(sessionId);
			
			if (permLevel == "admin" || selected.godId == sessionId) {
				LogEntry logEntry = new LogEntry();
				w.setHex(selected.row, selected.col, new Food(0), logEntry.updates);
				w.critters.remove(critterId);
				
				logEntry.deadCritters.add(critterId);
				
				version++;
				logEntry.version = version;
				log.log.addFirst(logEntry);
				
				response.setStatus(204);
			} else {
				response.setStatus(401);
			}
		}
		else {
			response.setStatus(400);
		}
	}
	
	private String splice(String uripath, int numelems) {
		String [] strs = uripath.split("/");
		StringBuilder sb = new StringBuilder();
		int itera = 0;
		while (itera < numelems && itera < strs.length) {
			sb.append(strs[itera]);
		}
		return sb.toString();
	}
	
	/**Returns true if the sessionID corresponds to write/admin access*/
	private boolean writeOrAdmin(int SessionID) {
		return !sessIDAccessLevel.get(SessionID).equals("read");
	}
	
	/**Returns true if the sessionID corresponds to admin access*/
	private boolean Admin(int SessionID) {
		return sessIDAccessLevel.get(SessionID).equals("admin");
	}
}
