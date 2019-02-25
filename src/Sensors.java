import java.util.ArrayList;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;


/**
 * Class containing methods for reading sensor readings.
 * @authors Tom Slattery, Adam Jaamour
 */
public class Sensors implements ISensors {
	
	// Initialise variables
	int leftValue; 
	int rightValue; 
	float readingValue; 
	float mean;
	float sumOfSquares;
	float standardDeviation;
	float deviationLowerBound;
	float deviationUpperBound;
	float sum;
	float distance;
	float[] initialValues;
	float[] deviations;
	float[] squares;
	ArrayList<Float> finalValues;	
	// LeJOS variables
	private static EV3UltrasonicSensor UltrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
	SampleProvider sp = UltrasonicSensor.getDistanceMode();
	private static EV3TouchSensor touchSensorL = new EV3TouchSensor(SensorPort.S2);
	private static EV3TouchSensor touchSensorR = new EV3TouchSensor(SensorPort.S3);
	public static final int infinityReading = 1000;
	
	@Override
	/**
	 * Returns the distance to the wall while using standard deviation to rule out odd readings and filter out noisy values.
	 */
	public float ultrasoundSenseDistanceToWall(float threshold, int numberOfReadings) {
		initialValues = new float[numberOfReadings];

		// Read sensor values and store them in a list
		for(int i = 0; i < numberOfReadings; i++){
			
			// read and store value read from the IR sensor
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			readingValue = sample[0];
 			
			// set to a lower value if the maximum 32-bit float value is read from the sensor
			if (readingValue >= Float.MAX_VALUE) {
				readingValue = infinityReading;
			}
			initialValues[i] = readingValue;
		}
		
		// DEBUG: uncomment to print values read from IR sensor
		//System.out.println(Arrays.toString(initialValues));

		// Get the mean
		sumOfSquares = 0;
		for (int i = 0; i < initialValues.length; i++) { // Taking the average to numbers
			sumOfSquares += initialValues[i];
		}
		mean = sumOfSquares / initialValues.length;
		
		// Get the deviation from the mean for each number in the list
		deviations = new float[numberOfReadings];
		for (int i = 0; i < deviations.length; i++) {
			deviations[i] = initialValues[i] - mean ;
		}

		// Get the square of each deviation in the list
		squares = new float[numberOfReadings];
		for (int i = 0; i < squares.length; i++) { // getting the squares of deviations
			squares[i] = deviations[i] * deviations[i];
		}

		// Get the sum of all the squares previously calculated
		sumOfSquares = 0;
		for (int i = 0; i< squares.length; i++) { // adding all the squares
			sumOfSquares += squares[i];
		}

		// Divide the numbers by one less than total numbers
		standardDeviation = sumOfSquares / (initialValues.length - 1);


		// Calculate the square root to the get final standard deviation of all values in the list
		standardDeviation = (float) Math.sqrt(standardDeviation);
		
		// Exclude values that are not within the standard deviation from the mean
		finalValues = new ArrayList<Float>();
		deviationLowerBound = mean - standardDeviation;
		deviationUpperBound = mean + standardDeviation;
		for (int i = 0; i < initialValues.length; i++) {
			if (initialValues[i] >= deviationLowerBound && initialValues[i] <= deviationUpperBound) {
				finalValues.add(initialValues[i]);
			} else {
				numberOfReadings--;
			}
		}
		// DEBUG: uncomment to print values read from IR sensor
		//System.out.println("Final values: " + finalValues);
		
		// Average the final list that excludes out of range readings
		sum = 0;
		if (!finalValues.isEmpty()) {
			for(int i = 0; i < numberOfReadings; i++){
				sum += finalValues.get(i);
			}
			distance = sum / numberOfReadings;
		} else {
			distance = infinityReading;
		}
		
		return distance;
	}
	

	@Override
	/** ultrasoundSense
	 * 
	 * Returns a boolean: true if there is a wall, false if not.
	 */
	public boolean ultrasoundSense(float threshold, int numberOfReadings) {
		
		distance = ultrasoundSenseDistanceToWall(threshold, numberOfReadings);

		// Return boolean to specify if a wall is nearby or not
		if(distance <= threshold) {
			return true; // there is a wall
		}
		return false; // there is no wall
	}

	@Override
	/** bumperSensor
	 * 
	 * Returns TRUE if either of the 2 touch sensors are pressed, ELSE returns false.
	 */
	public boolean bumperSensor() {
		// left touch sensor
		float[] leftSample = new float[sp.sampleSize()];
		touchSensorL.fetchSample(leftSample, 0);
		leftValue = (int)leftSample[0];
		
		// right touch sensor
		float[] rightSample = new float[sp.sampleSize()];
		touchSensorR.fetchSample(rightSample, 0);
		rightValue = (int)rightSample[0];
		
		// return true if either the left or right bumper sensors are activated, else false
		if((leftValue ==  1) || (rightValue == 1)) {
			return true;
		}
		return false;
	}
	
}
