package test.plotbot;

	/**
	 *Plotting routine for a line.
	 */
public class Line implements Plottable{
	
	private Coord start; 	//start coordinate of the line
	private Coord end; 		//end coordinate of the line
	Coord currentCoord; 	//coordinate of the current position
	
	public Line(Coord coordStart, Coord coordEnd){
		start 	= coordStart;
		end 	= coordEnd;
		currentCoord = new Coord(start.x,start.y); //creates a new object of currentCoord
	}
	/**
	 * This method plots a line
	 * @param pc - Object of PlotbotControl in order to move and control the robot
	 * @throws BotException
	 */
	public void plot(PlotbotControl pc) throws BotException{
		
		checkLocationAndMoveToStart(pc); //moves to the start position of the line and drops the pen
		
		if(start.x == end.x){ //vertical line
			pc.moveToY(end.y);
		} else if (start.y == end.y) { //horizontal line
			pc.moveToX(end.x);
		} else {
			pc.moveToXY(end);
		}
	}
	
	/**
	 * This method checks if the current location of the pen is equal to the start position to draw a line.
	 * If not, the pen is moved to the start position.
	 * @param pc - Object of PlotbotControl in order to move and control the robot
	 * @throws BotException
	 */
	public void checkLocationAndMoveToStart(PlotbotControl pc) throws BotException{
		if(currentCoord.x !=  pc.getLocation().x || currentCoord.y != pc.getLocation().y) {
			if(pc.isPenLifted() == false) {
				pc.liftPen();
			}
			pc.moveToXY(currentCoord);
		}
		pc.dropPen();
	}
}
