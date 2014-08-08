
/**
 * For storing bot responses, mainly to allow a short pause on a separate thread
 * to improve the interactivity of the machine.
 * @author Alex
 *
 */
public class BotResponse implements Runnable {

	String output;		// THE CONTENT OF THE RESPONSE
	
	/**
	 * The BotResponse constructor which just sets the content.
	 * @param Response
	 */
	public BotResponse(String Response){output= Response;}
	
	/**
	 * A necessary function of classes implementing runnable, this manages
	 * the new thread, in this case to create a pause when conversing with the
	 * bot.
	 */
	public void run() {
		
		/* WANT TO WAIT SOME RANDOM TIME BETWEEN 500 AND 1500 MILLISECONDS */
		int wait = 500 + (int)(Math.random()*(1000));
		
		/* THEN OUTPUT THE RESPONSE TO THE CONSOLE */
		try {Thread.sleep(wait);} catch (InterruptedException e){return;}
		
		/* AND ADD IT TO THE CONVERSATION TEXT AREA OF THE GUI */
		Main.mainInterface.conversationArea.append("BOT ->\t" + output + "\n");
	}
}
