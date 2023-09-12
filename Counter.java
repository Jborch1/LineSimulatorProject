import java.util.Queue;
import java.util.LinkedList;

public class Counter {
    private static final int PAYMENT_TIME = 40;
    private static final int ITEM_TIME = 7;
    private int number;
    private Queue<Customer> queue;
    private Customer currentCustomer;
    private Clock clock;
    private int idleTime;
    private int customersFinished;
    private int timeCheckingOut;
    private int counterNumber;
    private int totalWaitTime;
    private int customerAssignedTime;
    private Line line;
    
    
    /**
	 * Creates a new counter that initially does not have a customer
	 * @param num the counter number
	 * @param l the line that feeds the counter
	 * @param c the clock for the simulation
	 */
    
    public Counter(int num, Line l, Clock c) {
    	number = num;
        queue = new LinkedList<>();
        currentCustomer = null;
        clock = c;
        idleTime = 0;
        customersFinished = 0;
        timeCheckingOut = 0;
        totalWaitTime=0;
        customerAssignedTime=0;
        line = l; // assign the Line object to the instance variable
     
    }

    
    public void addCustomer(Customer customer) {
        line.addToLine(customer);
        totalWaitTime += clock.getTime() - customer.getArrivalTime();
    }
    
    
    /**
	 * If there are customers in line, it moves the
	 * next customer in line to the counter.
	 */
    

    /**
	 * Determines the time the customer will be finished checking out. The time
	 * a customer is finished checking out is determined by adding the product 
	 * of the number of items times the item time to the sum of the product time
	 * to the time that the customer left the line.
	 * @return the time the customer will be finished checking out or -1 if
	 * there is not current a customer at the counter
	 */
    private int getCustomerFinishTime() {
        if (currentCustomer != null) {
            int items = currentCustomer.getNumItems();
            int checkoutTime = items * ITEM_TIME;
            int finishTime = currentCustomer.getEnterTime() + checkoutTime + PAYMENT_TIME;
            return finishTime;
        } else {
            return -1;
        }
    }

    /**
	 * If there is a customer, it determines if they are done checking out and if
	 * so, it removes the customer from the counter.
	 */
    private void updateCurrentCustomer() {
        if (currentCustomer != null) {
            int finishTime = getCustomerFinishTime();
            if (finishTime == clock.getTime()) {
                timeCheckingOut += finishTime - currentCustomer.getEnterTime();
                currentCustomer.getExitTime();
                currentCustomer = null;
                customersFinished++;
            }
        }
    }

    /**
	 * Process the current second of the simulation for the counter. It first checks
	 * to see if any customers are done checking out and removes them if they are. 
	 * Then it tries to fill an empty counter with the next available customer if there
	 * are any. If the counter is empty at this point, it is idle.
	 * 
	 */
    public void updateCounter() {
        if (currentCustomer == null) {
            // if there is no current customer, assign the next customer from the line
            currentCustomer = line.getNextCustomer();
            if (currentCustomer != null) {
                customerAssignedTime = clock.getTime();
                totalWaitTime += clock.getTime() - currentCustomer.getArrivalTime();
            }
        } else {
            // if there is a current customer, check if they are finished checking out
            updateCurrentCustomer();
        }
    }
    /**
	 * Returns whether or not there is a customer at the counter
	 * @return true if there is a customer at the counter, false otherwise
	 */
    public boolean hasCustomer() {
        return currentCustomer != null;
    }
    /**
	 * Returns the number of customers that have finished checking out
	 * @return the number of customers that have finished checking out
	 */
    public int getCustomersFinished() {
        return customersFinished;
    }
    /**
	 * Finishes sales for any customers that finished as the simulation ended
	 */
    public void cleanUp() {
        while (currentCustomer != null) {
            updateCurrentCustomer();
        }
    }
    /**
	 * Gets the amount of time spent checking out by the customers who have completing checking out
	 * @return the time spent checking out by the customers who have completing checking out
	 */
    public int getTimeCheckingOut() {
        if (currentCustomer == null) {
            return 0;
        }
        return clock.getTime() - currentCustomer.getArrivalTime();
    }
    /**
	 * Prints the stats of the counter after cleaning up for the end of the simulation. 
	 * This includes the counter number, the number of customers who have finished checking
	 * out, whether or not there is a customer currently at the counter, and the percent of 
	 * time the counter was idle and the number of seconds it was idle 
	 */
    public String getStats() {
        double idlePercent = (double)idleTime / clock.getTime() * 100;
        String stats = String.format("Counter %d: Customers Finished = %d, Has Customer = %b, Idle Time = %d (%.2f%%)\n",
            number, customersFinished, hasCustomer(), idleTime, idlePercent);
        return stats;
    }
    
    
    
    
    public int getCounterNumber() {
        return counterNumber;
    }
    
    public int getIdleTime(){
    	return idleTime;
    }

    public int getTotalWait() {
        return totalWaitTime;
    }
    
    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }
    
    public boolean isFinished() {
        return this.currentCustomer == null;
    }

    public Customer releaseCounter() {
        Customer finishedCustomer = this.currentCustomer;
        this.currentCustomer = null;
        return finishedCustomer;
    }
    public void assignCustomer(Customer customer) {
        this.currentCustomer = customer;
        //this.customerAssignedTime = clock.getTime();
    }
    
    
	
    
    
}