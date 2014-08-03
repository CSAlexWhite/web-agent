import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * The representation of the memory graph as an adjacency matrix
 * @author Alex
 *
 */
public class ResponseMatrix {
		
	public byte[][] matrix;
	public int dimension;
	
	public int totalResponses;
	
	int start = 0; // start a the first user-entered response
	
	/**
	 * The data structure for the stored conversation: a directed graph.  Implements 
	 * functionality to keep the graph up to date and to query it for Responses
	 * for the bot to return.
	 * @param size
	 */
	public ResponseMatrix(int size){
		
		dimension = size;
		matrix = new byte[size][size];	
		
		for(int i=0; i<size; i++) matrix[i] = new byte[size];			
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
	
	public void addEdge(int from, int to){ matrix[from][to]++;}

	/**
	 * The response choice algorithm.  Looks in the matrix for what to say next.
	 * @param from
	 * @return
	 */
	public Response getNext(Response from){
		
		dimension = Main.dictionary.nextEmpty - 2;
		int fromID = from.getID();
		int toID1, toID2, toID3;
		int readValue = matrix[fromID][1];
		
		Stack <Response> extants = new Stack<Response>();
		
		int maxValue = 0;
		int currentMax = 0;
		
//		for(int i=0; i<dimension; i++){ 
//			if(matrix[fromID][i] > maxValue){ 
//				maxValue = matrix[fromID][i];
//				currentMax = i;
//			}
//		}
//	
//		toID1 = currentMax;
		
		int notZeros = 0;
		for(int i=0; i<dimension; i++){ 
			if(matrix[fromID][i] > 0){ 
				notZeros++;
				extants.push(Main.dictionary.getResponseAt(i));
				System.out.print(Main.dictionary.getResponseAt(i) + " OR ");
			}
			if(matrix[fromID][i] > maxValue){ 
				maxValue = matrix[fromID][i];
				currentMax = i;
			}
		}
		
		toID1 = currentMax;
		System.out.print("\nMax is: " + Main.dictionary.getResponseAt(toID1) + "\n");
		if(extants.isEmpty()) return new Response("Huh?", 0);
		
		int test = (int)(Math.random()*100);
		
		if(maxValue > notZeros) return Main.dictionary.getResponseAt(toID1);
		if(test%3==0 || test%3==2) return Main.dictionary.getResponseAt(toID1);
		else return extants.elementAt((int)(Math.random()*extants.size()));
		
//		toID1 = currentMax;
		
//		while(readValue == 0 && toID1 < dimension){
//			
//			toID1++;
//			readValue = matrix[fromID][toID1];
//		}	
		
		//int toID2 = (int)(Math.random()*dimension)-1;
		//if(toID1 >= dimension) toID1 = (int)(Math.random()*dimension);
		//System.out.println("ID1 = " + toID1 + " and ID2 = " + toID2);
		//System.out.println("Range = " + dimension);
		
		
		//if((toID1+toID2)%2 == 0) 
		//	return Main.dictionary.getResponseAt(toID1);
		//else return Main.dictionary.getResponseAt(toID2);		
	}
	
	/**
	 * Reads in the matrix.data file to retrieve the stored graph for a new
	 * session
	 * @param filename
	 * @throws IOException
	 */
	public void readFile(String filename) throws IOException{
		
		int readSize = Main.dictionary.nextEmpty;
		
		String nextLine = null;		
		BufferedReader inFile = new BufferedReader(new FileReader(filename));
		StringTokenizer lineToParse;
				
		for(int i=0; i<readSize; i++){
			nextLine = inFile.readLine();
			lineToParse = new StringTokenizer(nextLine, " ");
			
			for(int j=0; j<readSize; j++)
				matrix[i][j] = Byte.parseByte(lineToParse.nextToken());		
		}
		
		inFile.close();		
	}
	
	/**
	 * Writes to the matrix.data file, to keep the graph persistent
	 * @param filename
	 * @throws IOException
	 */
	public void writeFile(String filename) throws IOException{
		
		int writeSize = Main.dictionary.nextEmpty;

		PrintWriter newFile = new PrintWriter(filename);
		
		for(int i=0; i<writeSize; i++){		
			for(int j=0; j<writeSize; j++) newFile.print(matrix[i][j] + " ");
						
			newFile.println();
		}
		
		newFile.close();
	}
	
	/**
	 * Prints the matrix, including x and y coordinates and a the responses they
	 * correspond to on the right.  Useful if you'd like to check the graph, for
	 * debugging or other reasons.
	 */
	public void print(){
		
		int printSize = Main.dictionary.nextEmpty;
		
		System.out.println("REPEAT ID : " + Main.dictionary.repeatID);
		System.out.print("\t");
		for(int i=0; i<printSize; i++) System.out.print(i + "  ");
		System.out.print("  \tID  Response");
		System.out.println("\n");
		for(int i=0; i<printSize; i++){
			System.out.print(i + "\t");
			for(int j=0; j<printSize; j++)
				System.out.print(matrix[i][j] + "  ");
			System.out.println("  \t" + Main.dictionary.getResponseAt(i).getID() 
					+ "  " + Main.dictionary.getResponseAt(i).toString());
		}
		
		System.out.print("\n\t");
		for(int i=0; i<printSize; i++) System.out.print(i + "  ");
		System.out.println("");
	}
	
	String choice1, choice2;
	public void printC(){
		
		System.out.println("\n" + choice1 + " OR " + choice2);
	}
}