public class Robot {

	public static void main(String[] args) {
		Rover this_rover = new Rover(200, 100);
		ISensors this_sensor = new Sensors();
		boolean wall_detected_by_IR = false;
		
		while(true) {
			//MOVING STATE
			while(!wall_detected_by_IR) {
				System.out.println("MOVING STATE");
				this_rover.move_forwards();
				wall_detected_by_IR = this_sensor.IRSense(100, 0, 50);
			}
			
			//this_rover.stop();
			
			//SEARCHING STATE
			while(wall_detected_by_IR) {
				System.out.println("TURNING STATE");
				this_rover.turn_right();
				this_rover.stop();
				wall_detected_by_IR = this_sensor.IRSense(100, 0, 50);
			}
			
			this_rover.move_backward();

				
		}
	}	
}
