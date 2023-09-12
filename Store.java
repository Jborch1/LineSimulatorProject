import java.util.ArrayList;

/**
 * This represents the simulation of customers entering the checkout
 * area of the store and moving into lines and from lines to checkout 
 * counters. It allows for two types of set up, a single line set up
 * or a line per checkout set up and it then provides methods to simulate
 * customers moving through the line and check out counters and to 
 * print stats about the store at the end.
 * 
 * author Jacob Borchert
 * version 2/1/2023
 */
public class Store {

    private ArrayList<Counter> counters;
    private Line line;
    private ArrayList<Line> lines;
    private boolean multi;
    private Clock c;


    /**
     * Creates the store according to the simulation settings. It creates the specified number
     * of counters. If it is a multiple simulation, it creates one line per counter. If it
     * is not a multiple simulation (e.g. it is a single simulation), there is a single line.
     * @param numCounters the number of check out counters that are open during the simulation
     * @param multi true if there is a line per counter, false for a single line
     * @param c the clock for the simulation
     */
    public Store(int numCounters, boolean multi, Clock c) {
        this.multi = multi;
        this.c = c;
        this.counters = new ArrayList<Counter>();
        this.lines = new ArrayList<Line>();
        

        // Initialize the lines based on the value of 'multi'
        if (multi) {
            this.lines = new ArrayList<Line>();
            for (int i = 0; i < numCounters; i++) {
                Line line = new Line(c, i);
                this.lines.add(line);
                Counter counter = new Counter(i, line, c);
                this.counters.add(counter);
            }
        } else {
            Line line = new Line(c, numCounters);
            this.lines = new ArrayList<Line>();
            this.lines.add(line);
            this.line = line; // assign the Line object to this.line variable
            for (int i = 0; i < numCounters; i++) {
                Counter counter = new Counter(i, line, c);
                this.counters.add(counter);
            }
        }
    }

    /**
     * Processes a customer arriving at the checkout time. A customer will be placed in the
     * shortest line (also accounting for whether there is a customer at the counter) with
     * ties broken by the smallest counter number
     * @param customer the customer that is ready to get in line
     */
    public void customerArrival(Customer customer) {
        if (multi) {
            Line shortestLine = lines.get(0);
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).getLineLength() < shortestLine.getLineLength()) {
                    shortestLine = lines.get(i);
                }
            }
            shortestLine.addToLine(customer);
        } else {
            line.addToLine(customer);
        }
    }

    /**
     * Processes one second of the simulation for the store by updating each
     * counter
     */
    public void updateStore() {
        for (Counter counter : counters) {
            if (!counter.hasCustomer()) {
                if (multi) {
                    for (Line l : lines) {
                        if (l.getLineLength() > 0 ) {
                            counter.updateCounter();
                            break;
                        }
                    }
                } else {
                    if (line != null && line.getLineLength() > 0) {
                        counter.updateCounter();
                    }
                }
            } else {
                counter.updateCounter();
            }
        }
    }
	/**
	 * Cleans up the end of the simulation and prints the stats for the store. This includes: the types 
	 * of simulation (single line vs multiple lines), the stats for each individual counter and line, and 
	 * then the overall stats. The overall stats include the number of customers checked out across all 
	 * counters, the average wait time for all customers that made it to the counter across all the lines, 
	 * and the average time at the check out counter for the customers who finished checking out.
	 */
    public void printStats() {
    	System.out.println("Simulation type: " + (multi ? "Multiple lines" : "Single line"));
    	System.out.println("--------------------------------------------------------");
    	
    	if (multi) {
    		for (int i = 0; i < counters.size(); i++) {
    			Line line = lines.get(i);
    			Counter counter = counters.get(i);
    			System.out.println("Counter " + counter.getCounterNumber() + " (Line " + (i + 1) + ")");
    			System.out.println("\tTotal customers checked out: " + counter.getCustomersFinished());
    			System.out.println("\tTotal time spent checking out: " + counter.getTimeCheckingOut());
    			System.out.println("\tTotal time spent waiting: " + line.getTotalWait());
    			System.out.println("\tAverage wait time per customer: " + line.getAverageWaitTime());
    			System.out.println("\tMax line length: " + line.getMaxLength());
    			System.out.println();
    		}
    	} else {
    		Counter counter = counters.get(0);
    		System.out.println("Single line for all counters");
    		System.out.println("\tTotal customers checked out: " + counter.getCustomersFinished());
    		System.out.println("\tTotal time spent checking out: " + counter.getTimeCheckingOut());
    		System.out.println("\tTotal time spent waiting: " + line.getTotalWait());
    		System.out.println("\tAverage wait time per customer: " + line.getAverageWaitTime());
    		System.out.println("\tMax line length: " + line.getMaxLength());
    		System.out.println();
    	}
    	
    	System.out.println("Overall stats:");
    	System.out.println("\tTotal customers checked out: " + getTotalCustomersCheckedOut());
    	//System.out.println("\tAverage wait time per customer: " + getAverageWaitTime());
    	System.out.println("\tAverage check out time per customer: " + getAverageCheckOutTime());
    	System.out.println("\tMax line length: " + getMaxLineLength());
    }
    
    public int getTotalCustomersCheckedOut() {
        int total = 0;
        for (Counter counter : counters) {
            total += counter.getCustomersFinished();
        }
        return total;
    }

    /**
     * Returns the average wait time per customer for all customers that made it to the counter
     * across all lines.
     */
    public double getAverageWaitTime() {
        int numCustomers = 0;
        double totalWaitTime = 0.0;
        for (Line line : lines) {
            numCustomers += counters.get(0).getCustomersFinished();
            totalWaitTime += line.getTotalWait();
        }
        return (numCustomers == 0) ? 0.0 : totalWaitTime / numCustomers;
    }

    /**
     * Returns the average time spent at the check out counter for the customers who finished
     * checking out.
     */
    public double getAverageCheckOutTime() {
        int numCustomers = 0;
        double totalTimeCheckingOut = 0.0;
        for (Counter counter : counters) {
            numCustomers += counter.getCustomersFinished();
            totalTimeCheckingOut += counter.getTimeCheckingOut();
        }
        return (numCustomers == 0) ? 0.0 : totalTimeCheckingOut / numCustomers;
    }

    /**
     * Returns the maximum line length across all lines
     * WHY WONT THIS WORK :(
     */
    public int getMaxLineLength() {
        int maxLength = 0;
        for (Line line : lines) {
            if (line.getMaxLength() > maxLength) {
                maxLength = line.getMaxLength();
            }
        }
        return maxLength;
    }
}