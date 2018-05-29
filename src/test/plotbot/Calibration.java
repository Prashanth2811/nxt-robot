package test.plotbot;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Calibration implements Plottable {

	public void plot(PlotbotControl pc) throws BotException{
		
		
		LCD.clear();
		LCD.drawString("Calibration starts!", 0, 1);
		//pc.quickCalibration();
			
		pc.calibrateY();
		LCD.drawString("Y is calibrated", 0, 3);
		Button.waitForAnyPress();

		pc.calibratePenControl();
		LCD.drawString("Pen is calibrated", 0, 4);
		Button.waitForAnyPress();
			
		pc.calibrateSwivelAngle();
		LCD.drawString("Swivel Angle is calibrated", 0, 5);
		LCD.drawString("Calibration completed!", 0, 6);
			
		Button.waitForAnyPress();
		LCD.clear();
	
	}

}
