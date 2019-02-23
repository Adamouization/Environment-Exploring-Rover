import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class main {
	public static void main(String[] args) {
		IRover this_rover = new Rover(500, 300);
		
		Button.waitForAnyPress();
		
		this_rover.move_forwards();
		
		Delay.msDelay(300);
		
		this_rover.turn_left();
		
		Delay.msDelay(500);

		
		this_rover.turn_right();

		Delay.msDelay(700);
		
		this_rover.stop();
	}
	
}
