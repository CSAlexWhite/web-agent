import java.util.Vector;


public class ResponseList {
	
	Vector<Response> MasterList;

	public ResponseList(){
		
		MasterList = new Vector<Response>(0);
	}
	
	Response getResponseAt(int idNumber){
		
		return MasterList.get(idNumber);
	}
	
	Response findResponse(String target){
		
		for(int i=0; i<MasterList.size(); i++){
			
			if(MasterList.get(i).getContent() == target) 
				
				return MasterList.get(i);
		}
	}
}
