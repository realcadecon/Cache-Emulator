
import java.util.ArrayList;
import java.lang.Math;

public class Cache {
	
	private int cacheSize, dataBlockSize, associativity, replacementPolicy, writePolicy, missPolicy;
	private ArrayList<ArrayList<Line>> data;
	private RAM ram;
	private ArrayList<Integer> LRU; // Holds the U-bits for LRU-replacement policy

	public Cache(int cacheSize, int dataBlockSize, int associativity, int replacementPolicy, 
			int writePolicy, int missPolicy) {
		
		this.cacheSize = cacheSize;
		this.dataBlockSize = dataBlockSize;
		this.associativity = associativity;
		this.replacementPolicy = replacementPolicy;
		this.writePolicy = writePolicy;
		this.missPolicy = missPolicy;
		
		int number_of_sets = this.cacheSize / (this.associativity * this.dataBlockSize); //calculating number of sets
		this.data = new ArrayList<ArrayList<Line>>(0); //creates actual cache

		// initializes the cache
		for (int i = 0; i < number_of_sets; i++) {
			ArrayList<Line> insert = new ArrayList<Line>(0);
			for(int j = 0; j < this.associativity; j++)
			{
				insert.add(new Line(this.dataBlockSize));
			}
			data.add(insert);
		}

		// initializes the LRU U-bit array if necessary
		// each index in the LRU array will hold a 0 or 1 to represent that 
		// line 0 of 1 is the least recently used line in the pair and therefore
		// should be the replaced line if all lines are full.
		if (this.replacementPolicy == 2) {
			LRU = new ArrayList<Integer>((number_of_sets * associativity) / 2);
		}

	}
	
	public void setMemory(RAM ram) {
		this.ram = ram;
	}
	
	public void cacheRead(String hex) {
		int address = Integer.parseInt(hex, 16);
		String binAddress = Integer.toBinaryString(address);
		int blockOffset = Integer.parseInt(binAddress.substring((int) (binAddress.length() - (Math.log(this.dataBlockSize)/Math.log(2)))));
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

class Line {
	private int valid;
	private int tag;
	private ArrayList<Byte> block;

	public Line (int blockSize) {
		valid = 0;
		tag = 0;
		this.block = new ArrayList<Byte>(blockSize);
	}

	public int getValid() {
		return this.valid;
	}

	public int getTag() {
		return this.tag;
	}

	public void setValid(int v) {
		this.valid = v;
	}

	public void setTag(int t) {
		this.tag = t;
	}
}


