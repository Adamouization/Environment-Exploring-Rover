import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class wall_follower {
	
	private static EV3IRSensor ir1 = new EV3IRSensor(SensorPort.S4);

	
	public static void main(String[] args) {
		final SampleProvider sp = ir1.getDistanceMode();

		double distance = 0.0;
		double new_distance = 0.0;
		double tolerance = 5.0;
		int speed = 300;
		int turn_amount = 4;
		int accumulator = 0;
		int av = 10;
		int number_of_repeats = 20;
		
		Motor.C.forward();
		Motor.B.forward();
		
		//Main Loop
		while(true) {
			accumulator = 0;
			
			float [] sample = new float[sp.sampleSize()];
			for(int i = 0; i < number_of_repeats; i++) {
				sp.fetchSample(sample, 0);
				
				//Stop integer limit being hit
				if((int)sample[0] > 100) {
					accumulator += 100;
				} else {
					accumulator += (int)sample[0];
				}
			}
			new_distance = accumulator / number_of_repeats;
			
			
			System.out.println(new_distance);
						
			//if distance greater than 50 +/- tolerance move towards wall
			//if less than move away from
		
			//Too far, move towards wall
			if(new_distance >= (50 + tolerance)) {
				Motor.B.setSpeed(speed + (speed/turn_amount));
				Motor.C.setSpeed(speed - (speed/turn_amount));
			}
			//Too close, move away from wall
			else if(new_distance <= (50 - tolerance)) {
				Motor.B.setSpeed(speed - (speed/turn_amount));
				Motor.C.setSpeed(speed + (speed/turn_amount));
			} 
			//Just right, keep 
			else { 
				Motor.B.setSpeed(speed);
				Motor.C.setSpeed(speed);	
			}
			
			
			
			
			
			/*
			if(java.lang.Math.abs(new_distance - distance) <= tolerance) {
				System.out.println("f");
				// Motor.B.setSpeed(speed);
				// Motor.B.setSpeed(speed);
				//Change in distance to the wall is within the tolerance zone
			} 
			
			//Turn 
			else {
				if(new_distance <= distance) {
					System.out.println("R");

				//	Motor.B.setSpeed(speed + 10);
				//	Motor.C.setSpeed(speed - 10);

				} else {
					System.out.println("L");

				//	Motor.B.setSpeed(speed - 10);
			//		Motor.C.setSpeed(speed + 10);
				}
			}*/
		}
	}
	
}
