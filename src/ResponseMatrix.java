/**
 * The representation of the memory graph as an adjacency matrix
 * @author Alex
 *
 */
public class ResponseMatrix {
	
	// test Matrix with size x size array
	
	public int[][] matrix;
	private int dimension;
	
	public ResponseMatrix(int size){
		
		dimension = size;
		matrix = new int[size][size];	
		
		for(int i=0; i<size; i++) matrix[i] = new int[size];			
		for(int i=0; i<size; i++){			
			for(int j=0; j<size; j++) matrix[i][j] = 0;			
		}	
		
		matrix[1][0] = 1;
	}
	
	/**
	 * Given a response to and from, creates an edge between them, or, if such
	 * an edge exists, adds weight to it.
	 * 
	 * @param from
	 * @param to
	 */
	public void addEdge(Response from, Response to){
			
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
	
	public String getNext(Response from){
		
		int maxTest = 0;
		
		// CURRENT RESPONSE CHOICE ALGORITHM!		
		//Find an edge to go down
		int tempTo = (int)(Math.random()*(dimension));
		
		while(matrix[from.getID()][tempTo] == 0 && maxTest < 100){
			
			tempTo = (int)(Math.random()*(dimension));
			maxTest++;
			//System.out.println(tempTo);
		}
		
		// if I found a real response
		if(matrix[from.getID()][tempTo] != 0) 
			return Main.dictionary.getResponseAt(tempTo).getContent();
		
		// otherwise pick a random response
		int temp1 = (int)(Math.random()*(dimension));
		int temp2 = (int)(Math.random()*(dimension));
		
		while(matrix[temp1][temp2] == 0){
			
			temp1 = (int)(Math.random()*(dimension));
			System.out.print(temp1 + "  ");
			temp2 = (int)(Math.random()*(dimension));
			System.out.println(temp2);
		}
		
		addEdge(temp1, temp2);
		
		return Main.dictionary.getResponseAt(temp1).getContent();
		//return Main.dictionary.getResponseAt(0).getContent();
	}
	
	public void print(){
		
		for(int i=0; i<dimension; i++){
			for(int j=0; j<dimension; j++)
				System.out.print(matrix[i][j] + "  ");
			System.out.println();
		}
	}

}