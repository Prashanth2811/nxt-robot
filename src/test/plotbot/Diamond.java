package test.plotbot;

import lejos.nxt.LCD;

/**
 * Plotting routine of a diamond.
 */
public class Diamond implements Plottable{
	
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
	
	public Diamond(Coord coordLowerLeftCorner, Coord coordUpperRightCorner) {
		lowerLeftCorner = coordLowerLeftCorner;
		upperRightCorner = coordUpperRightCorner;
		widthRectangle = upperRightCorner.x - lowerLeftCorner.x; //width of rectangle in which the diamond is plotted
		heightRectangle = upperRightCorner.y - lowerLeftCorner.y; //height of rectangle in which the diamond is plotted
		
		//new objects of different coordinates and lines are created
		lowerCornerDiamond 	= new Coord(lowerLeftCorner.x + widthRectangle/2, lowerLeftCorner.y);
		upperCornerDiamond 	= new Coord(upperRightCorner.x - widthRectangle/2,upperRightCorner.y);
		rightCornerDiamond 	= new Coord(upperRightCorner.x,lowerLeftCorner.y + heightRectangle/2);
		leftCornerDiamond 	= new Coord(lowerLeftCorner.x,lowerLeftCorner.y + heightRectangle/2);
		
		upperLeftLineDiamond = new Line(upperCornerDiamond,leftCornerDiamond); 
		lowerLeftLineDiamond = new Line(leftCornerDiamond,lowerCornerDiamond);
		lowerRightLineDiamond = new Line(lowerCornerDiamond,rightCornerDiamond);
		upperRightLineDiamond = new Line(rightCornerDiamond,upperCornerDiamond);
	}
	
	/**
	 * Plots the lines of the diamond
	 * @param pc - Object of PlotbotControl in order to move and control the robot
	 * @throws BotException
	 */
	public void plot(PlotbotControl pc)throws BotException {	
//		LCD.drawString("We are in Diamond!!", 0, 6);
		upperLeftLineDiamond.plot(pc);
		lowerLeftLineDiamond.plot(pc);
		lowerRightLineDiamond.plot(pc);
		upperRightLineDiamond.plot(pc);
		pc.liftPen();
	}
}
