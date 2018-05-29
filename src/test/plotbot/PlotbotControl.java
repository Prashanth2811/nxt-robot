package test.plotbot;


import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;


/**
 *  This Class provides an easy-to-use control for the NXT-Plotbot. 
 *  It can be used for the safe and precise use of the Plotbot. It controls not only the 
 *  correct location of the robot at any time but it also moves to the new desired location
 *  in the absolute coordinate system. By constant updating the motor angle and checking of
 *  the boundaries the robot cannot be crashed.
 *
 */
public class PlotbotControl {
	
	private final int LIGHT_SENSOR_POS 	= 105;
	private final int ARM_LENGTH 		= 80;
	private final int WHEEL_DIAMETER 	= 56;
	private final int ARM_GEAR_RATIO 	= 84;
	private final int WHEEL_GEAR_RATIO 	= 5;
	private final int LIGHT_THRESHOLD 	= 30; // empirically validated
	private final int LS_STABLE			= 10;
	
//	private final double SPEED_CORRECTION_FACTOR = 2.2; // empirically validated
	private final int MOTOR_PLAY_C 	= 5;
	
	private static int maxX 		= 0; 		// static variable; easily accessible
	
	private final int Y_UPPER_BOUND 	= 230;
	private final int Y_LOWER_BOUND 	= 0;
	
	private int maxSwivelAngle 			= 0;
	private int liftedPenMotorAngle		= 0;
	private int dropedPenMotorAngle		= 0;
	
	private Coord currentLocation 		= new Coord(0,0);
	
	private boolean yIsCalibrated 			= false;
	private boolean swivelAngleIsCalibrated = false;
	private boolean penIsCalibrated 		= false;
	
	private boolean penIsLifted				= true;
	
	private TouchSensor tsArm 	= new TouchSensor(SensorPort.S1);
	private TouchSensor tsPen	= new TouchSensor(SensorPort.S2);
	private LightSensor ls 		= new LightSensor(SensorPort.S3, false);
	
	public PlotbotControl() {

	}
	
	public static int getMaxXValue(){
		return maxX;
	}

	/**
	 * Returns the pen status, 
	 * @return pen status: true if up, false if down
	 */
	
	public boolean isPenLifted() {
		return penIsLifted;
	}
	
	/**
	 * Returns the current location in the coordinate system
	 * @return current location of the pen in the coordinate system
	 */
	public Coord getLocation() {
		return currentLocation;
	}
	

	/**
	 * Moves the pen in a straight line from it's current position to the 
	 * input coordinate.
	 * @param target: coordinate that is targeted by the robot
	 * @throws BotException if the robot is out of its boundaries or the 
	 * targeted coordinates are out of range
	 */
	public void moveToXY(Coord target) throws BotException {
		
		int heightOfLine = Math.abs(target.y - currentLocation.y);
		int widthOfLine = Math.abs(target.x - currentLocation.x);
		
		while(target.x > currentLocation.x && target.y > currentLocation.y) {
			Motor.A.backward();
			
//			double vXX = ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.cos(Math.toRadians(getSwivelAngle()));
//			double vYY = -(- ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.sin(Math.toRadians(getSwivelAngle()))) + vXX*(heightOfLine/widthOfLine);
			
			double vY = calculateVy() + calculateVx()*(heightOfLine/widthOfLine);
			
			
			Motor.C.setSpeed( getMotorSpeedFromVy(vY) );
			Motor.C.forward();
			
			setCurrentLocation(false);
			
		}
		
		while(target.x > currentLocation.x && target.y < currentLocation.y) {
			Motor.A.backward();
			
//			double vXX = ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.cos(Math.toRadians(getSwivelAngle()));
//			double vYY = -(- ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.sin(Math.toRadians(getSwivelAngle()))) - vXX*(heightOfLine/widthOfLine);
			
			
			double vY = calculateVy() - calculateVx()*(heightOfLine/widthOfLine);
			
			Motor.C.setSpeed( getMotorSpeedFromVy(vY) );
			Motor.C.forward();
			
			setCurrentLocation(false);
			
		}
		
		while(target.x < currentLocation.x && target.y > currentLocation.y) {
			Motor.A.forward();
			
//			double vXX = ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.cos(Math.toRadians(getSwivelAngle()));
//			double vYY = -(- ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.sin(Math.toRadians(getSwivelAngle()))) + vXX*(heightOfLine/widthOfLine);
			
			double vY = calculateVy() + calculateVx()*(heightOfLine/widthOfLine);
			
			
			Motor.C.setSpeed( getMotorSpeedFromVy(vY) );
			Motor.C.forward();
			
			setCurrentLocation(false);
			
		}
		
		while(target.x < currentLocation.x && target.y < currentLocation.y) {
			Motor.A.forward();
			
//			double vXX = ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.cos(Math.toRadians(getSwivelAngle()));
//			double vYY = -(- ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.sin(Math.toRadians(getSwivelAngle()))) - vXX*(heightOfLine/widthOfLine);
			
			double vY = calculateVy() - calculateVx()*(heightOfLine/widthOfLine);
			
			
			Motor.C.setSpeed( getMotorSpeedFromVy(vY) );
			Motor.C.forward();
			
			setCurrentLocation(false);
			
		}
	}
	
	
	public void moveToX(int x) throws BotException {
		if (!yIsCalibrated || !swivelAngleIsCalibrated) {
			throw new BotException("ERROR: PlotBot is not calibrated!");
		}
		
		while ( x < currentLocation.x) {
			Motor.A.forward();
			
//			double vXX = ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.cos(Math.toRadians(getSwivelAngle()));
//			double vYY = -(- ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.sin(Math.toRadians(getSwivelAngle())));
			
			double vY = calculateVy();
			
			Motor.C.setSpeed( getMotorSpeedFromVy(vY) );
			Motor.C.forward();
			
			setCurrentLocation(false);
		}
		Motor.A.stop();
		Motor.C.stop();
		while ( x > currentLocation.x) {
			Motor.A.backward();
		
//			double vXX = ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.cos(Math.toRadians(getSwivelAngle()));
//			double vYY = -(- ARM_LENGTH * (Motor.A.getSpeed()/360)/ARM_GEAR_RATIO * Math.sin(Math.toRadians(getSwivelAngle())));
			
			double vY = calculateVy();
			
			
			Motor.C.setSpeed( getMotorSpeedFromVy(vY) );
			Motor.C.forward();
			
			setCurrentLocation(false);
		}
		Motor.A.stop();
		Motor.C.stop();
		
		Button.waitForAnyPress();
		
//		moveToY(startingY);
	}
	
