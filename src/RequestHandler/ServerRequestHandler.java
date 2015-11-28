package RequestHandler;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class ServerRequestHandler extends HttpServlet {

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
		
		System.out.println("GET sent to " + URIPath);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		URIInfo reqStringInfo = new URIInfo(request.getRequestURI());
		String URIPath = reqStringInfo.path;
		
		System.out.println("POST sent to " + URIPath);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		URIInfo reqStringInfo = new URIInfo(request.getRequestURI());
		String URIPath = reqStringInfo.path;
		
		System.out.println("DELETE sent to " + URIPath);
		
		if (! URIPath.substring(0, 22).equals("/CritterWorld/critter/")) {
			response.setStatus(400);
		}
	}
	
}
