package test.plotbot;

import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * Plotbot Class is the primary contact for the connection with LeJOS NXT Robot
 * to calibrate the robot, to draw an Anchor, to draw a Diamond inside Rectangle
 * or to draw a Line.
 * 
 * @authors Prashanth Reddy Ujjalli
 * @version 1.0
 * @since 2017-01-30
 */
public class Plotbot {
	public static PlotbotControl pc;
	public static Plottable s;

	/**
	 * This is the main method which receives all input data from user, with the
	 * help of MainMenu Class & then performs the needed actions by using
	 * PlotbotControl Class
	 * 
	 * @param args
	 *            Unused.
	 * @return null
	 */
	public static void main(String[] args) {
		// Some example code to check if the build process works
		LCD.drawString("Compiled successfully", 0, 0);
		LCD.drawString("Good Luck!", 0, 1);
		Button.ESCAPE.waitForPressAndRelease();
		
		pc = new PlotbotControl();
		MainMenu myMainMenu = new MainMenu();
		do{
			s = myMainMenu.select(); 
			try {
				s.plot(pc); // plot method in Calibration/ DiamondAndRectangle/ Anchor/ Line programs
			} catch (BotException e) {
				LCD.clear();
				LCD.drawString(e.getMsg(), 0, 1);
				Button.waitForAnyPress();
			}
		} while (!s.equals(null)); // after performing one task, asking user for next task
	}
}
