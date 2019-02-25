/**
 * Interface for the different sensor classes.
 * @authors Tom Slattery, Adam Jaamour
 */
public interface ISensors {

		public boolean ultrasoundSense(float threshold, int number_of_averages);
		
		public boolean bumperSensor();
		
		public float ultrasoundSenseDistanceToWall(float threshold, int numberOfReadings);

}
