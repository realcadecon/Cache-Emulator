// File: Driver
// Author(s): Cade Conner and William Henry
// Date: 04/29/2020
// Section: CC: 510, WH: 508
// E-mail: cadejconner@tamu.edu 
// Description: Driver connects all class together and contains the interaction menu.
// e.g. The content of this file implements...

public class cachesimulator {

	public static void main(String[] args) {
		String fileLocation = args[0];
		Initialize util = new Initialize(fileLocation);
		//file directory example "C:\\Users\\cadea\\eclipse-workspace\\Cache Simulator\\src\\input.txt"
		
		//RAM set up and initialization
		RAM ram = new RAM();
		System.out.println("*** Welcome to the cache simulator ***\ninitialize the RAM:\ninit-ram 0x00 0xFF");
		ram.setMem(util.setRAM());
		System.out.println("ram successfully initialized\n");
		
		//Cache set up and initialization
		Cache cache = util.configureCache();
		cache.setMemory(ram);
		
		boolean quit = false;
		boolean goodVal = false;
		String command = "";
		String temp = "";
		while(!quit) {
			System.out.println("\n***Cache simulator menu***\ntype one command\n"
					+ "1. cache-read\n"
					+ "2. cache-write\n" + 
					"3. cache-flush\n" + 
					"4. cache-view\n" + 
					"5. memory-view\n" + 
					"6. cache-dump\n" + 
					"7. memory-dump\n" + 
					"8. quit\n" + 
					"****************************\n" + 
					"");
			temp = util.getInput();
			goodVal = false;
			while(!goodVal) {
				if(!temp.equals("cache-read") && !temp.equals("cache-write") && !temp.equals("cache-flush") && !temp.equals("cache-view") && !temp.equals("memory-view") 
				&& !temp.equals("cache-dump") && !temp.equals("memory-dump") && !temp.equals("quit")) {
					System.out.println("Error: No such command. Please check spelling and try again.");
					temp = util.getInput();
				}
				else {
					goodVal = true;
					command = temp;
				}
			}
			
			switch(command){
				case "cache-read":
					String hexAddress1 = util.getInput();
					cache.cacheRead(hexAddress1);
					break;
				case "cache-write":
					String hexAddress2 = util.getInput();
					String hexVal = util.getInput();
					cache.cacheWrite(hexAddress2, hexVal);
					break;
				case "cache-flush":
					cache.cacheFlush();
					break;
				case "cache-view":
					cache.cacheView();
					break;
				case "memory-view":
					cache.memoryView();
					break;
				case "cache-dump":
					cache.cacheDump();
					break;
				case "memory-dump":
					cache.memoryDump();
					break;
				case "quit":
					quit = true;
					break;
			}
			
		}
		util.closeKB();
		cache.closeOutFiles();
		System.out.println("Finished.");
	}

}
