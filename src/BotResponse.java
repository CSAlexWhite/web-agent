
public class BotResponse implements Runnable {

	String output;
	int min, max;
	
	public BotResponse(String Response){
		
		output= Response;
	}
	
	public void run() {
		
		int wait = min + (int)(Math.random()*(1000));
		
		try {Thread.sleep(wait);} catch (InterruptedException e){}
		Main.mainInterface.conversationArea.append("BOT ->\t" + output + "\n");			
	}
}
