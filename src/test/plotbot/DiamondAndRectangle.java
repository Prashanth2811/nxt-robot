package test.plotbot;

import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * Plotting routine of a diamond and a rectangle.
 */

public class DiamondAndRectangle implements Plottable{
	
	private Coord lowerLeftCorner;
	private Coord upperRightCorner;
	
	private int widthRectangle; 
	private int heightRectangle; 
	
	private Coord lowerCornerDiamond;
	private Coord upperCornerDiamond;
	private Coord rightCornerDiamond;
	private Coord leftCornerDiamond;
	
	private Line upperLeftLineDiamond; 
	private Line lowerLeftLineDiamond; 
	private Line lowerRightLineDiamond; 
	private Line upperRightLineDiamond;
	
	private Rectangle rectangle;
	
	public DiamondAndRectangle(Coord coordLowerLeftCorner, Coord coordUpperRightCorner) {
		
		lowerLeftCorner = coordLowerLeftCorner;
		upperRightCorner = coordUpperRightCorner;
		widthRectangle = upperRightCorner.x - lowerLeftCorner.x; //width of rectangle
		heightRectangle = upperRightCorner.y - lowerLeftCorner.y; //height of rectangle
		
		//objects of different coordinates, lines and one rectangle are created
		lowerCornerDiamond 	= new Coord(lowerLeftCorner.x + widthRectangle/2, lowerLeftCorner.y);
		upperCornerDiamond 	= new Coord(upperRightCorner.x - widthRectangle/2,upperRightCorner.y);
		rightCornerDiamond 	= new Coord(upperRightCorner.x,lowerLeftCorner.y + heightRectangle/2);
		leftCornerDiamond 	= new Coord(lowerLeftCorner.x,lowerLeftCorner.y + heightRectangle/2);
		
		upperLeftLineDiamond = new Line(upperCornerDiamond,leftCornerDiamond); 
		lowerLeftLineDiamond = new Line(leftCornerDiamond,lowerCornerDiamond);
		lowerRightLineDiamond = new Line(lowerCornerDiamond,rightCornerDiamond);
		upperRightLineDiamond = new Line(rightCornerDiamond,upperCornerDiamond);
		
		rectangle = new Rectangle(coordLowerLeftCorner, coordUpperRightCorner); //creates a new object of a rectangle
	}
	
	/**
	 * Plots the lines of the diamond an the rectangle
	 * @param pc - Object of PlotbotControl in order to move and control the robot
	 * @throws BotException
	 */
	public void plot(PlotbotControl pc)throws BotException {
		
//		LCD.clear();
//		LCD.drawString("we are in Diamond", 0, 1);
//		Button.waitForAnyPress();
		
		rectangle.plot(pc);
		
		upperLeftLineDiamond.plot(pc);
		lowerLeftLineDiamond.plot(pc);
		lowerRightLineDiamond.plot(pc);
		upperRightLineDiamond.plot(pc);
	}
}

