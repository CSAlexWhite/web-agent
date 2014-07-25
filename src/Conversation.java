
public class Conversation {
	
	String convo= null;
	
	public Conversation(String first){
		
		convo = first.toString();
	}
	
	public void addNext(Response next){
		
		convo += " / " + next.toString();
	}
	
	public void addNext(String next){
		
		convo += " / " + next;
	}
	
	public void print(){
		
		System.out.println(convo);
	}

}
