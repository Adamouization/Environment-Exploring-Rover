import lejos.utility.Delay;


public class Robot {
	
	// define threshold constants for the Ultrasonic sensor
	public static final float THRESHOLD = 0.10f;
	public static final float THRESHOLD_FOLLOW = 0.40f;
	public static final float TOLERANCE = 0.02f;
	public static final float FOLLOWING_DISTANCE = 0.15f;

	/** main
	 * 
	 * Program entry point.
	 * @param args
	 */
	public static void main(String[] args) {
		// create new objects
		Robot this_robot = new Robot();
		Rover this_rover = new Rover(500, 100);
		ISensors this_sensor = new Sensors();		
		
		// booleans to keep track of system states
		boolean initial_state;
		boolean wall_following_state;
				
		

		// INITIAL STATE - move forward until bumper is pushed
		char direction = 'F';
		initial_state = true;
		wall_following_state = false;
		this_rover.move_forwards();
		
		System.out.println("INITIAL STATE");
		while(initial_state) {
			this_rover.move_forwards();
			initial_state = !checkBumper(this_sensor);
		}
		
		while(true) {
		
			//SEARCHING STATE - UNDERSTAND STATE OF THE ENVIRONMENT
			System.out.println("SEARCHING STATE");

			moveBack(this_rover); 
		
			direction = this_robot.sweepEyes(this_rover, this_sensor, direction); //sweep eyes and find a direction to move
			//Direction is the direction that is free, camera points in the OPPOSITE DIRECTION
			interpretDirection(this_rover, direction);
			
			
			//WALL FOLLOWING STATE - ROBOT IS PARALLEL TO A WALL WITH THE CAMERA FACING THE WALL
			System.out.println("WALL FOLLOWING STATE");
			wall_following_state = true;
			boolean is_bumper_pushed = false;
			while(true) {
				
				while(wall_following_state) {
					
					is_bumper_pushed = checkBumper(this_sensor);
					
					
					if(is_bumper_pushed) {
						break;
					}
						
					float distance_to_wall = this_sensor.ultrasoundSenseDistanceToWall(THRESHOLD, 50);
					
						
					if(distance_to_wall >= FOLLOWING_DISTANCE + TOLERANCE) {
						System.out.println("TOWARDS");
						
						//camera points LEFT
						if(direction == 'R') { 
							this_rover.curve_while_moving(-100);
						} 
						
						//camera points RIGHT
						else if(direction == 'L') { 
							this_rover.curve_while_moving(100);
						}
						
					} else if (distance_to_wall <= FOLLOWING_DISTANCE - TOLERANCE){
						System.out.println("AWAY");
		
						//camera points LEFT
						if(direction == 'R') { 
							this_rover.curve_while_moving(100);
						} 
						
						//camera points RIGHT
						else if(direction == 'L') { 
							this_rover.curve_while_moving(-100);
						}
												
					} else {
						this_rover.move_forwards();
					}
					
				}
				
				if(is_bumper_pushed) {
					break;
				}
				
			}
			this_rover.stop();
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
			System.out.println("CORNER, TURN RIGHT");
			return 'R';
		}
		
		//RIGHT BLOCKED, LEFT FREE
		else if(!l && r) {
			
			System.out.println("CORNER, TURN LEFT");
			return  'L';
		} 
		
		//RIGHT FREE, LEFT FREE
		else if(!l && !r) {
			System.out.println("WALL AHEAD, TURN R");
			return 'R' ;
		}
		
		//ALL BLOCKED
		else if(l && r) {
			System.out.println("SURROUNDED BY WALLS, TURN AROUND");
			return 'B';
		}
	
		return 'P';
		
	}
	
	/** sweepEyes
	 * 
	 * Sweeps the eyes back and forth
	 * @param this_rover
	 * @param this_sensor
	 * @return True if a wall is sensed
	 */
	private char sweepEyes(Rover this_rover, ISensors this_sensor, char opposite_of_current_eye_direction) {
		this_rover.eyes_to_front();
		
		boolean wall_on_left = false;
		boolean wall_on_right = false;
				
		if(opposite_of_current_eye_direction == 'R') {
			//Check Left 
			this_rover.turn_eyes_to_angle(-90);
			wall_on_left = this_sensor.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
		
			
			//Check Right
			this_rover.turn_eyes_to_angle(90);
			wall_on_right = this_sensor.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
		} else {	
			//Check Right
			this_rover.turn_eyes_to_angle(90);
			wall_on_right = this_sensor.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
			
			//Check Left 
			this_rover.turn_eyes_to_angle(-90);
			wall_on_left = this_sensor.ultrasoundSense(THRESHOLD, 15);
			Delay.msDelay(100);
		}
		
		this_rover.eyes_to_front();
		return(wallState( wall_on_left, wall_on_right));
	}
	
	/** checkBumper
	 * 
	 * 	Returns a boolean to specify if the bumper sensor was triggered or not.
	 * 
	 * @return True if bumper sensor triggered, False if not
	 */
	public static boolean checkBumper(ISensors this_sensor) {
		boolean bumper_pushed;
		bumper_pushed = this_sensor.bumper_sensor();
		return bumper_pushed;
	}
	
	/** moveBack
	 * 
	 * 	Inches the robot back a tiny bit away from whatever it has hit.
	 */
	public static void moveBack(Rover this_rover) {
		this_rover.stop();
		this_rover.move_backward();
		Delay.msDelay(200);
		this_rover.stop();
	}
	
	/** interpretDirection
	 * 
	 *  receives a direction as a char
	 *  Turns L, R or interprets middle as a right turn
	 *  Turns the camera in the opposite direction to the movement of the robot
	 */
	public static void interpretDirection(Rover this_rover, char direction) {
		//WALL AHEAD, TURN RIGHT
		if(direction == 'A') {
			this_rover.turn_right_90();
		} 
		
		//WALLS R AND AHEAD, TURN LEFT
		else if (direction == 'L') {
			this_rover.turn_left_90();
		} 
		
		//WALLS L AND AHEAD, TURN RIGHT
		else if (direction == 'R') {
			this_rover.turn_right_90();
		} 
		
		this_rover.camera_direction(direction);
	}
	
	/** inchForwards
	 * 
	 * Moves the rover forwards by a very small amount
	 * @param this_rover
	 */
	public static void inchForwards(Rover this_rover) {
		this_rover.move_forwards();
		Delay.msDelay(800);
		this_rover.stop();
	}

}
