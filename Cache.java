// File: Cache
// Author(s): Cade Conner and William Henry
// Date: 04/29/2020
// Section: CC: 510, WH: 508
// E-mail: cadejconner@tamu.edu 
// Description: This file contains all of Cache's data members and functions.
// e.g. The content of this file implements ...

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Math;

public class Cache {
	private static final int ADDRESS_SIZE = 8;
	private int cacheSize, dataBlockSize, associativity, replacementPolicy, writePolicy, missPolicy, numHits, numMisses;
	private int numberOfSets, blockOffsetStartingBit, setIndexStartingBit;
	private ArrayList<ArrayList<Line>> data;
	private RAM ram;
	private ArrayList<Integer> lru; // Holds the U-bits for LRU-replacement policy
	PrintWriter outCache, outRAM;
	

	public Cache(int cacheSize, int dataBlockSize, int associativity, int replacementPolicy, 
			int writePolicy, int missPolicy) {
		
		this.cacheSize = cacheSize;
		this.dataBlockSize = dataBlockSize;
		this.associativity = associativity;
		this.replacementPolicy = replacementPolicy;
		this.writePolicy = writePolicy;
		this.missPolicy = missPolicy;
		
		numHits = 0;
		numMisses = 0;
		
		try {
			outCache = new PrintWriter("cache.txt");
			outRAM = new PrintWriter("ram.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		numberOfSets = this.cacheSize / (this.associativity * this.dataBlockSize); //calculating number of sets
		System.out.println("numSets: " + numberOfSets);
		this.data = new ArrayList<ArrayList<Line>>(0); //creates 

		// initializes the cache
		for (int i = 0; i < numberOfSets; i++) {	//loops through number of sets
			ArrayList<Line> insert = new ArrayList<Line>(0);
			for(int j = 0; j < this.associativity; j++)		//loops through number of lines per set
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
			lru = new ArrayList<Integer>(0);
			for(int i = 0; i < this.numberOfSets; i++) {
				lru.add(0);
			}
		}

		this.blockOffsetStartingBit = Cache.ADDRESS_SIZE - (int) (Math.log(this.dataBlockSize)/Math.log(2));
		this.setIndexStartingBit = this.blockOffsetStartingBit - (int) (Math.log(this.numberOfSets)/Math.log(2));
	}
	
	private String getWritePolicy(){
		if(writePolicy == 1) {
			return "write_through";
		}
		return "write_back";
	}
	
	private String getMissPolicy() {
		if(missPolicy == 1) {
			return "write_allocate";
		}
		return "no_write_allocate";
	}
	
	public void setMemory(RAM ram) {
		this.ram = ram;
	}
	
	
	public void cacheRead(String hex) {
		int address = Integer.parseInt(hex.substring(2), 16);
		String binAddress = Integer.toBinaryString(address);	//converts address to binary from hex
		while (binAddress.length() < Cache.ADDRESS_SIZE) {		//formats address in order to add tag bits, block offset bits and set index
			binAddress = "0" + binAddress;	
		}
		System.out.println(binAddress);
		System.out.println(blockOffsetStartingBit);
		System.out.println(setIndexStartingBit);
		int blockOffset;
		if(blockOffsetStartingBit==8) {
			blockOffset = 0; //sets block offset bits
		}
		else{
			blockOffset = Integer.parseInt(binAddress.substring(this.blockOffsetStartingBit), 2); //sets block offset bits
		}
		int setIndex = Integer.parseInt(binAddress.substring(this.setIndexStartingBit, this.blockOffsetStartingBit), 2); //sets setIndex
		int tag = Integer.parseInt(binAddress.substring(0, this.setIndexStartingBit), 2); //sets tagBits

		boolean hit = false;
		int requestedData = 0;
		// Searches through set for matching tag
		for(int i = 0; i < this.associativity; i++) {
			if (data.get(setIndex).get(i).getValid() == 1)	//checks valid bit for given line in a set
				if(data.get(setIndex).get(i).getTag() == tag) {	//checks if tag bits match
					hit = true;
					requestedData = data.get(setIndex).get(i).getBlock().get(blockOffset);
					break;
				}
		}

		// Cache miss
		int lineIndexReplacement = 0;
		if(!hit) {
			// generating address to retrieve data from memory (replace block offset bits with 0)
			String binBlockRetrievalAddress = binAddress.substring(0, this.blockOffsetStartingBit);
			while(binBlockRetrievalAddress.length() < Cache.ADDRESS_SIZE) {
				binBlockRetrievalAddress = binBlockRetrievalAddress + "0";
			}
			int blockRetrievalAddress = Integer.parseInt(binBlockRetrievalAddress, 2);
			
			// Cache miss handling for 1-way associative/direct-mapped
			if(this.associativity == 1) {
				if (data.get(setIndex).get(0).getDirtyBit() == 1) {
					int evictionTag = data.get(setIndex).get(0).getTag();
					String binEvictionTag = Integer.toBinaryString(evictionTag);
					while(binEvictionTag.length() < this.setIndexStartingBit) {
						binEvictionTag = "0" + binEvictionTag;
					}
					String binEvictionAddress = binEvictionTag + binAddress.substring(this.setIndexStartingBit, this.blockOffsetStartingBit);
					while(binEvictionAddress.length() < Cache.ADDRESS_SIZE) {
						binEvictionAddress = binEvictionAddress + "0";
					}
					int evictionAddress = Integer.parseInt(binEvictionAddress, 2);
					for(int i = 0; i < this.dataBlockSize; i++) {
						ram.setByte(evictionAddress + i, Integer.toHexString(data.get(setIndex).get(0).getBlock().get(i)));
					}
					data.get(setIndex).get(0).setDirtyBit(0);
				}
				for(int i = 0; i < this.dataBlockSize; i++) {
					data.get(setIndex).get(0).getBlock().set(i, Integer.parseInt(ram.getByte(blockRetrievalAddress + i), 16));
				}
				data.get(setIndex).get(0).setTag(tag);
				data.get(setIndex).get(0).setValid(1);
				requestedData = data.get(setIndex).get(0).getBlock().get(blockOffset);
			}
			// Cache miss handling for non-1-way associative
			else if(this.replacementPolicy == 1) { // Random Replacement
				lineIndexReplacement = -1;
				for(int i = 0; i < this.associativity; i++) {
					if(data.get(setIndex).get(i).getValid() == 0) {
						lineIndexReplacement = i;
						break;
					}
				}
				if(lineIndexReplacement == -1) {
					lineIndexReplacement = (int) (this.associativity * Math.random());
					if (data.get(setIndex).get(lineIndexReplacement).getDirtyBit() == 1) {
						int evictionTag = data.get(setIndex).get(lineIndexReplacement).getTag();
						String binEvictionTag = Integer.toBinaryString(evictionTag);
						while(binEvictionTag.length() < this.setIndexStartingBit) {
							binEvictionTag = "0" + binEvictionTag;
						}
						String binEvictionAddress = binEvictionTag + binAddress.substring(this.setIndexStartingBit, this.blockOffsetStartingBit);
						while(binEvictionAddress.length() < Cache.ADDRESS_SIZE) {
							binEvictionAddress = binEvictionAddress + "0";
						}
						int evictionAddress = Integer.parseInt(binEvictionAddress, 2);
						for(int i = 0; i < this.dataBlockSize; i++) {
							ram.setByte(evictionAddress + i, Integer.toHexString(data.get(setIndex).get(lineIndexReplacement).getBlock().get(i)));
						}
						data.get(setIndex).get(lineIndexReplacement).setDirtyBit(0);
					}
				}
				for(int i = 0; i < this.dataBlockSize; i++) {
					data.get(setIndex).get(lineIndexReplacement).getBlock().set(i, Integer.parseInt(ram.getByte(blockRetrievalAddress + i), 16));
				}
				data.get(setIndex).get(lineIndexReplacement).setTag(tag);
				data.get(setIndex).get(lineIndexReplacement).setValid(1);
				requestedData = data.get(setIndex).get(lineIndexReplacement).getBlock().get(blockOffset);
			}
			else if(this.replacementPolicy == 2) { // LRU Replacement Policy
				lineIndexReplacement = -1;
				for(int i = 0; i < this.associativity; i++) {
					if(data.get(setIndex).get(i).getValid() == 0) {
						lineIndexReplacement = i;
						break;
					}
				}
				if (lineIndexReplacement == -1) {
					if(this.associativity == 2) { // 2-way Associative
						if(lru.get(setIndex) == 0) {
							lineIndexReplacement = 0;
							lru.set(setIndex, 1);
						}
						else {
							lineIndexReplacement = 1;
							lru.set(setIndex, 0);
						}
					}
					else { // 4-way associative
						String order = Integer.toString(lru.get(setIndex), 4);
						lineIndexReplacement = ((int) (order.charAt(0))) - 48;
						order = order.substring(1, 4) + order.substring(0, 1);
						lru.set(setIndex, Integer.parseInt(order, 4));
					}
					if (data.get(setIndex).get(lineIndexReplacement).getDirtyBit() == 1) {
						int evictionTag = data.get(setIndex).get(lineIndexReplacement).getTag();
						String binEvictionTag = Integer.toBinaryString(evictionTag);
						while(binEvictionTag.length() < this.setIndexStartingBit) {
							binEvictionTag = "0" + binEvictionTag;
						}
						String binEvictionAddress = binEvictionTag + binAddress.substring(this.setIndexStartingBit, this.blockOffsetStartingBit);
						while(binEvictionAddress.length() < Cache.ADDRESS_SIZE) {
							binEvictionAddress = binEvictionAddress + "0";
						}
						int evictionAddress = Integer.parseInt(binEvictionAddress, 2);
						for(int i = 0; i < this.dataBlockSize; i++) {
							ram.setByte(evictionAddress + i, Integer.toHexString(data.get(setIndex).get(lineIndexReplacement).getBlock().get(i)));
						}
						data.get(setIndex).get(lineIndexReplacement).setDirtyBit(0);
					}
				}
				for(int i = 0; i < this.dataBlockSize; i++) {
					data.get(setIndex).get(lineIndexReplacement).getBlock().set(i, Integer.parseInt(ram.getByte(blockRetrievalAddress + i), 16));
				}
				data.get(setIndex).get(lineIndexReplacement).setTag(tag);
				data.get(setIndex).get(lineIndexReplacement).setValid(1);
				requestedData = data.get(setIndex).get(lineIndexReplacement).getBlock().get(blockOffset);
			}
		}
		System.out.println("set: " + setIndex + "\ntag: " + tag + "\nhit: " + (hit ? "yes":"no") + "\neviction_line: " + lineIndexReplacement + "\nram_address: " + hex + "\ndata: " + requestedData); //format output
	}
	
	public void cacheWrite(String hexAddress, String hexValue) {
		int address = Integer.parseInt(hexAddress.substring(2), 16);
		String binAddress = Integer.toBinaryString(address);	//converts address to binary from hex
		while (binAddress.length() < Cache.ADDRESS_SIZE) {		//formats address in order to add tag bits, block offset bits and set index
			binAddress = "0" + binAddress;	
		}
		int blockOffset = Integer.parseInt(binAddress.substring(this.blockOffsetStartingBit, 2)); //sets block offset bits
		int setIndex = Integer.parseInt(binAddress.substring(this.setIndexStartingBit, this.blockOffsetStartingBit), 2); //sets setIndex
		int tag = Integer.parseInt(binAddress.substring(0, this.setIndexStartingBit), 2); //sets tagBits

		boolean hit = false;
		int requestedLine = 0;
		for (int i = 0; i < this.associativity; i++) {
			if(data.get(setIndex).get(i).getTag() == tag) {
				hit = true;
				requestedLine = i;
				data.get(setIndex).get(i).getBlock().set(blockOffset, Integer.parseInt(hexValue.substring(2), 16));
				if(this.writePolicy == 1) {
					ram.setByte(address, hexValue);
				}
			}
		}


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
		numHits = 0;
		numMisses = 0;
	}
	
	public void cacheView () {	//prints out cache to console
		String output = "";
		output+="cache_size:"+ cacheSize+"\n";
		output+="data_block_size:"+dataBlockSize+"\n";
		output+="associativity:"+associativity+"\n";
		output+="replacement_policy:"+replacementPolicy+"\n";
		output+="write_hit_policy:"+getWritePolicy()+"\n";
		output+="write_miss_policy:"+getMissPolicy()+"\n";
		output+="number_of_cache_hits:"+numHits+"\n";
		output+="number_of_cache_misses:"+numMisses+"\n";
		output+="cache_content:\n";
		for(int set=0; set<data.size(); set++) {
			for(int line=0; line<data.get(set).size(); line++) {
				//<valid bit 0/1> <dirty bit 0/1> <tag in 2 digit hexadecimal> <hex data inside block>
				output += data.get(set).get(line).getValid() + " " 
						+ data.get(set).get(line).getDirtyBit() + " "
						+ data.get(set).get(line).getTagHex() + " "
						+ data.get(set).get(line).displayBlock() + "\n";
			}
		}
		System.out.println(output);
	}
	
	public void memoryView () {	//prints out RAM to console
		String output = "";
		int j = 0;
		output+="memory_size:256\n";
		output+="memory_content:\n";
		output+="Address:Data\n";
		output+="0x0"+Integer.toHexString(0)+":";
		for(int i=1; i<=256; i++) {
			if(j>=8) {
				if(i<10) {
					output+="\n0x0" + Integer.toHexString(i-1)+":";
				}
				else {
					output+="\n0x" + Integer.toHexString(i-1)+":";
				}
				j=0;
			}
			//0x<address in hex format>:<8 bytes of data starting from provided address separated by single space, format:hex>
			output += ram.getByte(i-1)+ " ";
			j++;
		}
		System.out.println(output);
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
		this.block = new ArrayList<Integer>(0);
		for(int i = 0; i < blockSize; i++) {
			this.block.add(0);
		}
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
	
	public int getDirtyBit() {
		return dirtyBit;
	}

	public void setDirtyBit(int val) {
		this.dirtyBit = val;
	}
	
	public String getTagHex() {
		return Integer.toHexString(tag);
	}

	public void setValid(int v) {
		this.valid = v;
	}

	public void setTag(int t) {
		this.tag = t;
	}
	
	public String displayBlock() {
		String output = "";
		for(int i=0; i<block.size(); i++) {
			output+= Integer.toHexString(block.get(i)) + " ";
		}
		return output;
	}
}


