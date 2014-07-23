import java.util.Vector;


public class ResponseMatrix {
	
//	private Vector<Vector<Response>> matrix;
//	
//	public ResponseMatrix(){
//		
//		matrix = new Vector<Vector<Response>>();
//		
//	}
	
	// test Matric with 100x100 array
	
	private int[][] matrix;
	
	public ResponseMatrix(int size){
		
		matrix = new int[size][];
		
		for(int i=0; i<size; i++){
			
			matrix[i] = new int[size];
		}	
	}
	
	
	
	

}