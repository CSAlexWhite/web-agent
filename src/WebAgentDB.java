import java.sql.*;

public abstract class WebAgentDB{
  
    static Connection connection = null;
   	static Statement statement = null;
   	static boolean exists = false;
   	
	public static void connect() throws SQLException, ClassNotFoundException{
		
		Class.forName("com.mysql.jdbc.Driver");		
		try{connection = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "root");} 
		catch (SQLException se){System.out.println("Could not connect to database");}
		statement = connection.createStatement();	
		
		try{instruct( "CREATE DATABASE WEBAGENT");}
		catch(SQLException se){
			instruct("USE WEBAGENT");
		}
		
		try{instruct("USE WEBAGENT");}
		catch(SQLException se){
			instruct("CREATE DATABASE WEBAGENT");
			instruct("USE WEBAGENT");
		}
		
		try{	instruct( "CREATE TABLE CONVERSATIONS"
				+ "(id INTEGER not NULL AUTO_INCREMENT,"
				+ "responseid INTEGER NOT NULL,"
				+ "user BOOLEAN,"
				+ "previous INTEGER,"
				+ "PRIMARY KEY (id))");	
			} catch(SQLException se)
		{System.out.println("Table CONVERSATIONS exists");}
				
		
		try{	instruct( 
				"CREATE TABLE DICTIONARY " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " length INTEGER not NULL, " + 
                " content VARCHAR(8192) NOT NULL, " + 
                " PRIMARY KEY ( id ))"); 
		} catch(SQLException se)
		{
			System.out.println("Table CONVERSATIONS exists");
			instruct("TRUNCATE TABLE DICTIONARY");
			}
	}
	
	public static void instruct(String inputStatement) throws SQLException{
		 
		String sql = inputStatement;
		statement = connection.createStatement();
		try{statement.executeUpdate(sql);} 
		catch(SQLException se){ /*se.printStackTrace();*/}		
	}
	
	public static void addToConvoDB(Integer id, Boolean user, Integer previousID){
		
		try{
			statement.executeUpdate( 
				"INSERT INTO Conversations (responseid, user, previous)"
				+ " VALUE ("
				+ id.toString()			+ "," 
				+ user.toString() 		+ ","
				+ previousID.toString() + ")"
				);
		}catch (SQLException se){se.printStackTrace();}
				
	}
	
	static int errorcount = 0;
	public static void addToDictDB(Integer length, String content){
		
		
		
		try{
			errorcount++;
			statement.executeUpdate( 
					"INSERT INTO `dictionary`(length, content) VALUE ('"+length.toString()+"','"+content+"');");
				//"INSERT INTO Dictionary (length, content) VALUES (" + length.toString()	+ "," + content + ")");
//				"INSERT INTO Dictionary (length, content) VALUE (" + length.toString() + "," + content + ")");//1, 2)");
//			('"+pid+"','"+tid+"','"+rid+"',"+tspent+",'"+des+"');"
		} catch (SQLException se){System.out.println(errorcount);se.printStackTrace();}
				
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
