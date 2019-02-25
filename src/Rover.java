import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Rover {

	private int speed;
	private int delay_time;
	
	public Rover(int speed, int delay_time) {
		this.speed = speed;
		this.delay_time = delay_time;
		Motor.A.setSpeed(100);
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
	}

	public void move_forwards() {
		Motor.B.forward();
		Motor.C.forward();
	}
	
	public void move_backward() {
		Motor.B.backward();
		Motor.C.backward();
	}
	
	
	public void stop() {
		Motor.B.stop(true);
		Motor.C.stop(true);
		
	}
	
	public void turn_to_angle(int angle) {	    
		Motor.B.rotate(angle*2, true);
		Motor.C.rotate(-angle*2, true);
		Delay.msDelay(1000);
	}
	
	public void turn_left_90() {	    
		Motor.B.rotate(-180, true);
		Motor.C.rotate(180);
		Delay.msDelay(1000);


	}

	public void turn_right_90() {
		Motor.B.rotate(180, true);
		Motor.C.rotate(-180);
		Delay.msDelay(1000);


	}
	

	public void turn_180() {
		turn_left_90();
		turn_left_90();
		Delay.msDelay(1000);

	}
	
	public void turn_eyes(int angle) {
		Motor.A.setSpeed(100);
		Motor.A.rotate(angle);
	}
	
	public void eyes_to_front() {
		Motor.A.rotateTo(0);
	}

	public void turn_eyes_to_angle(int angle) {
		Motor.A.rotateTo(angle);
	}

}
