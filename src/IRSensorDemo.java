import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class IRSensorDemo {

	private static EV3IRSensor ir1 = new EV3IRSensor(SensorPort.S4);

	private static int HALF_SECOND = 25;

	public static void main(String[] args) {

		final SampleProvider sp = ir1.getDistanceMode();
		int distanceValue = 0;
		int accumulator = 0;
		int av = 10;
		
		//Control loop
		final int iteration_threshold = 500;
		for(int i = 0; i <= iteration_threshold; i++) {
			
			accumulator = 0;
			
			for(int j = 0; j < av; j++) {
				float [] sample = new float[sp.sampleSize()];
			    sp.fetchSample(sample, 0);
			    distanceValue = (int)sample[0];
			    accumulator += distanceValue;
			}
			distanceValue = accumulator/av;

			System.out.println("Iteration: {}" + i);
			System.out.println("Distance: {}" + distanceValue);

			Motor.B.forward();
			Motor.C.forward();
			if(distanceValue != -1) {
				if(distanceValue <= 40) {
					Motor.B.stop(true);
					Motor.C.stop(true);
					
				    Delay.msDelay(500);
				}
			}
		    Delay.msDelay(HALF_SECOND);
		}
		
	}
}