
import lejos.utility.Delay;

public class Robot {

	public static final float THRESHOLD = 0.1f;
	public static final float THRESHOLD_FOLLOW = 0.4f;

	private char wall_state( boolean l, boolean m, boolean r) {
		
	
		//wall in the middle ahead, turn right.
		
		
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
		
		else if(!l && !m && !r) {
			System.out.println("NO WALLS");
			return 'F';
		}
		
		return 'P'; //default back and turn
		
	}
	//Sweeps the eyes back and forth. Returns true if a wall is sensed
	private char sweep_eyes(Rover this_rover, ISensors this_sensor) {
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
		

		return(wall_state( wall_on_left, wall_middle, wall_on_right));

	}
	
	
	/** check_bumper
	 * 
	 * 	true if bumper sensor triggered
	 * 	false if not
	 * @return
	 */
	public static boolean check_bumper(ISensors this_sensor) {
		boolean bumper_pushed;
		bumper_pushed = this_sensor.bumper_sensor();
		return bumper_pushed;

	}
	
	/** move_back
	 * 
	 * 	inches the robot back a tiny bit away from whatever it has hit
	 */
	public static void move_back(Rover this_rover) {
		this_rover.stop();
		this_rover.move_backward();
		Delay.msDelay(200);
		this_rover.stop();
	}
	
	/** interpret_direction
	 * 
	 *  receives a direction as a char
	 *  Turns L, R or interprets middle as a right turn
	 *  Turns the camera in the opposite direction to the movement of the robot
	 */
	public static void interpret_direction(Rover this_rover, char direction) {
	 
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
	
	/** inch_forwards
	 * 
	 * Moves the rover forwards by a very small amount
	 * @param this_rover
	 */
	public static void inch_forwards(Rover this_rover) {
		this_rover.move_forwards();
		Delay.msDelay(800);
		this_rover.stop();
	}

	
	public static void main(String[] args) {
		Robot this_robot = new Robot();
		Rover this_rover = new Rover(400, 100);
		ISensors this_sensor = new Sensors();
		boolean wall_detected_by_IR = false;
		boolean wall_detected_by_Bumper = false;

		boolean searching = false;
		
		this_rover.move_forwards();
		wall_detected_by_IR = false;
		char direction = 'F';
		boolean following_wall = false;
		boolean wall_is_still_near = false;
		
		
		//STATES
		boolean initial_state;
		boolean wall_following_state;
		boolean bumper_pushed = false;
		
		//INITIAL STATE - MOVE FORWARDS UNTIL BUMPER IS PUSHED
		initial_state = true;
		wall_following_state = false;
		
		System.out.println("INITIAL STATE");
		while(initial_state) {
			this_rover.move_forwards();
			initial_state = !check_bumper(this_sensor);
		}
		
		while(true) {
		
			//SEARCHING STATE - UNDERSTAND STATE OF THE ENVIRONMENT
			System.out.println("SEARCHING STATE");

			move_back(this_rover); 
		
			direction = this_robot.sweep_eyes(this_rover, this_sensor); //sweep eyes and find a direction to move
			interpret_direction(this_rover, direction);
			
			
			//WALL FOLLOWING STATE - ROBOT IS PARALLEL TO A WALL WITH THE CAMERA FACING THE WALL
			System.out.println("WALL FOLLOWING STATE");
			wall_following_state = true;
			boolean is_bumper_pushed = false;
			while(true) {
				
				while(wall_following_state) {
					
					this_rover.move_forwards();	
					is_bumper_pushed = check_bumper(this_sensor);
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
				inch_forwards(this_rover);
				
				
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
}