	/**
	 * Moves the robot-arm to the desired y-coordinate by turning the wheels
	 * @param y: desired y-coordinate
	 * @throws BotException
	 */
	public void moveToY(int y) throws BotException {
		
		if (!yIsCalibrated || !swivelAngleIsCalibrated) {
			throw new BotException("ERROR: PlotBot is not calibrated!");
		}
		if (y < Y_LOWER_BOUND || y > Y_UPPER_BOUND) {
			throw new BotException("ERROR: Y-coordinate is out of range!");
		}
		
		while (y < currentLocation.y) {
			Motor.C.backward();
			setCurrentLocation(false);
		}
		Motor.C.stop();
		
		while (y > currentLocation.y) {
			Motor.C.forward();
			setCurrentLocation(false);
		}
		Motor.C.stop();
	}
	
	/**
	 * Moves the pen in a straight line to the new y coordinate.
	 * When in calibration Mode, boundaries will not be considered.
	 * @param y: target y-coordinate
	 * @param calibrationMode: can be set to true to 
	 * @throws BotException
	 */
	
	private void moveToY(int y, boolean calibrationMode) throws BotException {
		
		if(!calibrationMode) {
			if (!yIsCalibrated || !swivelAngleIsCalibrated) {
				throw new BotException("ERROR: PlotBot is not calibrated!");
			}
			if (y < Y_LOWER_BOUND || y > Y_UPPER_BOUND) {
				throw new BotException("ERROR: Y-coordinate is out of range!");
			}
		}
		
		while (y < currentLocation.y) {
			Motor.C.backward();
			setCurrentLocation(calibrationMode);
		}
		Motor.C.stop();
		
		while (y > currentLocation.y) {
			Motor.C.forward();
			setCurrentLocation(calibrationMode);
		}
		Motor.C.stop();
	}
	
	/**
	 * Drops the pen to the paper.
	 * @throws BotException
	 */
	
	public void dropPen() throws BotException {
		if(!penIsCalibrated) {
			throw new BotException("ERROR: Pen control is not calibrated.");
		}
		
		Motor.B.rotateTo(dropedPenMotorAngle);
		penIsLifted = false;
	}
	
	/**
	 * Lifts the pen off the paper; tested and works
	 * @throws BotException
	 */
	
	public void liftPen() throws BotException {
		if(!penIsCalibrated) {
			throw new BotException("ERROR: Pen control is not calibrated.");
		}
		
		Motor.B.rotateTo(liftedPenMotorAngle);
		penIsLifted = true;
	}

	/**
	 * Calibrates the y-location of the robot
	 * @throws BotException
	 */
	
