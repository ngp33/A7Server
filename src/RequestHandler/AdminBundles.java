package RequestHandler;

public class AdminBundles {
	
	public static class login {
		String level;
		String password;
		public login(String lvl, String pswd) {
			level = lvl;
			password = pswd;
		}
	}
	
	public static class SessID {
		int session_id;
		public SessID(int i) {
			session_id = i;
		}
	}
	
	public static class numSteps {
		int counter;
		public numSteps(int num) {
			counter = num;
		}
		
		/**Returns the number of steps the world should execute based on the numStep object's data*/
		public int getStepnum() {
			return counter == 0 ? 1 : counter;
		}
	}
	
	public static class runRate {
		int rate;
		public runRate(int speed) {
			rate = speed;
		}
	}

}
