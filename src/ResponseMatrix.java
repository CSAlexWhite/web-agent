/**
 * The representation of the memory graph as an adjacency matrix
 * @author Alex
 *
 */
public class ResponseMatrix {
	
	// test Matrix with size x size array
	
	public int[][] matrix;
	private int dimension;
	
	public int totalResponses;
	
	String choice1;
	String choice2;
	int flip;
	int tempTo;
	int start = 1; // start a the first user-entered response
	
	public ResponseMatrix(int size){
		
		dimension = size;
		matrix = new int[size][size];	
		
		for(int i=0; i<size; i++) matrix[i] = new int[size];			
		for(int i=0; i<size; i++){			
			for(int j=0; j<size; j++) matrix[i][j] = 0;			
		}	
		
		//matrix[0][0] = 1;
	}
	
	/**
	 * Given a response to and from, creates an edge between them, or, if such
	 * an edge exists, adds weight to it.
	 * 
	 * @param from
	 * @param to
	 */
	public void addEdge(Response from, Response to){
		
		dimension = Main.dictionary.nextEmpty;
		matrix[from.getID()][to.getID()]++;
	}
	
	/**
	 * For directly adding an edge
	 * @param from
	 * @param to
	 */
	public void addEdge(int from, int to){
		
		matrix[from][to]++;
	}
	
	public Response getNext(Response from){
		
		dimension = Main.dictionary.nextEmpty;
		int maxTest = 0;
		int tempTo1 = 0, tempTo2 = 0;
		tempTo = 0;
		
		// CURRENT RESPONSE CHOICE ALGORITHM!		
		//find the highest weight edge
		int tempMax = matrix[from.getID()][0];
		for(int i=start; i<dimension; i++){
					
			if(matrix[from.getID()][i] > tempMax){ 
				tempMax = matrix[from.getID()][i];
				tempTo2 = i;
			}
			
		}//Find another edge
		tempTo1 = (int)(Math.random()*(dimension));
		
		while(matrix[from.getID()][tempTo1] == 0 
				&& maxTest < 100 
				&& tempTo1 == tempTo2
				&& tempTo1 <= start)
		{			
			tempTo1 = (int)(Math.random()*(dimension));
			maxTest++;
		}
				
		flip = (int)(Math.random()*dimension);
		if(flip%2 == 0) tempTo = tempTo1;
		if(flip%2 == 1) tempTo = tempTo2;
		
		choice1 = Main.dictionary.getResponseAt(tempTo1).toString();
		choice2 = Main.dictionary.getResponseAt(tempTo2).toString();
		//System.out.println(choice1 + " OR " + choice2);
		
		// if I found a real response
		//if(matrix[from.getID()][tempTo] != 0) 
		return Main.dictionary.getResponseAt(tempTo);
		//return Main.dictionary.getResponseAt(0);
	}

	public void print(){
		
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++)
				System.out.print(matrix[i][j] + "   ");
			System.out.println();
		}
	}
	
	public void printC(){
		
		System.out.println("\n" + choice1 + " OR " + choice2);
		System.out.println("Flip is: " + flip + "%2= " + flip%2);
	}

}