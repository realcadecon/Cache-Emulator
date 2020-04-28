import java.util.ArrayList;

public class Cache {
	
	private int cacheSize, dataBlockSize, associativity, replacementPolicy, writePolicy, missPolicy;
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
		
		int number_of_sets = this.cacheSize / (this.associativity * this.dataBlockSize);
		this.data = new ArrayList<ArrayList<Line>>(0);

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
		if (this.replacementPolicy == 2) {
			LRU = new ArrayList<Integer>((number_of_sets * associativity) / 2);
		}

	}
	
	public void cacheRead(String hex) {
		
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


