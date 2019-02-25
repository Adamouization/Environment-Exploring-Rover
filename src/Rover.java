import lejos.hardware.motor.Motor;
import lejos.utility.Delay;


/**
 * Class for Rover movement methods, including:
 * 		- wheel movement
 * 		- UltraSound sensor movement
 * @authors Tom Slattery, Adam Jaamour
 */
public class Rover {
	
	// global variable
	private int speed;
	
	/** Rover
	 * 
	 * Constructor for the Rover class.
	 * @param speed: the speed the motors move at
	 */
	public Rover(int speed) {
		this.speed = speed;
		Motor.A.setSpeed(500);
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
	}

	/** moveForward
	 * 
	 * Method for the robot to move forward at the specified speed.
	 */
	public void moveForward() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		Motor.B.forward();
		Motor.C.forward();
	}
	
	/** curveWhileMoving
	 * 
	 * Curve the robot's direction slightly while moving.
	 * (+ = curve right)
	 * (- = curve left)
	 * @param speed_difference: defines the curve's strength
	 */
	public void curveWhileMoving(int speed_difference) {
		Motor.B.setSpeed(speed + speed_difference);
		Motor.C.setSpeed(speed - speed_difference);
		Motor.B.forward();
		Motor.C.forward();
	}
	
	/** moveBackward
	 * 
	 * Make the robot move backwards.
	 */
	public void moveBackward() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		Motor.B.backward();
		Motor.C.backward();
	}
	
	/** stop
	 * 
	 * Causes both wheels to stop moving simultaneously.
	 */
	public void stop() {
		Motor.B.stop(true);
		Motor.C.stop(true);
	}
	
	/** turnToAngle
	 * Rotate the robot TO a specified angle.
	 * @param angle: the angle to rotate the robot to
	 */
	public void turnToAngle(int angle) {	    
		Motor.B.rotate(angle*2, true);
		Motor.C.rotate(-angle*2, true);
		Delay.msDelay(1000);
	}
	
	/** turnLeft90
	 *
	 * Rotate the eyes by 90 degrees to the left.
	 */
	public void turnLeft90() {	
		// motors overcompensate
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		Motor.B.rotate(-200, true);
		Motor.C.rotate(200);
		Delay.msDelay(1000);
	}

	/** turnRight90
	 * 
	 * Rotate the eyes by 90 degrees to the right.
	 */
	public void turnRight90() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		Motor.B.rotate(200, true);
		Motor.C.rotate(-200);
		Delay.msDelay(1000);
	}
	
	/** turn180
	 * 
	 * Rotate the eyes by 180 degrees.
	 */
	public void turn180() {
		Motor.B.setSpeed(speed);
		Motor.C.setSpeed(speed);
		turnLeft90();
		turnLeft90();
		Delay.msDelay(1000);

	}
	
	/** turnEyesByAngle
	 * 
	 * Turns eyes BY a specified angle.
	 * @param angle: the angle to move the eyes BY
	 */
	public void turnEyesByAngle(int angle) {
		Motor.A.setSpeed(500);
		Motor.A.rotate(angle);
	}
	
	/** turnEyesToAngle
	 * 
	 * Turns eyes towards an angle.
	 * @param angle: the angle to move the eyes TO
	 */
	public void turnEyesToAngle(int angle) {
		Motor.A.rotateTo(angle);
	}
	
	/** moveEyesToFront
	 * 
	 * Moves the robot's UltraSound Sensor to face its front direction.
	 */
	public void moveEyesToFront() {
		Motor.A.rotateTo(0);
	}

	/** cameraDirection
	 * 
	 * Moves the camera to a specified direction.
	 * @param direction: 'F' for forward, 'L' for left, 'R' for right.
	 */
	public void cameraDirection(char direction) {

		// If direction is F, turn towards the front
		if (direction == 'F') {
			moveEyesToFront();
		}
		
		// If direction is L, turn towards the right
		else if (direction == 'L') {

			turnEyesToAngle(90);
		}
		
		// If direction is R, turn towards the left
		else if (direction == 'R') {
			turnEyesToAngle(-90);

		}
	}

}
