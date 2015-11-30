package RequestHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import world.World;
//I added src repository to the buildpath so we could use world (I think). Hopefully this isn't a problem?
@WebServlet("/")
public class ServerRequestHandler extends HttpServlet {
	
	//World w;
	HashMap<Integer, String> LevelPassword;
	BundleFactory bf;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	class URIInfo { //could this maybe be a separate file? I'm not terribly fond of defining
		//a class within another class
		
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
		switch (URIPath) { //TODO I assume that URIPath itself will not be one of these values,
		//and that we will have to write some method to get this information
		case "CritterWorld/critters": //list all critters
			
		case "CritterWorld/critter": //retrieve a critter
			
		case "CritterWorld/world": //Get world or subsection of the world
			
		default:
			
		}
		System.out.println("GET sent to " + URIPath);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		URIInfo reqStringInfo = new URIInfo(request.getRequestURI());
		String URIPath = reqStringInfo.path;
		switch (URIPath) {
		case "CritterWorld/login":
			
		case "CritterWorld/critters":
			
		case "CritterWorld/world":
			
		case "CritterWorld/world/create_entity":
			
		case "CritterWorld/step":
			
		case "CritterWorld/run":
			
		default:
			
		}
		System.out.println("POST sent to " + URIPath);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		URIInfo reqStringInfo = new URIInfo(request.getRequestURI());
		String URIPath = reqStringInfo.path;
		
		System.out.println("DELETE sent to " + URIPath);
		
		if (URIPath.substring(0, 22).equals("/CritterWorld/critter/")) {
			
		}
		else {
			response.setStatus(400); //Assume from now on that all URIs are valid.
		}
	}
	
}
