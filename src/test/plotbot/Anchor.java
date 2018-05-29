package test.plotbot;

	/**
	 * Plotting routine for a anchor.
	 */
public class Anchor implements Plottable{
	
	private Coord lowerLeftCorner;
	private int width;
	
	private Coord lowerLeftCornerForDiamond;
	private Coord upperRightCornerForDiamond;
	private Diamond diamondAnchor;
	
	private Coord lowerVerticalLineBottom; 
	private Coord lowerVerticalLineTop;
	private Coord lowerHorizontalLineRight;
	private Coord lowerHorizontalLineLeft;
	private Coord upperVerticalLineBottom;
	private Coord upperVerticalLineTop;
	private Coord upperHorizontalLineRightR;
	private Coord upperHorizontalLineLeftL;
	private Coord upperVerticalLineRightBottom;
	private Coord upperVerticalLineLeftBottom;
	
	private Line lowerVerticalLine;
	private Line lowerHorizontalLine;
	private Line upperVerticalLine;
	private Line upperHorizontalLineRight;
	private Line upperVerticalLineRight;
	private Line upperHorizontalLineLeft;
	private Line upperVerticalLineLeft;
	
	public Anchor(Coord coordLowerLeftCorner, int widthAnchor) {
		
		lowerLeftCorner = coordLowerLeftCorner;
		width = widthAnchor;
		
		//new objects of different coordinates, Lines and one diamond are created
		lowerLeftCornerForDiamond = new Coord(lowerLeftCorner.x + width/4,lowerLeftCorner.y); 
		upperRightCornerForDiamond = new Coord(lowerLeftCorner.x + width*3/4,lowerLeftCorner.y + width/2);
		diamondAnchor = new Diamond(lowerLeftCornerForDiamond,upperRightCornerForDiamond);
		
		lowerVerticalLineBottom = new Coord(lowerLeftCorner.x + width/2, lowerLeftCorner.y + width/2);
		lowerVerticalLineTop = new Coord(lowerVerticalLineBottom.x, lowerVerticalLineBottom.y + width/4);
		lowerHorizontalLineRight = new Coord(lowerVerticalLineBottom.x + width/4, lowerVerticalLineTop.y);
		lowerHorizontalLineLeft = new Coord(lowerVerticalLineBottom.x - width/4, lowerVerticalLineTop.y);
		upperVerticalLineBottom = lowerVerticalLineTop;
		upperVerticalLineTop = new Coord(lowerVerticalLineBottom.x, lowerVerticalLineTop.y + width*5/4);
		upperHorizontalLineRightR = new Coord(lowerVerticalLineBottom.x + width/2, upperVerticalLineTop.y);
		upperHorizontalLineLeftL = new Coord(lowerVerticalLineBottom.x - width/2, upperVerticalLineTop.y);
		upperVerticalLineRightBottom = new Coord(lowerHorizontalLineRight.x, upperVerticalLineTop.y - width/4);
		upperVerticalLineLeftBottom = new Coord(lowerHorizontalLineLeft.x, upperVerticalLineTop.y - width/4);
		
		lowerVerticalLine = new Line(lowerVerticalLineBottom,lowerVerticalLineTop); 
		lowerHorizontalLine = new Line(lowerHorizontalLineLeft,lowerHorizontalLineRight);
		upperVerticalLine = new Line(upperVerticalLineBottom,upperVerticalLineTop);
		upperHorizontalLineRight = new Line(upperVerticalLineTop,upperHorizontalLineRightR);
		upperVerticalLineRight = new Line(upperHorizontalLineRightR,upperVerticalLineRightBottom);
		upperHorizontalLineLeft = new Line(upperVerticalLineTop,upperHorizontalLineLeftL);
		upperVerticalLineLeft = new Line(upperHorizontalLineLeftL,upperVerticalLineLeftBottom);
	}
	
	/**
	 * Plots the lines of the anchor
	 * @param pc - Object of PlotbotControl in order to move and control the robot
	 * @throws BotException
	 */
	public void plot(PlotbotControl pc)throws BotException {
		diamondAnchor.plot(pc);
		lowerVerticalLine.plot(pc);
		lowerHorizontalLine.plot(pc);
		upperVerticalLine.plot(pc);
		upperHorizontalLineRight.plot(pc);
		upperVerticalLineRight.plot(pc);
		upperHorizontalLineLeft.plot(pc);
		upperVerticalLineLeft.plot(pc);
		pc.liftPen();
	}
}
