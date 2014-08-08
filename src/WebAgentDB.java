import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class which has methods and variables to do with the connection to the
 * mySQL server by which WebAgent backs up and can retrieve and analyze data.
 * @author Alex
 *
 */
public abstract class WebAgentDB{
  
	/* STATIC VARIABLES FOR DATABASE ACCESS */
	static Connection connection = null;		// DATABASE CONNECTION OBJECT
   	static Statement statement = null;			// CURRENT STATEMENT OBJECT
   	static boolean exists = false;				// WHETHER THE DB HAS BEEN BUILT
   	
	/**
	 * A method that connects to the mySQL database server on the current computer.
	 * It connects using "root" and "root" as username and password.
	 * Once connected, 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
   	public static void connect() throws SQLException, ClassNotFoundException{
		
   		/* CONNECT TO THE DATABASE, IF BROKEN THROW AN EXCEPTION AND TELL USER */
		Class.forName("com.mysql.jdbc.Driver");		
		try{connection = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "root");} 
		catch (SQLException se){System.out.println("Could not connect to database");}
		
		/* BUILD A STATEMENT OBJECT THAT THE DATABASE CAN INTERPRET */
		statement = connection.createStatement();	
		
		/* INITIALIZATION STEP:
		 * SQL INSTRUCTIONS TO MAKE THE DATABASE, SET IT TO BE USED, AND
		 * MAKE THE TWO IMPORTANT TABLES: CONVERSATIONS AND DICTIONARY, WHICH
		 * WILL HOLD INFORMATION ABOUT RESPONSES AND PAST RELATIONSHIPS, TO 
		 * BE ANALYZED.
		 * */
		instruct( "CREATE DATABASE WEBAGENT");
		
		instruct("USE WEBAGENT");
		
		instruct( "CREATE TABLE CONVERSATIONS"
				+ "(ID INTEGER not NULL AUTO_INCREMENT,"
				+ "rID INTEGER NOT NULL,"
				+ "USER BOOLEAN,"
				+ "lastID INTEGER,"
				+ "PRIMARY KEY (ID))");
				
		
		instruct( 
				"CREATE TABLE DICTIONARY " +
                "(rID INTEGER not NULL AUTO_INCREMENT, " +
                " LENGTH INTEGER not NULL, " + 
                " CONTENT VARCHAR(8192) NOT NULL, " + 
                " PRIMARY KEY ( rID ))");
	}
	
	/**
	 * A method to be called elsewhere, to easily pass SQL queries to be passed 
	 * to the db server.
	 * @param inputStatement
	 */
   	public static void instruct(String inputStatement){
		
   		/* BUILD THE STRING, SEND IT TO THE SERVER, THROW AN EXCEPTION IF 
   		 * SOMETHING IS BROKEN */
		String sql = inputStatement;
		try {statement = connection.createStatement();} catch (SQLException e) {e.printStackTrace();}
		try{statement.executeUpdate(sql);} 
		catch(SQLException se){ /*se.printStackTrace();*/}		
	}
	
	/**
	 * A method which adds new Responses to the Conversations database, called 
	 * during the back and forth bot and human interactions.
	 * @param id
	 * @param user
	 * @param previousID
	 */
   	public static void addToConvoDB(Integer id, Boolean user, Integer previousID){
		
   		/* CREATE A PREPARED STATEMENT, WHICH HELPS TO FORMAT AND PASS ALONG
   		 * SYNTACTICALLY CORRECT QUERIES, CATCH EXCEPTIONS*/
		try{
			String sql= "INSERT INTO conversations(rID, USER, lastID) VALUES(?, ?, ?)";
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.setInt(1, id);
			prepared.setBoolean(2, user);
			prepared.setInt(3, previousID);
			int val = prepared.executeUpdate();
			} catch (SQLException se){se.printStackTrace();}
				
	}
	
	static int errorcount = 0;
	public static void addToDictDB(Integer length, String content){
		
		/* CREATE A PREPARED STATEMENT, WHICH HELPS TO FORMAT AND PASS ALONG
   		 * SYNTACTICALLY CORRECT QUERIES, CATCH EXCEPTIONS */
		try{
			String sql= "INSERT INTO dictionary(LENGTH, CONTENT) VALUES(?, ?)";
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.setInt(1, length);
			prepared.setString(2, content);
			int val = prepared.executeUpdate();
		} catch (SQLException se){se.printStackTrace();}
				
	}
	
	/**
	 * A method which when passed a mySQL query, returns a corresponding result
	 * set if availabLe.  Useful in WebAgent's console mode.  Does console
	 * printing automatically.
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet queryDB(String query) throws SQLException{
		
		/* PASS THE DATABASE A QUERY, AND SAVE THE RESULTS AND THE
		 * METADATA IN OUTPUT OBJECTS */
		ResultSet output = statement.executeQuery(query);
		ResultSetMetaData metaData = output.getMetaData();
		
		/* GET EACH COLUMN NAME AND PRINT THEM AS HEADINGS */
		for(int i=1; i<=metaData.getColumnCount(); i++){
			System.out.print(metaData.getColumnName(i) + "\t");
		} System.out.println();
		
		/* GET ALL THE RESULTS AND PRINT THEM LINE BY LINE */	
		while(output.next()){
			
			for(int i=1; i<=metaData.getColumnCount(); i++){
				System.out.print(output.getString(i) + "\t");
			} System.out.println();
		}
		
		System.out.println();		
		return output;  		// WE RETURN AN OUTPUT AS WELL, TO EITHER BE
								// USED OR BECAUSE IT WILL THROW AN EXCEPTION IF
								// THE QUERY IS IMPROPERLY FORMULATED
	}
	
	/**
	 * A method which writes the contents of a result set to a file with a
	 * specified name, called by a console method.  Same basic structure as 
	 * printing to the console.
	 * @param toOutput
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	public static void rsToFile(ResultSet toOutput, String filename) throws FileNotFoundException, SQLException{
	
		/* INSTANTIATE THE PRINTWRITE TO HELP US WRITING TO FILE */
		PrintWriter newFile = new PrintWriter(filename + ".txt");
		
		/* SAVE THE METADATA In AN OUTPUT OBJECT */
		ResultSetMetaData metaData = toOutput.getMetaData();
		
		/* GET EACH COLUMN NAME AND PRINT THEM AS HEADINGS */
		for(int i=1; i<=metaData.getColumnCount(); i++){
			newFile.print(metaData.getColumnName(i) + "\t");
		} System.out.println();
		
		/* GET ALL THE RESULTS AND PRINT THEM LINE BY LINE */	
		while(toOutput.next()){
			
			for(int i=1; i<=metaData.getColumnCount(); i++){
				newFile.print(toOutput.getString(i) + "\t");
			} newFile.println();
		} newFile.close();
	}
	
	/**
	 * A method which returns how many rows there are in the Dictionary table, 
	 * useful for debugging the writing commands and server-client connection
	 * @return
	 */
	public static int count(){

		/* PASSES THE APPROPRIATE QUERY TO THE DATABASE, AND RETURNS THE 
		 * LENGTH OF THE DICTIONARY TABLE, CATCHES EXCEPTIONS */
		try{
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Dictionary");
			int count = resultSet.getInt(1);
			return count;
	
		} catch(SQLException se){se.printStackTrace();return 0;}
	}
	
	/**
	 * A short method called to shut down the database connection, called when 
	 * the program closes.
	 * @throws Exception
	 */
	public static void disconnect() throws Exception{
		
		statement.close();
		connection.close();	
	}
}
