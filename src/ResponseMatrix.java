/**
 * The representation of the memory graph as an adjacency matrix
 * @author Alex
 *
 */
public class ResponseMatrix {
	
	// test Matrix with size x size array
	
	public int[][] matrix;
	public int dimension;
	
	public int totalResponses;
	
	int start = 0; // start a the first user-entered response
	
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
		int fromID = from.getID();
		int toID = 0;
		int readValue = matrix[fromID][0];
		
		while(readValue == 0){
			
			toID++;
			readValue = matrix[fromID][toID];
		}		
		
		return Main.dictionary.getResponseAt(toID);
	}

	public void print(){
		
		dimension = Main.dictionary.nextEmpty;
		
		System.out.println("REPEAT ID : " + Main.dictionary.repeatID);
		System.out.print("\t");
		for(int i=0; i<dimension; i++) System.out.print(i + "  ");
		System.out.print("\tID\tResponse");
		System.out.println("\n");
		for(int i=0; i<dimension; i++){
			System.out.print(i + "\t");
			for(int j=0; j<dimension; j++)
				System.out.print(matrix[i][j] + "  ");
			System.out.println("\t" + Main.dictionary.getResponseAt(i).getID() 
					+ "\t" + Main.dictionary.getResponseAt(i).toString());
		}
		
		System.out.print("\n\t");
		for(int i=0; i<dimension; i++) System.out.print(i + "  ");
		System.out.println("");
	}
	
	String choice1, choice2;
	public void printC(){
		
		System.out.println("\n" + choice1 + " OR " + choice2);
		//System.out.println("Flip is: " + flip + "%2= " + flip%2);
	}
}