package test.plotbot;

/**
 * Interface marking a shape which can be plotted by the Plotbot.
 * 
 */
public interface Plottable {
	public void plot(PlotbotControl pc) throws BotException;
}
