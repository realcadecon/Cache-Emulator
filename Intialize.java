import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Intialize {
	
	private Scanner fileIn;
	
	public Intialize(String filename) {
		try {
			fileIn = new Scanner(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> setRAM(String filename) {
		String currLine = "";
		ArrayList<String> ramMem = new ArrayList<String>();
		while(fileIn.hasNext()) {
			currLine = fileIn.next();
			
			ramMem.add(currLine);	//uses .add function in arraylist to add a new btye to ram
		}
		return ramMem;
	}
	
	public Cache configureCache() {
		Scanner kb = new Scanner(System.in);
		int cacheSize = 0;
		int dataBlockSize = 0;
		int associativity = 0;
		int replacementPolicy = 0;
		int writePolicy = 0;
		int missPolicy = 0;
		System.out.println("configure the cache\ncache size(8-256 bytes): ");
		cacheSize = Integer.valueOf(kb.next());
		
		System.out.println("data block size(in bytes): ");
		dataBlockSize = Integer.valueOf(kb.next());
		
		System.out.println("associativity(1|2|4): ");
		associativity = Integer.valueOf(kb.next());
		
		System.out.println("replacementPolicy: ");
		replacementPolicy = Integer.valueOf(kb.next());
		
		System.out.println("write-hit policy: ");
		writePolicy = Integer.valueOf(kb.next());
		
		System.out.println("write-miss policy: ");
		missPolicy = Integer.valueOf(kb.next());	
		
		kb.close();
		
		Cache cache = new Cache(cacheSize, dataBlockSize, associativity, replacementPolicy, 
				writePolicy, missPolicy);
		
		return cache;
	}
	
	
	
}
