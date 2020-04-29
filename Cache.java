
import java.util.ArrayList;
import java.lang.Math;

public class Cache {
	private static final int ADDRESS_SIZE = 8;
	private int cacheSize, dataBlockSize, associativity, replacementPolicy, writePolicy, missPolicy;
	private int numberOfSets, blockOffsetStartingBit, setIndexStartingBit;
	private ArrayList<ArrayList<Line>> data;
	private ArrayList<Integer> LRU; // Holds the U-bits for LRU-replacement policy

	public Cache(int cacheSize, int dataBlockSize, int associativity, int replacementPolicy, 
			int writePolicy, int missPolicy) {
		
		this.cacheSize = cacheSize;
		this.dataBlockSize = dataBlockSize;
		this.associativity = associativity;
		this.replacementPolicy = replacementPolicy;
		this.writePolicy = writePolicy;
		this.missPolicy = missPolicy;
		
		numberOfSets = this.cacheSize / (this.associativity * this.dataBlockSize); //calculating number of sets
		this.data = new ArrayList<ArrayList<Line>>(0); //creates 

		// initializes the cache
		for (int i = 0; i < numberOfSets; i++) {
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
			LRU = new ArrayList<Integer>((numberOfSets * associativity) / 2);
		}

		this.blockOffsetStartingBit = Cache.ADDRESS_SIZE - (int) (Math.log(this.dataBlockSize)/Math.log(2));
		this.setIndexStartingBit = Cache.ADDRESS_SIZE - this.blockOffsetStartingBit - (int) (Math.log(this.numberOfSets)/Math.log(2));

	}
	
	public void cacheRead(String hex) {
		int address = Integer.parseInt(hex, 16);
		String binAddress = Integer.toBinaryString(address);
		while (binAddress.length() < Cache.ADDRESS_SIZE) {
			binAddress = "0" + binAddress;
		}
		int blockOffset = Integer.parseInt(binAddress.substring(this.blockOffsetStartingBit, 2));
		int setIndex = Integer.parseInt(binAddress.substring(this.setIndexStartingBit, this.blockOffsetStartingBit), 2);
		int tag = Integer.parseInt(binAddress.substring(0, this.setIndexStartingBit));
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


