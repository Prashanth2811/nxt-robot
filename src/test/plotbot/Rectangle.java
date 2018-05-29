package test.plotbot;


/**
 * Plotting routine of a rectangle.
 */
public class Rectangle implements Plottable{
	
	private Coord lowerLeftCorner;
	private Coord upperRightCorner;
	private Coord upperLeftCorner;
	private Coord lowerRightCorner;
	
	private Line leftLine;
	private Line lowerLine;
	private Line rightLine;
	private Line upperLine;
	
	public Rectangle(Coord coordLowerLeftCorner, Coord coordUpperRightCorner) {
		lowerLeftCorner = coordLowerLeftCorner;
		upperRightCorner = coordUpperRightCorner;
		
		//new objects of different coordinates and lines are created
		upperLeftCorner = new Coord(lowerLeftCorner.x,upperRightCorner.y); 
		lowerRightCorner = new Coord(upperRightCorner.x,lowerLeftCorner.y);
		
		leftLine = new Line(upperLeftCorner,lowerLeftCorner); 
		lowerLine = new Line(lowerLeftCorner,lowerRightCorner);
		rightLine = new Line(lowerRightCorner,upperRightCorner);
		upperLine = new Line(upperRightCorner,upperLeftCorner);
	}
	
	/**
	 * Plots the lines of the rectangle, in the end the pen is lifted
	 * @param pc - Object of PlotbotControl in order to move and control the robot
	 * @throws BotException
	 */
	public void plot(PlotbotControl pc)throws BotException {
		leftLine.plot(pc);
		lowerLine.plot(pc);
		rightLine.plot(pc);
		upperLine.plot(pc);
		pc.liftPen();		
	}
}
