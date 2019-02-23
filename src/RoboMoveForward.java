import javax.sound.sampled.Port;

import lejos.*;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.*;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class RoboMoveForward {
	public static void main(String[] args) throws Exception {
		
		while(true) {
			Motor.C.forward();
			Motor.B.forward();
			
			
		    Delay.msDelay(1000);
		    
			Motor.C.stop(true);
		    Motor.B.stop(true);
			
		    Delay.msDelay(250);

		}
	}
}
