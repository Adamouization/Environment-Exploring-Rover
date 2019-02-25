public interface ISensors {

		public boolean ultrasoundSense(float threshold, int number_of_averages); //
		public boolean bumper_sensor();
		public float ultrasoundSenseDistanceToWall(float threshold, int numberOfReadings);

}
