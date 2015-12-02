package RequestHandler;

import java.util.ArrayList;

public class LogEntry {
	
	int version;
	public ArrayList<HexUpdate> updates;
	public ArrayList<Integer> deadCritters;

	public LogEntry(ArrayList<HexUpdate> ups, ArrayList<Integer> dead) {
		updates = ups;
		deadCritters = dead;
	}
	
	public LogEntry() {
		
	}
	
	/**Merges entry with this log entry. The updates from entry take precedence.*/
	public void mergeLaterEntry(LogEntry entry) {
		for (HexUpdate update : entry.updates) {
			for (HexUpdate ownUpdate : this.updates) {
				if (ownUpdate.row == update.row && ownUpdate.col == update.col) {
					ownUpdate.updatedValue = update.updatedValue;
				}
			}
		}
		for (Integer i : entry.deadCritters) {
			deadCritters.add(i);
		}
	}
	
}
