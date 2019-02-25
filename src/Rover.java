import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Rover {

	private int speed;
	private int delay_time;
	
	public Rover(int speed, int delay_time) {
		this.speed = speed;
		this.delay_time = delay_time;
		Motor.A.setSpeed(500);
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
	}

	public void move_forwards() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		Motor.B.forward();
		Motor.C.forward();
	}
	
	/**Curve slightly while moving
	 * 
	 * + = curve right
	 * - = curve left
	 * 
	 * @param speed_difference
	 */
	public void curve_while_moving(int speed_difference) {
		Motor.B.setSpeed(speed + speed_difference);
		Motor.C.setSpeed(speed - speed_difference);
		Motor.B.forward();
		Motor.C.forward();
	}
	
	public void move_backward() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
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
	
	//Motors overcompensate
	public void turn_left_90() {	
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		Motor.B.rotate(-200, true);
		Motor.C.rotate(200);
		Delay.msDelay(1000);


	}

	public void turn_right_90() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		Motor.B.rotate(200, true);
		Motor.C.rotate(-200);
		Delay.msDelay(1000);


	}
	
	public void turn_180() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		turn_left_90();
		turn_left_90();
		Delay.msDelay(1000);

	}
	
	/** turn_eyes
	 * 
	 * Turns eyes BY an angle
	 * @param angle
	 */
	public void turn_eyes_by_angle(int angle) {
		Motor.A.setSpeed(500);
		Motor.A.rotate(angle);
	}
	
	public void eyes_to_front() {
		Motor.A.rotateTo(0);
	}

	public void camera_direction(char direction) {

		if (direction == 'F') {
			eyes_to_front();
		}
		
		//If direction is L, turn towards the right
		else if (direction == 'L') {

			turn_eyes_to_angle(90);
		}
		//If direction is R, turn towards the left
		else if (direction == 'R') {
			turn_eyes_to_angle(-90);

		}
	}
	
	/** turn_eyes_to_angle
	 * 
	 * Turns eyes towards an angle
	 * @param angle
	 */
	public void turn_eyes_to_angle(int angle) {
		Motor.A.rotateTo(angle);
	}

}
