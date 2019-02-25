import lejos.utility.Delay;


public class Robot {
	
	// define threshold constants for the Ultrasonic sensor
	public static final float THRESHOLD = 0.1f;
	public static final float THRESHOLD_FOLLOW = 0.4f;
	
	/** main
	 * 
	 * Program entry point.
	 * @param args
	 */
	public static void main(String[] args) {
		// create new objects
		Robot this_robot = new Robot();
		Rover this_rover = new Rover(400, 100);
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
		
			direction = this_robot.sweepEyes(this_rover, this_sensor); //sweep eyes and find a direction to move
			interpretDirection(this_rover, direction);
			
			
			//WALL FOLLOWING STATE - ROBOT IS PARALLEL TO A WALL WITH THE CAMERA FACING THE WALL
			System.out.println("WALL FOLLOWING STATE");
			wall_following_state = true;
			boolean is_bumper_pushed = false;
			while(true) {
				
				while(wall_following_state) {
					
					this_rover.move_forwards();	
					is_bumper_pushed = checkBumper(this_sensor);
					if(is_bumper_pushed) {
						break;
					}
					wall_following_state = this_sensor.ultrasoundSense(THRESHOLD_FOLLOW, 15); //CHECK IF WALL IS STILL THERE
					
					//CHECK DISTANCE TO WALL AND ADJUST MOTORS HERE
					System.out.println(wall_following_state);
				}
				
				if(is_bumper_pushed) {
					break;
				}
					
				this_rover.stop();
				inchForwards(this_rover);
				
				
				//If direction turned is left, wall was on the right, so a right turn is needed
				if(direction == 'L') {
					this_rover.turn_right_90();
				} else if (direction == 'R') {
					this_rover.turn_left_90();
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
	private char wallState(boolean l, boolean m, boolean r) {
		//LEFT and MIDDLE blocked, turn RIGHT
		if(l && m && !r) {
			System.out.println("CORNER, TURN RIGHT");
			return 'R';
		}
		
		//MIDDLE and RIGHT blocked, turn LEFT
		else if(!l && m && r) {
			
			System.out.println("CORNER, TURN LEFT");
			return  'L';
		} 
		
		//MIDDLE blocked, turn RIGHT
		else if(!l && m && !r) {
			System.out.println("WALL AHEAD, TURN R");
			return 'R' ;
		}
		
		//LEFT, MIDDLE, and RIGHT blocked, back out
		else if(l && m && r) {
			System.out.println("SURROUNDED BY WALLS, TURN AROUND");
			return 'B';
		}
		
		//NO WALLS DETECTED so keep going forward
		else if(!l && !m && !r) {
			System.out.println("NO WALLS");
			return 'F';
		}
		
		//DEFAULT back and turn
		return 'P';
		
	}
	
	/** sweepEyes
	 * 
	 * Sweeps the eyes back and forth
	 * @param this_rover
	 * @param this_sensor
	 * @return True if a wall is sensed
	 */
	private char sweepEyes(Rover this_rover, ISensors this_sensor) {
		this_rover.eyes_to_front();
		
		boolean wall_on_left = false;
		boolean wall_middle = false;
		boolean wall_on_right = false;
		
		//Check Left 
		this_rover.turn_eyes_to_angle(-90);
		wall_on_left = this_sensor.ultrasoundSense(THRESHOLD, 15);
		Delay.msDelay(100);
	
		
		//Check Middle
		this_rover.eyes_to_front();
		wall_middle = this_sensor.ultrasoundSense(THRESHOLD, 15);
		Delay.msDelay(100);

		
		//Check Right
		this_rover.turn_eyes_to_angle(90);
		wall_on_right = this_sensor.ultrasoundSense(THRESHOLD, 15);
		Delay.msDelay(100);

		this_rover.eyes_to_front();
		return(wallState( wall_on_left, wall_middle, wall_on_right));
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
