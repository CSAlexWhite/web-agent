import java.util.Random;
import java.util.Vector;


public class Response {

	private String content;
	private Vector <Response> nexts;
	private int id;
	
	public Response(String input){
		
		content = input;
		nexts = new Vector<Response>(0);
	}
	
	public void addNext(Response input){
		
		nexts.add(input);
	}
	
	public Response next(){
		
		// CURRENT RESPONSE CHOICE ALGORITHM!
		Random rand = new Random();
		
		int index = rand.nextInt(nexts.size());	
		return nexts.get(index);
	}
	
	public String getContent(){
		
		return content;
	}
}
