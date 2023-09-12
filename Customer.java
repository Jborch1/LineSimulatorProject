/**
 * Represents a customer in a simulation of check out lines in a store. The
 * customer keeps information about the amount of time they spend waiting in 
 * line as well as how many items they are checking out.
 * 
 * @author Jacob Borchert
 * @version 2/1/2023
 */
public class Customer {
    private static int nextPositionInLine = 1;
    private int enterTime;
    private int exitTime;
    private int numItems;
    private Clock c;
    private int arrivalTime;
    private int checkoutStartTime;
    private int positionInLine;

    /**
     * Creates a customer who is starting their wait at the current
     * simulation time.
     * @param c the clock keeping track of time in the simulation
     * @param num the number of items the customer has to check out
     */
    public Customer(Clock c, int num) {
        this.exitTime = -1;
        this.numItems = num;
        this.c = c;
        this.startWait();
        this.positionInLine = nextPositionInLine;
        nextPositionInLine++;
    }
    
    /**
     * Records the time a customer started waiting
     */
    private void startWait() {
        this.enterTime = c.getTime();
    }
    
    /**
     * Records the time a customer stopped waiting
     */
    public void stopWait() {
        this.exitTime = c.getTime();
    }
    
    /**
     * Gets the total amount of time the customer waited in line
     * @return the total amount of time a customer waited or -1 if the customer has 
     * not finished their wait
     */
    public int getTotalWait() {
        if(this.exitTime >= 0 && this.exitTime>=this.enterTime) {
            return (this.exitTime - this.enterTime);
        }
        return -1;
    }
    
    /**
     * Gets the time the customer entered the line
     * @return the time the customer entered the line
     */
    public int getEnterTime() {
        return this.enterTime;
    }
    
    /**
     * Gets the time the customer left the line
     * @return the time the customer left the line
     */
    public int getExitTime() {
        return this.exitTime;
    }
    
    /**
     * Gets the number of items the customer has to check out
     * @return the number of items the customer has to check out
     */
    public int getNumItems() {
        return this.numItems;
    }
    
    /**
     * Gets the position of the customer in line
     * @return the position of the customer in line
     */
    public int getPositionInLine() {
        return this.positionInLine;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setCheckoutStartTime(int time) {
        this.checkoutStartTime = time;
    }
        
}