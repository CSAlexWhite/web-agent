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
		
	public byte[][] matrix;	// THE ADJACENCY MATRIX REPRESENTATION OF THE GRAPH
							// SAVED IN BYTES, BECAUSE IT MIGHT GET REALLY BIG
	public int dimension;	// THE CURRENT SIZE OF THE GRAPH, FOR PRINTING
	
	int start = 0; // START AT THE FIRST USER-GENERATED RESPONSE
	
	/**
	 * The data structure for the stored conversation: a directed graph.  Implements 
	 * functionality to keep the graph up to date and to query it for Responses
	 * for the bot to return.
	 * @param size
	 */
	public ResponseMatrix(int size){
		
		dimension = size;				// GET THE SIZE TO MAKE IT
		matrix = new byte[size][size];	// INSTANTIATE THE OUTER ARRAY
		
		for(int i=0; i<size; i++) 		// INSTANTIATE THE INNER ARRAY
			matrix[i] = new byte[size]; 
		
		for(int i=0; i<size; i++){		// SET IT ALL EQUAL TO ZERO	
			for(int j=0; j<size; j++) 
				matrix[i][j] = 0;			
		}	
	}
	
	/**
	 * Given a response to and from, creates an edge between them, or, if such
	 * an edge exists, adds weight to it.
	 * 
	 * @param from
	 * @param to
	 */
	public void addEdge(Response from, Response to){
		
		dimension = Main.dictionary.nextEmpty;		// UPDATE THE DIMENSION
		matrix[from.getID()][to.getID()]++;			// ADD WEIGHT TO THE GRAPH
	}
	
	public void addEdge(int from, int to){ matrix[from][to]++;}

	/**
	 * AI ALGORITHM
	 * The response choice algorithm.  Looks in the matrix for what to say next.
	 * @param from
	 * @return
	 */
	public Response getNext(Response from){
		
		dimension = Main.dictionary.nextEmpty - 2;	// GET THE DIMENSION
		int toID1, fromID = from.getID();		 	// GET THE RESPONSE ID
		
		/* A STACK TO PUSH EXISTING EDGES ONTO AS THEY'RE FOUND */
		Stack <Response> extants = new Stack<Response>();  
		
		int maxValue = 0, currentMax = 0, notZeros = 0; // FOR TRACKING WEIGHTS		
		 					
		/* LOOK ACROSS THE ROW VECTOR FOR THE CURRENT RESPONSE */
		for(int i=0; i<dimension; i++){ 
			
			/* IF WE FOUND ANYTHING, PUSH IT AS A RESPONSE TO THE STACK */
			if(matrix[fromID][i] > 0){ 				
				notZeros++;
				extants.push(Main.dictionary.getResponseAt(i));
			}
			
			/* KEEP TRACK OF THE CURRENT MAXIMUM WEIGHTED EDGE */
			if(matrix[fromID][i] > maxValue){ 
				maxValue = matrix[fromID][i];
				currentMax = i;
			}
		}
		
		/* SET THE CURRENT MAX */
		toID1 = currentMax;
		
		/* IF NO NATURAL (OR UNNATURAL EVEN) RESPONSE, REPLY AS FOLLOWS */
		if(extants.isEmpty()) return new Response("Huh?", 0);
		
		/* FIND A RANDOM RESPONSE THAT MAY OR MAY NOT BE THE HIGHEST WEIGHT
		 * BY PICKING A RANDOM NUMBER BETWEEN ZERO AND THE SIZE OF THE STACK 
		 * GIVE A TWO-THIRDS CHANGE TO PICK THE RANDOM ONE, AND ONE-THIRD
		 * TO PICK THE HIGHEST WEIGHTED ONE */
		int test = (int)(Math.random()*100);	
		if(maxValue > notZeros) return Main.dictionary.getResponseAt(toID1);
		if(test%3==0 || test%3==2) return Main.dictionary.getResponseAt(toID1);
		else return extants.elementAt((int)(Math.random()*extants.size()));	
	}
	
	/**
	 * Reads in the matrix.data file to retrieve the stored graph for a new
	 * session
	 * @param filename
	 * @throws IOException
	 */
	public void readFile(String filename) throws IOException{
		
		System.out.print("REBUILDING MEMORY ...");
		int readSize = Main.dictionary.nextEmpty;		
		
		String nextLine = null;		
		BufferedReader inFile = new BufferedReader(new FileReader(filename));
		StringTokenizer lineToParse;
		
		/* TO RECONSTITUTE THE MATRIX, DOUBLE FOR LOOP */
		int i=0; // SO WE CAN USE I LATER TO PRINT THE SIZE
		for(i=0; i<readSize; i++){
			
			nextLine = inFile.readLine();
			lineToParse = new StringTokenizer(nextLine, " ");
			
			/* PARSE THE BYTES IN THE LINE, ADD TO MATRIX */
			for(int j=0; j<readSize; j++)
				matrix[i][j] = Byte.parseByte(lineToParse.nextToken());	
			
		} System.out.println("\t\t" + i*i + " bytes.");
		
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
		
		/* WRITES THE MATRIX, LINE BY LINE, TO THE APPROPRIATE DATA FILE */
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
		
		/* HERE WE PRINT THE MATRIX TO THE CONSOLE, USING COORDINATES
		 * AND ON THE RIGHT, THE RESPONSE CONTENTS, FOR REFERENCE AND
		 * DEBUGGING OF THE EDGE-ADDING AND RESPONSE-FINDING ALGORITHMS */
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
	
	/* ANOTHER DEBUGGING FEATURE, TO OUTPUT DECISION VARIABLES OF THE AI */
	String choice1, choice2;
	public void printC(){
		
		System.out.println("\n" + choice1 + " OR " + choice2);
	}
}