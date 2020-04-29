import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Initialize {
	
	private Scanner fileIn;
	Scanner kb;
	
	public Initialize(String filename) {
		try {
			fileIn = new Scanner(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kb = new Scanner(System.in);
	}
	
	public ArrayList<String> setRAM() {
		String currLine = "";
		ArrayList<String> ramMem = new ArrayList<String>();
		while(fileIn.hasNext()) {
			currLine = fileIn.next();
			
			ramMem.add(currLine);	//uses .add function in arraylist to add a new btye to ram
		}
		return ramMem;
	}
	
	public Cache configureCache() {
		int cacheSize = 0;
		int dataBlockSize = 0;
		int associativity = 0;
		int replacementPolicy = 0;
		int writePolicy = 0;
		int missPolicy = 0;
		boolean goodVal = false;
		String temp = "";
		
		//Cache Size
		System.out.print("configure the cache: ");
		goodVal = false;
		while(!goodVal) {
			System.out.print("cache size(8-256 bytes): ");
			temp = kb.next();
			if(checkInt(temp)){
				if(Integer.valueOf(temp)<=256 && Integer.valueOf(temp)>=8) {
					cacheSize = Integer.valueOf(temp);
					goodVal = true;
				}
				else if(!(Integer.valueOf(temp)<=256 && Integer.valueOf(temp)>=8)) {
						System.out.println("Error: cache size must be between 8 and 256 bytes. Please try again.");
				}
			}
			else{
				System.out.println("Error: cache size must be an integer. Please try again.");
			}
		}
		
		//Data block size
		goodVal = false;
		while(!goodVal) {
			System.out.print("data block size(in bytes): ");
			temp = kb.next();
			if(checkInt(temp)){
				if(Integer.valueOf(temp)<=cacheSize && Integer.valueOf(temp)>=1) {
					dataBlockSize = Integer.valueOf(temp);
					goodVal = true;
				}
				else if(!(Integer.valueOf(temp)<=cacheSize && Integer.valueOf(temp)>=1)) {
						System.out.println("Error: data block size must be positive and less than cache size. Please try again.");
				}
			}
			else {
				System.out.println("Error: data block size must be an integer. Please try again.");
			}
			
		}
		
		//Associativity
		goodVal = false;
		while(!goodVal) {
			System.out.print("associativity(1|2|4): ");
			temp = kb.next();
			if(checkInt(temp)){
				if(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2 || Integer.valueOf(temp)==4) {
					associativity = Integer.valueOf(temp);
					goodVal = true;
				}
				else if(!(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2 || Integer.valueOf(temp)==4)) {
						System.out.println("Error: Associativity must be either 1, 2, or 4-way. Please try again.");
				}
			}
			else {
				System.out.println("Error: Associativity must be an integer. Please try again.");
			}
		}
		
		//Replacement Policy
		goodVal = false;
		while(!goodVal) {
			System.out.print("replacementPolicy(RR = 1 | LRU = 2): ");
			temp = kb.next();
			if(checkInt(temp)){
				if(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2) {
					replacementPolicy = Integer.valueOf(temp);
					goodVal = true;
				}
				else if(!(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2)) {
						System.out.println("Error: replacementPolicy must be either 1 or 2. Please try again.");
				}
			}
			else {
				System.out.println("Error: replacementPolicy must be an integer. Please try again.");
			}
		}
		
		//Write-Hit Policy
		goodVal = false;
		while(!goodVal) {
			System.out.print("write-hit policy(write-through = 1 | write-back = 2): ");
			temp = kb.next();
				if(checkInt(temp)){
					if(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2) {
						writePolicy = Integer.valueOf(temp);
						goodVal = true;
					}
					else if(!(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2)) {
							System.out.println("Error: writePolicy must be either 1 or 2. Please try again.");
					}
				}
				else {
					System.out.println("Error: writePolicy must be an integer. Please try again.");
				}
		}
	
		//Write-miss policy
		goodVal = false;
		while(!goodVal) {
			System.out.print("write-miss policy(write-allocate = 1 | no write-allocate = 2): ");
			temp = kb.next();
			if(checkInt(temp)){
				if(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2) {
					missPolicy = Integer.valueOf(temp);
					goodVal = true;
				}
				else if(!(Integer.valueOf(temp)==1 || Integer.valueOf(temp)==2)) {
						System.out.println("Error: missPolicy must be either 1 or 2. Please try again.");
				}
			}
			else {
				System.out.println("Error: missPolicy must be an integer. Please try again.");
			}
		}
		
		//create and return configured cache
		Cache cache = new Cache(cacheSize, dataBlockSize, associativity, replacementPolicy, 
				writePolicy, missPolicy);
		return cache;
	}
	
	public boolean checkInt(String s) {
		try {
			Integer.valueOf(s);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	public String getInput() {
		return kb.next();
	}
	
	public void closeKB() {
		kb.close();
	}
	
	
	
}