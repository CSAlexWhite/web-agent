import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Conversation {
	
	private Vector<String> convo;
	boolean training = false;
	
	public Conversation(String first){
		
		convo = new Vector<String>(0);
	}

	public void addNext(Response next){ 
		convo.add(next.toString()); 
	}
	
	public void addNext(String next){ 
		convo.add(next); 
	}
	
	public void readFile(String matrix, String responses) throws IOException{
		
		String nextLine = null;
		StringTokenizer lineToParse = null;
		
		BufferedReader inFile1 = new BufferedReader(new FileReader(matrix));
		BufferedReader inFile2 = new BufferedReader(new FileReader(responses));
		
		int rows = 0, cols = 0;
		
		while(inFile1.ready()){
			
			nextLine = inFile1.readLine();
			lineToParse = new StringTokenizer(nextLine, " ");
			
			while(lineToParse.hasMoreTokens()){
				Main.memory.matrix[rows][cols++] = 
						Byte.parseByte(lineToParse.nextToken());
			}
			
			rows++;
			Main.dictionary.nextEmpty++;
			Main.memory.dimension++;
		}	
		
		inFile1.close();		
		
		int lineNum = 0;
		while(inFile2.ready()){
			
			nextLine = inFile2.readLine();
			Main.dictionary.add(nextLine, lineNum);
		}
		
		inFile2.close();
	}
	
	public String writeFile() throws IOException{
		
//		String folderName = "Conversations";
//		Path path = Paths.get("matrix.data");
//		
//		File newFolder = new File("Conversations");

//		Files.createDirectories(path.getParent());
		
//		if(!newFolder.exists()) new File("/" + newFolder).mkdirs();
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
	
		PrintWriter newFile = 
				new PrintWriter(dateFormat.format(date) + ".txt");

		for(int i=0; i<convo.size(); i++){
			newFile.println(convo.elementAt(i));
		}
					
		newFile.close();
		
		return dateFormat.format(date).toString();
	}
	
	public void print(){ 
		System.out.println(convo); 
	}
}

