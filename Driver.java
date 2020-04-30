// File: Driver
// Author(s): Cade Conner and William Henry
// Date: 04/29/2020
// Section: CC: 510, WH: 508
// E-mail: cadejconner@tamu.edu 
// Description: Driver connects all class together and contains the interaction menu.
// e.g. The content of this file implements...

public class Driver {

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
		int command = 0;
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
				if(util.checkInt(temp)) {
					if(Integer.valueOf(temp)<=8 && Integer.valueOf(temp)>=1) {
						command = Integer.valueOf(temp);
						goodVal = true;
					}
					else {
						System.out.println("Error: command must be between 1 and 8. Please try again.");
					}
				}
				else{
					System.out.println("Error: command must be an integer. Please try again.");
				}
			}
			
			switch(command){
				case 1:
					System.out.print("Enter hex address (30 decimal == 1e hex): ");
					String hexAddress = util.getInput();
					cache.cacheRead(hexAddress);
					break;
				case 2:
					cache.cacheWrite();
					break;
				case 3:
					cache.cacheFlush();
					break;
				case 4:
					cache.cacheView();
					break;
				case 5:
					cache.memoryView();
					break;
				case 6:
					cache.cacheDump();
					break;
				case 7:
					cache.memoryDump();
					break;
				case 8:
					quit = true;
					break;
			}
			
		}
		util.closeKB();
		cache.closeOutFiles();
		System.out.println("Finished.");
	}

}
