import java.util.ArrayList;

public class Cache {
	
	private int cacheSize, dataBlockSize, associativity, replacementPolicy, writePolicy, missPolicy;
	private ArrayList<String> cache;

	public Cache(int cacheSize, int dataBlockSize, int associativity, int replacementPolicy, 
			int writePolicy, int missPolicy) {
		
		this.cacheSize = cacheSize;
		this.dataBlockSize = dataBlockSize;
		this.associativity = associativity;
		this.replacementPolicy = replacementPolicy;
		this.writePolicy = writePolicy;
		this.missPolicy = missPolicy;
	}
	
	public void cacheRead() {
		
	}
	
	public void cacheWrite() {
		
	}
	
	public void cacheFlush() {
		
	}
	
	public void cacheView () {
		
	}
	
	public void memoryView () {
		
	}
	
	public void cacheDump () {
		
	}
	
	public void memoryDump () {
		
	}

	@Override
	public String toString() {
		return "Cache [cacheSize=" + cacheSize + ", dataBlockSize=" + dataBlockSize + ", associativity=" + associativity
				+ ", replacementPolicy=" + replacementPolicy + ", writePolicy=" + writePolicy + ", missPolicy="
				+ missPolicy + "]";
	}
	
	
	
}
