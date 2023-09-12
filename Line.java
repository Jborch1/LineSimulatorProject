import java.util.LinkedList;
import java.util.Queue;
/**
 * Represents the line that allows customers to wait until they are 
 * called for the next opening. The line keeps track of statistics about 
 * how many customers are waiting, as well as the average and max wait plus
 * the longest the line got.
 * 
 * @author Jacob Borchert
 * @version 2/1/2023
 *
 */
public class Line {

    private final Clock clock;
    private final int lineNumber;
    private int counterNumber;
    private final Queue<Customer> line = new LinkedList<>();
    private int totalWaitTime = 0;
    private int maxWaitTime = 0;
    private int maxLength = 0;
    private int numCustomersServed = 0;
    private int customersFinished;
    /**
	 * Creates an empty line for customers to wait in
	 * @param c the clock for the simulation
	 * @param num the line number
	 */
    public Line(Clock c, int num) {
        clock = c;
        lineNumber = num;
        customersFinished=0;
    }

    
    /**
	 * Adds a new customer to the end of the line
	 * @param cust the customer to add to the line
	 */
    public void addToLine(Customer cust) {
        line.add(cust);
        int waitTime = clock.getTime() - cust.getArrivalTime();
        if (waitTime > maxWaitTime) {
            maxWaitTime = waitTime;
        }
    }
    /**
	 * Determines if the line has at least
	 * one customer waiting
	 * @return true if there is at least one customer waiting, false otherwise
	 */
    public boolean hasNextCustomer() {
        return !line.isEmpty();
    }
    /**
	 * Gets the current length of the line
	 * @return the current length of the line
	 */
    public int getLineLength() {
        return line.size();
    }
    /**
	 * Gets the total amount of time waited by all customers that
	 * have exited the line.
	 * @return the total amount of time waited by everyone who has left the line
	 */
    public int getTotalWait() {
        return totalWaitTime;
    }
    /**
	 * Removes and returns the customer at the front of the line 
	 * @return the customer at the front of the line
	 */
    public Customer getNextCustomer() {
        if (line.isEmpty()) {
            return null;
        }
        Customer cust = line.remove();
        int waitTime = clock.getTime() - cust.getArrivalTime();
        totalWaitTime += waitTime;
        numCustomersServed++; // increment counter
        return cust;
    }
    /**
	 * At the end of the simulation, removes all the customers that are still
	 * in line (without counting them as completed customers). And updates
	 * the max wait if any of them waited longer than the current max
	 * @return the number of customers that were removed from the line
	 */
    public int cleanUp() {
        int numRemoved = line.size();
        for (Customer cust : line) {
            int waitTime = clock.getTime() - cust.getArrivalTime();
            if (waitTime > maxWaitTime) {
                maxWaitTime = waitTime;
            }
        }
        line.clear();
        maxLength = 0;
        return numRemoved;
    }
    /**
	 * Cleans up at the end of the simulation and prints the stats about the line. 
	 * This includes the line number, how many customers were in line when the 
	 * simulation ended, the max length of the line the average wait for customers 
	 * who left the line before the simulation ended, and the max wait for any customer 
	 * (whether they had left the line before the simulation ended or not).
	 */
    public void printLineStats() {
        System.out.println("Line " + lineNumber + " stats:");
        System.out.println("  customers in line at end of simulation: " + line.size());
        System.out.println("  max line length during simulation: " + maxLength);
        System.out.println("  average wait time for customers who left line before simulation end: " + (double) totalWaitTime / numCustomersServed); // use counter instead of computing difference
        System.out.println("  max wait time for any customer: " + maxWaitTime);
    }
    
    public int getMaxLength() {
        int max = 0;
        for (Customer c : line) {
            int length = c.getPositionInLine();
            if (length > max) {
                max = length;
            }
        }
        maxLength = max;
        return maxLength;
    }
    
   
    
    public int getCounterNumber() {
        return counterNumber;
    }
    
    
    
    
    public double getAverageWaitTime() {
        if (numCustomersServed == 0) { // handle division by zero
            return 0.0;
        } else {
            return (double) totalWaitTime / numCustomersServed;
        }
    }
    
    public int getMaxWaitTime() {
        return maxWaitTime;
    }
    
    public int getCustomersFinished() {
        return customersFinished;
    }
    
    
    

}
