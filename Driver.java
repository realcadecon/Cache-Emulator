
public class Driver {

	public static void main(String[] args) {
		Initialize util = new Initialize("C:\\Users\\cadea\\eclipse-workspace\\Cache Simulator\\src\\input.txt");
		
		//RAM set up and initialization
		RAM ram = new RAM();
		System.out.println("*** Welcome to the cache simulator ***\ninitialize the RAM:\ninit-ram 0x00 0xFF");
		ram.setMem(util.setRAM());
		System.out.println("ram successfully initialized\n");
		
		//Cache set up and initialization
		Cache cache = util.configureCache();
		
		System.out.println(ram);
		System.out.println(cache);
		System.out.println("Finished.");
	}

}
