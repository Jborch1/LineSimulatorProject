/**
 * This class represents a simple clock to keep track of the time
 * since the start of the simulation
 * 
 * @author Jacob Borchert 
 * @version 2/1/2023
 *
 */
public class Clock {
	private int time;
	
	/**
	 * Creates a clock with the time starting at 0
	 */
	public Clock() {
		this.time = 0;
	}
	
	/**
	 * Gets the amount of time since the start of the simulation
	 * @return the amount of time since the start of the simulation
	 */
	public int getTime() {
		return this.time;
	}
	
	/**
	 * Increments the time in the simulation
	 */
	public void incrementTime() {
		this.time++;
	}
}
