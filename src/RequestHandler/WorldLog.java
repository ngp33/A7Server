package RequestHandler;

import java.util.ArrayList;
import java.util.LinkedList;

public class WorldLog {

	/**Invariant: Every entry in the log should have a higher version than its successor in the list.*/
	LinkedList<LogEntry> log;
	
	private static boolean updateAlreadyExists(ArrayList<HexUpdate> diff, HexUpdate toCheck) {
		for (HexUpdate update : diff) {
			if (update.row == toCheck.row && update.col == toCheck.col) {
				return true;
			}
		}
		return false;
	}
	
	/**Returns a list of updates between the latest version in the log and oldVersion.*/
	public ArrayList<HexUpdate> getDiff(int oldVersion) {
		ArrayList<HexUpdate> diff = new ArrayList<HexUpdate>();
		
		for (LogEntry entry : log) {
			if (entry.version <= oldVersion) {
				return diff;
			}
			for (HexUpdate update : entry.updates) {
				if (!updateAlreadyExists(diff, update)) {
					diff.add(update);
				}
			}
		}
		
		return diff;
	}

}
