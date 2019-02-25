
import lejos.utility.Delay;

public class Robot {

	
	
	private char wall_state( boolean l, boolean m, boolean r) {
		
	
		//wall in the middle ahead, turn right.
		
		
		//in a corner with freedom on the right
		if(l && m && !r) {
			System.out.println("CORNER, TURN RIGHT");
			return 'R';
		}
		
		//in a corner with freedom on the left
		else if(!l && m && r) {
			
			System.out.println("CORNER, TURN LEFT");
			return  'L';
		} 
		
		else if(!l && m && !r) {
			System.out.println("WALL AHEAD");
			return 'R' ;
		}
		
		//surrounded by walls, back out, turn around, go
		else if(l && m && r) {
			
			System.out.println("SURROUNDED BY WALLS, TURN AROUND");

			
			return 'B';
		}
		
		System.out.println("FUCK");
		return 'B'; //default back and turn
		
	}
	//Sweeps the eyes back and forth. Returns true if a wall is sensed
	private char sweep_eyes(Rover this_rover, ISensors this_sensor) {
		
		boolean wall_on_left = false;
		boolean wall_middle = false;
		boolean wall_on_right = false;
		
		//Check Left 
		this_rover.turn_eyes_to_angle(-90);
		System.out.println("CHECK LEFT:");
		wall_on_left = this_sensor.IRSense(0.15f, 15);
		System.out.println("WALL ON LEFT:" + wall_on_left);

		Delay.msDelay(100);
	
		
		//Check Middle
		this_rover.eyes_to_front();
		
		System.out.println("CHECK MIDDLE:");
		wall_middle = this_sensor.IRSense(0.15f, 15);
		System.out.println("WALL MIDDLE" + wall_on_left);

		Delay.msDelay(100);

		
		//Check Right
		System.out.println("CHECK RIGHT:");
		this_rover.turn_eyes_to_angle(90);
		wall_on_right = this_sensor.IRSense(0.15f, 15);
		System.out.println("WALL ON RIGHT:" + wall_on_right);

		Delay.msDelay(100);

		this_rover.eyes_to_front();
		

		return(wall_state( wall_on_left, wall_middle, wall_on_right));

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
		char direction;
		
		//4 stage main loop
		while(true) {
			
			wall_detected_by_IR = false;
			wall_detected_by_Bumper = false;
			
			
			//MOVING FORWARDS
			while(!wall_detected_by_Bumper) {
				this_rover.move_forwards();
				wall_detected_by_Bumper = this_sensor.bumper_sensor();
			}
			
			//WALL HIT - MOVE BACKWARDS
			this_rover.move_backward();
			Delay.msDelay(100);
			this_rover.stop();
			
			//SEARCHING WITH IRSCANNER
			
			direction = this_robot.sweep_eyes(this_rover, this_sensor);
			
			if(direction == 'B') {
				this_rover.move_backward();
				Delay.msDelay(50);
				this_rover.stop();
				this_rover.turn_right_90();
			} else if (direction == 'L') {
				this_rover.turn_left_90();
			} else if (direction == 'R') {
				this_rover.turn_right_90();
			} 
			
		}
		
		
		
		
		
		
		
		
		
		
		/*
		while(true) {
			while(!wall_detected_by_Bumper) {
				
				wall_detected_by_IR = this_sensor.IRSense(100, 0, 41);
				
				
				wall_detected_by_Bumper = this_sensor.bumper_sensor();
	
				
				if(wall_detected_by_IR) {
	
					searching = true;
					this_rover.stop();
	
					//searching for no wall
					while(searching) {
						
						wall_detected_by_Bumper = this_sensor.bumper_sensor();
						searching = this_sensor.IRSense(100, 0, 41);
						this_rover.turn_to_angle(15);
					}
					
				}
				
				this_rover.move_forwards();
				wall_detected_by_IR = false;
				searching = false;
			}
			
			this_rover.move_backward();
			
			Delay.msDelay(1000);
			this_rover.stop();
			this_rover.turn_right_90();
			wall_detected_by_Bumper = false;
		
		}
		
		*/
	}	
}
