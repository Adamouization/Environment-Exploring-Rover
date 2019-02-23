import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Rover implements IRover {

	private int speed;
	private int delay_time;
	
	public Rover(int speed, int delay_time) {
		this.speed = speed;
		this.delay_time = delay_time;
	}
	
	
	@Override
	public void move_forwards() {
		stop();
	    		
		Motor.B.forward();
		Motor.C.forward();
		Motor.B.setSpeed(speed);
		Motor.B.setSpeed(speed);
	}

	@Override
	public void stop() {
		Motor.B.stop(true);
		Motor.C.stop(true);
		
		Delay.msDelay(delay_time);

	}

	@Override
	public void turn_left() {
		stop();
	    
		
		Motor.B.forward();
		Motor.C.backward();
	}

	@Override
	public void turn_right() {
		stop();
	    		
		Motor.B.backward();
		Motor.C.forward();
	}

	@Override
	public void turn_by_angle(int angle) {
		// TODO Auto-generated method stub
	}

}
