import lejos.hardware.Button;
import lejos.utility.Delay;


/**
 * Contains the methods that define the robot's states 
 * and the main function to start controlling it.
 * @authors Tom Slattery, Adam Jaamour
 */
public class Robot {
	
	// define threshold constants for the Ultrasonic sensor
	public static final float THRESHOLD = 0.10f;
	public static final float TOLERANCE = 0.02f;
	public static final float FOLLOWING_DISTANCE = 0.15f; // change this to determine how closely the robot will follow the wall

	/** main
	 * 
	 * Program entry point.
	 * @param args
	 */
	public static void main(String[] args) {
		
		// create new objects
		Robot robot = new Robot();
		Rover rover = new Rover(500);
		ISensors sensors = new Sensors();		
		
		// booleans to keep track of system states
		boolean isInitialState;
		boolean isWallFollowingState;
			
		// keep track of the number of times the touch sensor was activated for the experience.
		int numberOfBumps = 0;
		
		// wait for a button to be pressed to start the robot
        Button.waitForAnyPress();

		// INITIAL STATE - move forward until bumper is pushed
		char direction = 'F';
		isInitialState = true;
		isWallFollowingState = false;
		rover.moveForward();
		
		// System.out.println("INITIAL STATE");
		while(isInitialState) {
			rover.moveForward();
			
			isInitialState = !checkBumper(sensors);
			
			if(!isInitialState) {
				numberOfBumps++;
			}
		}
		
		while(true) {
			System.out.println(numberOfBumps);

			//SEARCHING STATE - UNDERSTAND STATE OF THE ENVIRONMENT
			//System.out.println("SEARCHING STATE");

			moveBack(rover); 
		
			direction = robot.sweepEyes(rover, sensors, direction); //sweep eyes and find a direction to move
			//Direction is the direction that is free, camera points in the OPPOSITE DIRECTION
			interpretDirection(rover, direction);
			
			//WALL FOLLOWING STATE - ROBOT IS PARALLEL TO A WALL WITH THE CAMERA FACING THE WALL
			//System.out.println("WALL FOLLOWING STATE");
			isWallFollowingState = true;
			boolean isBumperPushed = false;
			while(true) {
				while(isWallFollowingState) {
					
					isBumperPushed = checkBumper(sensors);
					if(isBumperPushed) {
						numberOfBumps++;
					}
					
					if(isBumperPushed) {
						break;
					}
						
					float distanceToWall = sensors.ultrasoundSenseDistanceToWall(THRESHOLD, 50);
						
					if(distanceToWall >= FOLLOWING_DISTANCE + TOLERANCE) {
						//camera points LEFT
						if(direction == 'R') { 
							rover.curveWhileMoving(-100);
						} 
						//camera points RIGHT
						else if(direction == 'L') { 
							rover.curveWhileMoving(100);
						}
						
					} 
					
					else if (distanceToWall <= FOLLOWING_DISTANCE - TOLERANCE){
						//camera points LEFT
						if(direction == 'R') { 
							rover.curveWhileMoving(100);
						} 
						//camera points RIGHT
						else if(direction == 'L') { 
							rover.curveWhileMoving(-100);
						}
												
					} 
					
					else {
						rover.moveForward();
					}
				}
				
				if(isBumperPushed) {
					break;
				}
			}
			rover.stop();
		}
	}	

	/** wallState
	 * 
	 * @param l
	 * @param m
	 * @param r
	 * @return char
	 */
	private char wallState(boolean l, boolean r) {
		//LEFT and MIDDLE blocked, turn RIGHT
				
		//LEFT BLOCKED, RIGHT FREE
		if(l && !r) {
			return 'R';
		}
		
		//RIGHT BLOCKED, LEFT FREE
		else if(!l && r) {
			return  'L';
		} 
		
		//RIGHT FREE, LEFT FREE
		else if(!l && !r) {
			return 'R' ;
		}
		
		//ALL BLOCKED
		else if(l && r) {
			return 'B';
		}
	
		return 'P';
		
	}
	
	/** sweepEyes
	 * 
	 * Sweeps the eyes back and forth
	 * @param rover
	 * @param sensors
	 * @return True if a wall is sensed
	 */
	private char sweepEyes(Rover rover, ISensors sensors, char oppositeOfCurrentEyeDirection) {
		rover.moveEyesToFront();
		
		boolean isWallOnLeft = false;
		boolean isWallOnRight = false;
				
		if(oppositeOfCurrentEyeDirection == 'R') {
			//Check Left 
			rover.turnEyesToAngle(-90);
			isWallOnLeft = sensors.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
			
			//Check Right
			rover.turnEyesToAngle(90);
			isWallOnRight = sensors.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
		} 
		
		else {	
			//Check Right
			rover.turnEyesToAngle(90);
			isWallOnRight = sensors.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
			
			//Check Left 
			rover.turnEyesToAngle(-90);
			isWallOnLeft = sensors.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
		}
		
		rover.moveEyesToFront();
		return(wallState(isWallOnLeft, isWallOnRight));
	}
	
	/** checkBumper
	 * 
	 * 	Returns a boolean to specify if the bumper sensor was triggered or not.
	 * 
	 * @return True if bumper sensor triggered, False if not
	 */
	public static boolean checkBumper(ISensors sensors) {
		boolean isBumperPushed;
		isBumperPushed = sensors.bumperSensor();
		return isBumperPushed;
	}
	
	/** moveBack
	 * 
	 * 	Inches the robot back a tiny bit away from whatever it has hit.
	 */
	public static void moveBack(Rover rover) {
		rover.stop();
		rover.moveBackward();
		Delay.msDelay(200);
		rover.stop();
	}
	
	/** interpretDirection
	 * 
	 *  receives a direction as a char
	 *  Turns L, R or interprets middle as a right turn
	 *  Turns the camera in the opposite direction to the movement of the robot
	 */
	public static void interpretDirection(Rover rover, char direction) {
		//WALL AHEAD, TURN RIGHT
		if(direction == 'A') {
			rover.turnRight90();
		} 
		
		//WALLS R AND AHEAD, TURN LEFT
		else if (direction == 'L') {
			rover.turnLeft90();
		} 
		
		//WALLS L AND AHEAD, TURN RIGHT
		else if (direction == 'R') {
			rover.turnRight90();
		} 
		
		rover.cameraDirection(direction);
	}
	
	/** inchForwards
	 * 
	 * Moves the rover forwards by a very small amount
	 * @param rover
	 */
	public static void inchForwards(Rover rover) {
		rover.moveForward();
		Delay.msDelay(800);
		rover.stop();
	}

}