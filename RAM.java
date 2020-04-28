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

	
	public String toString() {
		String output = "";
		int j = 0;
		for(int i = 0; i<memory.size(); i++) {
			if(j<=15) {
				output += memory.get(i) + " | ";
				j++;
			}
			else {
				output+="\n";
				j=0;	
			}
		}
		return output;
	}
	
	
}
