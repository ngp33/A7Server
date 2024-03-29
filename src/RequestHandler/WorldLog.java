package RequestHandler;

import java.util.ArrayList;
import java.util.LinkedList;

public class WorldLog {

	/**Invariant: Every entry in the log should have a higher version than its successor in the list.*/
	LinkedList<LogEntry> log;
	
	public static boolean updateAlreadyExists(ArrayList<HexUpdate> diff, HexUpdate toCheck) {
		for (HexUpdate update : diff) {
			if (update.row == toCheck.row && update.col == toCheck.col) {
				return true;
			}
		}
		return false;
	}
	
	//This could probably be simplified using the merge method but I don't want to change anything around yet.
	/**Returns a list of updates between the latest version in the log and oldVersion.*/
	public LogEntry getDiff(int oldVersion) {
		ArrayList<HexUpdate> diff = new ArrayList<HexUpdate>();
		ArrayList<Integer> deadCritters = new ArrayList<Integer>();
		
		for (LogEntry entry : log) {
			if (entry.version <= oldVersion) {
				break;
			}
			for (HexUpdate update : entry.updates) {
				if (!updateAlreadyExists(diff, update)) {
					diff.add(update);
				}
			}
			for (Integer id : entry.deadCritters) {
				deadCritters.add(id);
			}
		}
		
		return new LogEntry(diff, deadCritters); //TODO make it so the new logEntry has the correct version num.
	}
	
	public LogEntry getDiffBounded(int oldVersion, int rowMin, int rowMax, int colMin, int colMax) {
		ArrayList<HexUpdate> diff = new ArrayList<HexUpdate>();
		ArrayList<Integer> deadCritters = new ArrayList<Integer>();
		
		for (LogEntry entry : log) {
			if (entry.version <= oldVersion) {
				break;
			}
			for (HexUpdate update : entry.updates) {
				if (update.col <= colMax && update.col >= colMin
						&& update.row <= rowMax && update.row >= rowMin) {
					if (!updateAlreadyExists(diff, update)) {
						diff.add(update);
					}
				}
				for (Integer id : entry.deadCritters) {
					deadCritters.add(id);
				}
			}
		}
		
		return new LogEntry(diff, deadCritters);
	}

	/*public void mergeToPreviousVersion(int endVersion) {
		ArrayList<HexUpdate> updates = getDiff(endVersion);
		
		LogEntry head = log.peek();
		while (head != null && head.version >= endVersion) {
			log.remove();
			head = log.peek();
		}
	}*/

}
