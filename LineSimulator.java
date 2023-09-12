import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * DO NOT CHANGE
 * 
 * Simulates two types of lines at the store, a single line for all counters
 * and a store with a line per counter, using customer arrival 
 * data from a user provided file. It then prints relevant statistics about 
 * the lines and windows in the theater
 * 
 * @author Jacob Borchert
 * @version 2/1/2023
 *
 */
public class LineSimulator {

	public static void main(String[] args) {
		//Get the simulation information
		Scanner keyboard = new Scanner(System.in);
		System.out.println("What is the file with the simulation data?");
		String filename = keyboard.next();
		System.out.println("What is the simulation type (single or multiple)?");
		String type = keyboard.next();
		boolean multi;
		if(type.toLowerCase().equals("single")) {
			multi = false;
		}
		else {
			multi = true;
		}
		keyboard.close();

		Scanner infile;
		try {
			//Set up Simulation
			infile = new Scanner(new File(filename));
			String simName = infile.nextLine();
			int mins = infile.nextInt();
			int numWindows = infile.nextInt();
			Clock c = new Clock();
			Store sim = new Store(numWindows, multi, c);
			infile.nextLine();
			int time = -1;
			int numItems = -1;
			//get first customer
			if(infile.hasNextInt()) {
				time = infile.nextInt();
				numItems = infile.nextInt();
			}
			//run simulation
			while(c.getTime() < mins*60) {
				
				//check for new customers and add to line
				while(time == c.getTime()) {
					sim.customerArrival(new Customer(c, numItems));
					if(!infile.hasNextInt()) {
						time = -1;
					}
					else {
						time = infile.nextInt();
						numItems = infile.nextInt();
					}
				}

				//update the store
				sim.updateStore();

				//update the clock
				c.incrementTime();

			}

			System.out.println("\n\n");
			System.out.println();
			System.out.println();

			System.out.println("Stats for the simulation: "+simName);
			System.out.println();
			System.out.println();
			sim.printStats();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

	}

}
