/**
 * Interface for the different sensor classes.
 * @author tslattery
 *
 */
public interface ISensors {

		public boolean ultrasoundSense(float threshold, int number_of_averages);
		
		public boolean bumperSensor();
		
		public float ultrasoundSenseDistanceToWall(float threshold, int numberOfReadings);
}
