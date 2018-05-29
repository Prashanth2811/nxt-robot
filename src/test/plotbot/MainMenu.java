package test.plotbot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.TextMenu;

/**
 * MainMenu program, acts as an User Interface to provide needed inputs for
 * LeJOS NXT Robot.
 * 
 * @authors Prashanth Reddy Ujjalli
 * @version 1.0
 * @since 2017-01-30
 */
public class MainMenu {
	private static final String[] ITEMS = { "Calibration", "Diamond", "Anchor", "Line" };
	private static final String TITLE = "Choose Shape to draw:";
	private TextMenu menu;
	private static DiamondAndRectangle diamondAndRectangle;
	private static Anchor anchor;
	private static Line line;
	private final int Y_UPPER_BOUND = 230;
	private final int STEP_SIZE = 10;

	/**
	 * Creates a new MainMenu object, which allows user to select the action
	 * needs to be done by robot with usage of TextMenu
	 */
	public MainMenu() {
		menu = new TextMenu(ITEMS, 1, TITLE);
	}

	/**
	 * This is the method used to get inputs from user for the shapes to be
	 * drawn, based on the selection from TextMenu object
	 * 
	 * @param null
	 * @return Plottable object (Eg: Calibration/ Diamond/ Anchor/ Line)
	 */
	public Plottable select() {
		int selection = -1;
		do {
			selection = menu.select();
		} while (selection < 0);

//		while (Button.ENTER.isDown()) {
//		}
		Plottable toDraw = null;
		
		switch (selection) {
		case 0:
			toDraw = new Calibration();
			break;
		case 1:
			toDraw = inputsForDiamondAndRectangle();
			break;
		case 2:
			toDraw = inputsForAnchor();
			break;
		case 3:
			toDraw = inputsForLine();
			break;
//		default:
//			LCD.drawString("Invalid Input", 0, 6);
//			toDraw = new MainMenu().select();
		}
		return toDraw;
	}

	/**
	 * This is the method used to get inputs [P1(x1,y1), P2(x2,y2)] from user,
	 * for the Line to be drawn & creates Line object with selected inputs
	 * 
	 * @param null
	 * @return Line object
	 */
	private Line inputsForLine() {
		LCD.clear();
		LCD.drawString("Enter Details of line", 0, 0);
		int xMax = PlotbotControl.getMaxXValue(); // Max movement of Arm in X-axis direction
		
		LCD.drawString("X1 : ", 0, 1);
		int x1 = 0;
		LCD.drawInt(x1, 9, 1);
		Button.waitForAnyPress();
		x1 = changeValue(xMax, -xMax, x1, 9, 1); // x1 should be in its bounds
		
		LCD.drawString("Y1 : ", 0, 2);
		int y1 = 10;
		LCD.drawInt(y1, 8, 2);
		Button.waitForAnyPress();
		y1 = changeValue(Y_UPPER_BOUND, 0, y1, 8, 2); // y1 should be in its bounds
		
		LCD.drawString("X2 : ", 0, 3);
		int x2 = 10;
		LCD.drawInt(x2, 9, 3);
		Button.waitForAnyPress();
		x2 = changeValue(xMax, -xMax, x2, 9, 3); // x2 should be in its bounds
		
		LCD.drawString("Y2 : ", 0, 4);
		int y2 = 10;
		LCD.drawInt(y2, 8, 4);
		Button.waitForAnyPress();
		y2 = changeValue(Y_UPPER_BOUND, 0, y2, 8, 4); // y2 should be in its bounds
		
		Coord coord1 = new Coord(x1, y1);
		Coord coord2 = new Coord(x2, y2);
		
		line = new Line(coord1, coord2);
		return line;
	}

	/**
	 * This is the method used to get inputs [Height, Width of Rectangle] from
	 * user, for the Diamond to be drawn (Inside Rectangle) & creates
	 * DiamondAndRectangle object with selected inputs
	 * 
	 * @param null
	 * @return DiamondAndRectangle object
	 */
	private DiamondAndRectangle inputsForDiamondAndRectangle() {
		LCD.clear();
		LCD.drawString("Enter Details of Rectangle", 0, 0);
		int xMax = PlotbotControl.getMaxXValue(); // Max movement of Arm in X-axis direction
		
		LCD.drawString("Height : ", 0, 1);
		int height = 10;
		LCD.drawInt(height, 9, 1);
		Button.waitForAnyPress();
		height = changeValue(Y_UPPER_BOUND, 10, height, 9, 1); // height should be in its bounds
		
		LCD.drawString("Width : ", 0, 2);
		int width = 10;
		LCD.drawInt(width, 8, 2);
		Button.waitForAnyPress();
		width = changeValue(2*xMax, 10, width, 8, 2); // width should be in its bounds
		
		int xLowerLeft = -width/2;
		int yLowerLeft = Y_UPPER_BOUND - height;
		Coord coordLowerLeft = new Coord(xLowerLeft, yLowerLeft);
		
		int xUpperRight = xLowerLeft + width;
		int yUpperRight = yLowerLeft + height;
		Coord coordUpperRight = new Coord(xUpperRight, yUpperRight);
		
		diamondAndRectangle = new DiamondAndRectangle(coordLowerLeft, coordUpperRight);
		return diamondAndRectangle;
	}

	/**
	 * This is the method used to get input [Width] from user, for the Anchor to
	 * be drawn, then calculates it's Height & creates Anchor object with
	 * selected inputs
	 * 
	 * @param null
	 * @return Anchor object
	 */
	private Anchor inputsForAnchor() {
		// Ask for width, X Position of Anchor, calculate height. Then call
		// method in Anchor.java
		LCD.clear();
		LCD.drawString("Enter details of Anchor", 0, 0);
		int xMax = PlotbotControl.getMaxXValue(); // Max movement of Arm in X-axis direction
		
		LCD.drawString("Width : ", 0, 1);
		int width = 10;
		LCD.drawInt(width, 8, 1);
		Button.waitForAnyPress();
		width = changeValue(2*xMax, 10, width, 8, 1); // width should be in its bounds
		
		int xLowerLeft = -width/2;
		int height = 2 * width;
		int yLowerLeft = Y_UPPER_BOUND - height;
		Coord coordLowerLeft = new Coord(xLowerLeft, yLowerLeft);
		
		anchor = new Anchor(coordLowerLeft, width);
		return anchor;
	}

	/**
	 * This is the method used to Increment/ Decrement the variable value with
	 * specified STEP_SIZE. Left Button on LeJOS will decrement the value, Right
	 * Button will increment the value & Enter button finalises the value for
	 * the specified variable
	 * 
	 * @param upperBound, lowerBound, currentValue, 
	 *            columnPositionInLCDDisplay, rowPositionInLCDDisplay 
	 *            of the variable
	 * @return Integer (Value for the particular Variable)
	 */
	private int changeValue(int upperBound, int lowerBound, int startValue ,int lcdColumn, int lcdRow) {
		int var = startValue;
		while (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
			if (Button.RIGHT.isDown() && startValue < upperBound) {
				var += STEP_SIZE;
			} else if (Button.LEFT.isDown() && var > lowerBound) {
				var -= STEP_SIZE;
			}
			LCD.drawInt(var, lcdColumn, lcdRow);
			Button.waitForAnyPress();
		}
		if (Button.ENTER.isDown()){
			return var;
		} else
			return changeValue(upperBound, lowerBound, startValue, lcdColumn, lcdRow);
	}
}