	public void calibrateY() throws BotException {
		int whiteGround, whiteGroundOld =0;
		
		LCD.clear();
		LCD.drawString("Calibrating y-coordinate...", 0, 4);
		
		whiteGround = ls.readNormalizedValue();
		
		//wait for the stabilization of the light sensor
		while (whiteGroundOld < whiteGround + LS_STABLE && whiteGroundOld > whiteGround -LS_STABLE) {
			whiteGroundOld = whiteGround;
			whiteGround = ls.readNormalizedValue();
		}
		
		Motor.C.setSpeed(360);
		
		while (ls.readNormalizedValue() > (whiteGround - LIGHT_THRESHOLD) && !tsArm.isPressed()) {
			Motor.C.backward();	
		}
		Motor.C.stop();
		
		Motor.C.resetTachoCount();
		currentLocation.y = 0;
		
		
		moveToY(LIGHT_SENSOR_POS - ARM_LENGTH + MOTOR_PLAY_C, true);
		
		
		Motor.C.resetTachoCount();
		currentLocation.y = 0;
		
		yIsCalibrated = true;
		
		LCD.clear();
		LCD.drawString("y-calibration completed.", 0, 4);
	}
	
	/**
	 * Calibrates the swivel angle of the robot. with the assumption that it is located
	 * in the center at the beginning.
	 * @return
	 */
	
	public void calibrateSwivelAngle() {
		
		LCD.clear();
		LCD.drawString("calibrating swivel angle...", 0, 2);
		
		Motor.A.resetTachoCount(); 	// Reset the tacho-count. Assumption: Arm is in center.
		currentLocation.x = 0;		// Reset the location. Assumption: Arm is in center.
		
		while (!tsArm.isPressed()) {
			Motor.A.backward();
			LCD.drawString("SwivelAngle: " + getSwivelAngle(), 0, 4);
		}
		Motor.A.stop();
		
		maxSwivelAngle = getSwivelAngle();
		
		Motor.A.rotateTo( - Motor.A.getTachoCount() );
		Motor.A.rotateTo(0);
		
		maxX = (int) ( ARM_LENGTH * Math.sin( Math.toRadians(maxSwivelAngle) ) );
		
		swivelAngleIsCalibrated = true;
		
		LCD.clear();
		LCD.drawString("Swivel angle calibrated.", 0, 4);
	}
	
	/**
	 * Calibrates the pen control of the robot. When Pen is up, it is dropped by pressing 
	 * the touch-sensor on the right side of the robot.
	 * @throws BotException
	 */
	public void calibratePenControl() throws BotException{
		
		LCD.clear();
		LCD.drawString("calibrating pen...", 0, 4);
		
		while (!tsPen.isPressed()) {
			Motor.B.forward();
		}
		Motor.B.stop();
		Motor.B.resetTachoCount();
		
		while (true) { 						// wait for pressing the Button
			boolean i = false;
			while(tsArm.isPressed()) { 		// lower the Pen as long as the Button is pressed
				Motor.B.backward();	
				i = true;
			}
			Motor.B.stop();
			if (i) break;
		}
		dropedPenMotorAngle = Motor.B.getTachoCount();
		
		penIsCalibrated = true;
		
		liftPen();
		
		LCD.clear();
		LCD.drawString("Pen.calibration completed.", 0, 4);
	}
	
	
	/**
	 * sets the current location of the robot dependent on the measured motor-angles of the wheel motor 
	 * and the motor at the swivel angle. When calibration mode is turned on, the robot will not consider
	 * any boundaries in y-direction.
	 * @param calibrationMode: true for on, false for off
	 * @throws BotException in case of the robot leaving its boundaries
	 */
	private void setCurrentLocation(boolean calibrationMode) throws BotException{
		if(!calibrationMode) {
			if (!yIsCalibrated) {
				throw new BotException("ERROR: Y-coordinate is not calibrated.");
			}
			if (!swivelAngleIsCalibrated) {
				throw new BotException("ERROR: Swivel angle is not calibrated.");
			}
		}
		
		currentLocation.x = (int) ( Math.sin(Math.toRadians(getSwivelAngle())) * ARM_LENGTH);
		currentLocation.y = (int) ( (Motor.C.getTachoCount() * Math.PI * WHEEL_DIAMETER) / (360 * WHEEL_GEAR_RATIO) 
									- ARM_LENGTH * (1 - Math.cos(Math.toRadians(getSwivelAngle()))) );

		LCD.drawString("x: " + currentLocation.x, 0, 7);
		LCD.drawString("y: " + currentLocation.y, 8, 7);
		
		if ( !calibrationMode &&	
				(	
					getSwivelAngle() > maxSwivelAngle || getSwivelAngle() < - maxSwivelAngle 
					||	Motor.C.getTachoCount() < 0 - 10 || currentLocation.y > Y_UPPER_BOUND + 10
				)
			) {
			Motor.A.flt();
			Motor.B.flt();
			Motor.C.flt();
			throw new BotException("ERROR: Robot out of control!");
		}
	}
	
