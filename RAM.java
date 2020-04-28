import java.util.ArrayList;

public class RAM {
	private ArrayList<String> memory;
	
	
	public RAM() {
		memory = new ArrayList<String>();
	}
	
	public void addMemory(String data) {
		memory.add(data);
	}
	
	public String getByte(int adrs) {
		return memory.get(adrs);
	}
}
