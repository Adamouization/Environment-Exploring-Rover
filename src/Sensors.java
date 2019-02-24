import java.util.Arrays;
import java.util.ArrayList;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class Sensors implements ISensors {
	
	// Initialise variables
	int readingValue; 
	int sumOfSquares;
	int[] initialValues;
	float mean;
	float standardDeviation;
	float deviationLowerBound;
	float deviationUpperBound;
	float sum;
	float distance;
	float[] deviations;
	float[] squares;
	ArrayList<Integer> finalValues;	
	// LeJOS variables
	private static EV3IRSensor infrared_sensor = new EV3IRSensor(SensorPort.S4);
	SampleProvider sp = infrared_sensor.getDistanceMode();
	public static final int infinityReading = 1000;
	
	@Override
	public boolean IRSense(int threshold, int tolerance, int numberOfReadings) {
		
		initialValues = new int[numberOfReadings];
		// Read sensor values and store them in a list
		for(int i = 0; i < numberOfReadings; i++){
			
			// read and store value read from the IR sensor
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			readingValue = (int)sample[0];
			
			// set to a lower value if the maximum 32-bit integer value is read from the sensor
			if (readingValue >= Integer.MAX_VALUE) {
				readingValue = infinityReading;
			}
			initialValues[i] = readingValue;
		}
		
		// DEBUG: uncomment to print values read from IR sensor
		System.out.println(Arrays.toString(initialValues));
		
		// Get the mean
		sumOfSquares = 0;
		for (int i = 0; i < initialValues.length; i++) { // Taking the average to numbers
			sumOfSquares += initialValues[i];
		}
		mean = (float)(sumOfSquares / initialValues.length);
		System.out.printf("Mean = %2.2f", mean);
		
		// Get the deviation from the mean for each number in the list
		deviations = new float[numberOfReadings];
		System.out.println("\n\nDeviation of mean for each number in list: ");
		for (int i = 0; i < deviations.length; i++) {
			deviations[i] = initialValues[i] - mean ;
			System.out.printf("%2.2f | ", deviations[i]);
		}

		// Get the square of each deviation in the list
		squares = new float[numberOfReadings];
		System.out.println("\n\nSquares of deviation: ");
		for (int i = 0; i < squares.length; i++) { // getting the squares of deviations
			squares[i] = deviations[i] * deviations[i];
			System.out.printf("%4.2f | ", squares[i]);
		}

		// Get the sum of all the squares previously calculated
		sumOfSquares = 0;
		for (int i = 0; i< squares.length; i++) { // adding all the squares
			sumOfSquares += squares[i];
		}
		System.out.println("\n\nSum of all squares = " + sumOfSquares);

		// Divide the numbers by one less than total numbers
		standardDeviation = sumOfSquares / (initialValues.length - 1);
		System.out.print("Divide addition of squares by total (numbers) - 1 = ");
		System.out.printf("%4.2f\n", standardDeviation);

		// Calculate the square root to the get final standard deviation of all values in the list
		standardDeviation = (float) Math.sqrt(standardDeviation);
		System.out.print("\n\nStandard Deviation of the numbers in the list = ");
		System.out.printf("%4.2f\n", standardDeviation);
		
		// Exclude values that are not within the standard deviation from the mean
		finalValues = new ArrayList<Integer>();
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
		System.out.println("Final values: " + finalValues);
		
		// Average the final list that excludes out of range readings
		sum = 0;
		if (!finalValues.isEmpty()) {
			for(int i = 0; i < numberOfReadings; i++){
				sum += finalValues.get(i);
			}
			System.out.println("number of readings: " + numberOfReadings);
			distance = sum / numberOfReadings;
		} else {
			distance = infinityReading;
		}
		System.out.println("distance = " + distance);
		
		// Return boolean to specify if a wall is nearby or not
		if(distance <= threshold) {
			return true; // there is wall
		}
		return false; // there is no wall
	}

	@Override
	public boolean bumper_sensor() {
		// TODO Auto-generated method stub
		return false;
	}

}
