
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Math;

public class Cache {
	private static final int ADDRESS_SIZE = 8;
	private int cacheSize, dataBlockSize, associativity, replacementPolicy, writePolicy, missPolicy;
	private int numberOfSets, blockOffsetStartingBit, setIndexStartingBit;
	private ArrayList<ArrayList<Line>> data;
	private RAM ram;
	private ArrayList<Integer> LRU; // Holds the U-bits for LRU-replacement policy
	PrintWriter outCache, outRAM;
	

	public Cache(int cacheSize, int dataBlockSize, int associativity, int replacementPolicy, 
			int writePolicy, int missPolicy) {
		
		this.cacheSize = cacheSize;
		this.dataBlockSize = dataBlockSize;
		this.associativity = associativity;
		this.replacementPolicy = replacementPolicy;
		this.writePolicy = writePolicy;
		this.missPolicy = missPolicy;
		
		try {
			outCache = new PrintWriter("cache.txt");
			outRAM = new PrintWriter("ram.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
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
	
	public void setMemory(RAM ram) {
		this.ram = ram;
	}
	
	public void cacheRead(String hex) {
		int address = Integer.parseInt(hex, 16);
		String binAddress = Integer.toBinaryString(address);
		while (binAddress.length() < Cache.ADDRESS_SIZE) {
			binAddress = "0" + binAddress;
		}
		int blockOffset = Integer.parseInt(binAddress.substring(this.blockOffsetStartingBit, 2));
		int setIndex = Integer.parseInt(binAddress.substring(this.setIndexStartingBit, this.blockOffsetStartingBit), 2);
		int tag = Integer.parseInt(binAddress.substring(0, this.setIndexStartingBit), 2);

		boolean hit = false;
		int requestedData = 0;
		// Searches through set for matching tag
		for(int i = 0; i < this.associativity; i++) {
			if (data.get(setIndex).get(i).getValid() == 1)
				if(data.get(setIndex).get(i).getTag() == tag) {
					hit = true;
					requestedData = data.get(setIndex).get(i).getBlock().get(blockOffset);
					break;
				}
		}

		// Cache miss
		if(!hit) {
			// generating address to retrieve data from memory (replace block offset bits with 0)
			String binBlockRetrievalAddress = binAddress.substring(0, this.blockOffsetStartingBit);
			while(binBlockRetrievalAddress.length() < Cache.ADDRESS_SIZE) {
				binBlockRetrievalAddress = binBlockRetrievalAddress + "0";
			}
			int blockRetrievalAddress = Integer.parseInt(binBlockRetrievalAddress, 2);
			
			// Cache miss handling for 1-way associative/direct-mapped
			if(this.associativity == 1) {
				for(int i = 0; i < this.dataBlockSize; i++) {
					data.get(setIndex).get(0).getBlock().set(i, Integer.parseInt(ram.getByte(blockRetrievalAddress + i), 16));
				}
				data.get(setIndex).get(0).setTag(tag);
				data.get(setIndex).get(0).setValid(1);
				requestedData = data.get(setIndex).get(0).getBlock().get(blockOffset);
			}
			// Cache miss handling for non-1-way associative
			else if(this.replacementPolicy == 1) { // Random Replacement
				int lineIndexReplacement = (int) (this.associativity * Math.random());
				for(int i = 0; i < this.dataBlockSize; i++) {
					data.get(setIndex).get(lineIndexReplacement).getBlock().set(i, Integer.parseInt(ram.getByte(blockRetrievalAddress + i), 16));
				}
				data.get(setIndex).get(lineIndexReplacement).setTag(tag);
				data.get(setIndex).get(lineIndexReplacement).setValid(1);
				requestedData = data.get(setIndex).get(lineIndexReplacement).getBlock().get(blockOffset);
			}
			else if(this.replacementPolicy == 2) { // LRU Replacement Policy
				
			}
		}
		
		System.out.println(""); //format output
	}
	
	public void cacheWrite() {
		
	}
	
	public void cacheFlush() {
		for (int i = 0; i < this.numberOfSets; i++) {
			for (int j = 0; j < this.associativity; j++) {
				data.get(i).get(j).setTag(0);
				data.get(i).get(j).setValid(0);
				for(int k = 0; k < this.dataBlockSize; k++) {
					data.get(i).get(j).getBlock().set(k, 0);
				}
			}
		}
	}
	
	public void cacheView () {
		
	}
	
	public void memoryView () {
		
	}
	
	
	public void cacheDump () {	//writes cache contents to cache.txt file
		String output = "";
		for(int r=0; r<data.size(); r++) {
			for(int c=0; c<(data.get(r)).size(); c++) {
				output+= data.get(r).get(c) + "\n";
			}
		}
		outCache.write(output);
	}
	
	public void memoryDump () { //writes ram contents to ram.txt
		String output = ram.toString();
		outRAM.write(output);
	}
	
	public void closeOutFiles() {
		outRAM.close();
		outCache.close();
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
	private int dirtyBit;
	private ArrayList<Integer> block;

	public Line (int blockSize) {
		valid = 0;
		tag = 0;
		dirtyBit = 0;
		this.block = new ArrayList<Integer>(blockSize);
	}

	public ArrayList<Integer> getBlock() {
		return block;
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


