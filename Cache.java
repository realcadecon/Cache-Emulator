import java.util.ArrayList;

public class Cache {
	
	private int cacheSize, dataBlockSize, associativity, replacementPolicy, writePolicy, missPolicy;
	public ArrayList<ArrayList<Line>> data;

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

		for (int i = 0; i < number_of_sets; i++) {
			ArrayList<Line> insert = new ArrayList<Line>(0);
			for(int j = 0; j < this.associativity; j++)
			{
				insert.add(new Line(this.dataBlockSize));
			}
			data.add(insert);
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
	private boolean valid;
	private int tag;
	private ArrayList<Byte> block;

	public Line (int blockSize) {
		valid = false;
		tag = 0;
		this.block = new ArrayList<Byte>(blockSize);
	}

	public boolean getValid() {
		return this.valid;
	}

	public int getTag() {
		return this.tag;
	}

	public void setValid(boolean v) {
		this.valid = v;
	}

	public void setTag(int t) {
		this.tag = t;
	}
}


