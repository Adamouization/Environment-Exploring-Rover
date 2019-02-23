import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class Sensors implements ISensors {
	
	private static EV3IRSensor infrared_sensor = new EV3IRSensor(SensorPort.S4);
	SampleProvider sp = infrared_sensor.getDistanceMode();
	float sum;
	float distance; 
	
	@Override
	public boolean IRSense(int threshold, int tolerance, int number_of_averages) {
		
		float[] sample = new float[sp.sampleSize()];
		sum = 0;
		
		for(int i = 0; i < number_of_averages; i++){
			sp.fetchSample(sample, 0);
			sum +=  (int)sample[0];
		}
		
		distance = sum / number_of_averages;
		if(distance <= threshold) {
			return true; //true if wall
		}
		return false; //false if no wall
	}

	@Override
	public boolean bumper_sensor() {
		// TODO Auto-generated method stub
		return false;
	}

}
