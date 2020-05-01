// File: RAM
// Author(s): Cade Conner and William Henry
// Date: 04/29/2020
// Section: CC: 510, WH: 508
// E-mail: cadejconner@tamu.edu 
// Description: RAM contains data members and functions needed for our memory.
// e.g. The content of this file implements...

import java.util.ArrayList;

public class RAM {
	private ArrayList<String> memory;
	
	
	public RAM() {
		memory = new ArrayList<String>();
	}
	
	public void setMem(ArrayList<String> memory) {
		this.memory = memory;
	}
	
	public void addMemory(String data) {
		memory.add(data);
	}
	
	public String getByte(int adrs) {
		return memory.get(adrs);
	}

	public void setByte(int adrs, String data) {
		memory.set(adrs, data);
	}

	
	public String toString() {
		String output = "";
		for(int i = 0; i<memory.size(); i++) {
			output += memory.get(i) + "\n";
		}
		return output;
	}
	
}
