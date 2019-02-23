import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Rover {

	private int speed;
	private int delay_time;
	
	public Rover(int speed, int delay_time) {
		this.speed = speed;
		this.delay_time = delay_time;
	}

	public void move_forwards() {
		Motor.B.forward();
		Motor.C.forward();
		
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
	}
	
	public void stop() {
		Motor.B.stop(true);
		Motor.C.stop(true);
		
		Delay.msDelay(delay_time);

	}
	
	public void turn_left() {	    
		
		Motor.B.forward();
		Motor.C.backward();
		
		Delay.msDelay(100);

	}

	public void turn_right() {
	    		
		Motor.B.backward();
		Motor.C.forward();
		
		Delay.msDelay(100);

	}

	public void turn_by_angle(int angle) {
		// TODO Auto-generated method stub
	}

}