	/**
	 * Calculates the current swivel angle dependent on the rotation of motor A. 
	 * @return
	 */
	private int getSwivelAngle() {
		return - Motor.A.getTachoCount() / ARM_GEAR_RATIO;
		
	}
	
	/**
	 * calculates the motor speed of motor C dependent on the desired speed in y-direction.
	 * @param vY: desired speed in y-direction
	 * @return motor speed in deg/sec
	 */
	private int getMotorSpeedFromVy( double vY ) {
//		return (int) (SPEED_CORRECTION_FACTOR*360 * vY * WHEEL_GEAR_RATIO / ( WHEEL_DIAMETER * Math.PI ) );
		return (int) ( 360* vY * WHEEL_GEAR_RATIO / ( WHEEL_DIAMETER * Math.PI ) );
	}
	
	/**
	 * Moves the robot arm to the desired x-coordinate without changing the y-coordinate
	 * @param x
	 * @throws BotException
	 */
	private double calculateVx() {
//		return - ( Math.sin(Math.toRadians( -Motor.A.getSpeed()/ARM_GEAR_RATIO) ) * ARM_LENGTH );
		return ARM_LENGTH * ( Motor.A.getRotationSpeed() * 2*Math.PI/360 ) / ARM_GEAR_RATIO *Math.cos(Math.toRadians(getSwivelAngle()));
	}
	
	/**
	 * calculates the necessary speed in y-direction to keep the y-coordinate steady whilst the x-coordinate
	 * changes.
	 * @return 
	 */
	private double calculateVy() {
//		return calculateVx() * ( ( 1-Math.cos(Math.toRadians(getSwivelAngle())) ) / Math.sin( Math.toRadians(getSwivelAngle()) ));
		return - ( -ARM_LENGTH * (Motor.A.getRotationSpeed() * 2*Math.PI/360 ) / ARM_GEAR_RATIO * Math.sin(Math.toRadians(getSwivelAngle())));
	}

	
//////////////////////////////////////TEST ENVIRONMENT/////////////////////////////////////////////	
	
	/**
	 * calibrates the robot in a quick skip-it-all way. The angles where measured up-front and there is 
	 * a safety factor considered.
	 */
	public void quickCalibration() {
		currentLocation.x = 0;
		currentLocation.y = 0;
		
		Motor.A.resetTachoCount();
		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();
		
		maxSwivelAngle = 45;
		maxX = (int) ( ARM_LENGTH * Math.sin(maxSwivelAngle) );
		
		dropedPenMotorAngle = -520;
		
		penIsCalibrated = true;
		yIsCalibrated = true;
		swivelAngleIsCalibrated = true;
	}
	
	/**
	 * tests the light sensor and returns the raw value that is measured.
	 * @return the raw light-value of the light sensor
	 */
	public int testSensorLight() {
		ls.setFloodlight(true);
		int x = ls.getLightValue();
		ls.setFloodlight(false);
		return x;
	}
	
	/**
	 * tests all touch sensors and turns on the light of the light sensor, when a sensor is pressed
	 */
	public void testSensors() {
		while(true) {
			while (tsPen.isPressed() || tsArm.isPressed()) {
				ls.setFloodlight(false);
			}
			ls.setFloodlight(true);
			if (tsPen.isPressed()) {
				return;
			}
		}
	}
	
	/**
	 * Tests the throwing of an exception.
	 * @throws BotException
	 */
	
	public void exceptionTest() throws BotException {
		if (tsArm.isPressed()) throw new BotException("Test passed.");
	}
	
	public void moveMotorA() {
		while(true) {
			while(tsArm.isPressed()) {
				Motor.A.backward();
			}
			Motor.A.stop();
			if (tsPen.isPressed()) {
				return;
			}
		}
	}
	
	/**
	 * moves the pen when the touch-sensor at the arm is pressed
	 */
	public void moveMotorB() {
		while(true) {
			while(tsArm.isPressed()) {
				Motor.B.backward();
			}
			Motor.B.stop();
		}
	}
	
	/**
	 * moves the robot in y direction while the touch sensor at the arm is pressed.
	 */
	public void moveMotorC() {
		while(true) {
			while(tsArm.isPressed()) {
				Motor.C.forward();
			}
			Motor.C.stop();
		}
	}
	
}
