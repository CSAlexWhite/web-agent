import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class WebAgentDB{
  
    static Connection connection = null;
   	static Statement statement = null;
   	static boolean exists = false;
   	
	public static void connect() throws SQLException, ClassNotFoundException{
		
		Class.forName("com.mysql.jdbc.Driver");		
		try{connection = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "root");} 
		catch (SQLException se){System.out.println("Could not connect to database");}
		statement = connection.createStatement();	
		
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
	
	public static void instruct(String inputStatement){
		 
		String sql = inputStatement;
		try {statement = connection.createStatement();} catch (SQLException e) {e.printStackTrace();}
		try{statement.executeUpdate(sql);} 
		catch(SQLException se){ /*se.printStackTrace();*/}		
	}
	
	public static void addToConvoDB(Integer id, Boolean user, Integer previousID){
		
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
		
		
		try{
			String sql= "INSERT INTO dictionary(LENGTH, CONTENT) VALUES(?, ?)";
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.setInt(1, length);
			prepared.setString(2, content);
			int val = prepared.executeUpdate();
		} catch (SQLException se){se.printStackTrace();}
				
	}
	
	public static ResultSet queryDB(String query) throws SQLException{
		
		ResultSet output = statement.executeQuery(query);
		ResultSetMetaData metaData = output.getMetaData();
		
		for(int i=1; i<=metaData.getColumnCount(); i++){
			System.out.print(metaData.getColumnName(i) + "\t");
		}
		
		System.out.println();
		
		while(output.next()){
			
			for(int i=1; i<=metaData.getColumnCount(); i++){
				System.out.print(output.getString(i) + "\t");
			}
			System.out.println();
		}
		
		System.out.println();
		
		return output;
	}
	
	public static void rsToFile(ResultSet toOutput, String filename) throws FileNotFoundException, SQLException{
	
		PrintWriter newFile = 
				new PrintWriter(filename + ".txt");
		
		ResultSetMetaData metaData = toOutput.getMetaData();
		
		for(int i=1; i<=metaData.getColumnCount(); i++){
			newFile.print(metaData.getColumnName(i) + "\t");
		}
		
		System.out.println();
		
		while(toOutput.next()){
			
			for(int i=1; i<=metaData.getColumnCount(); i++){
				newFile.print(toOutput.getString(i) + "\t");
			}
			
			newFile.println();
		}
					
		newFile.close();
	}
	
	public static int count(){

		try{
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM Dictionary");
			int count = resultSet.getInt(1);
			return count;
	
		} catch(SQLException se){se.printStackTrace();return 0;}
	}
		
	public static void disconnect() throws Exception{
		
		statement.close();
		connection.close();	
	}
}
