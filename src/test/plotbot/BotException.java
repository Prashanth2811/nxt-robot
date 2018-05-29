package test.plotbot;

public class BotException extends Throwable {
	private String Msg;
	
	public BotException (String ErrorMsg) {
		Msg = ErrorMsg;
	}
	
	public String getMsg() {
		return Msg;
	}
}
