import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class SQLConnect{
		
	public static void connect() throws Exception{
		
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection con = DriverManager.getConnection
				("jdbc:mysql://localhost","root","root");
		
		PreparedStatement statement = con.prepareStatement
				("select * from pet");
		
		ResultSet result = statement.executeQuery();
		
		while(result.next()){
			
			System.out.println("Name\tOwner\tSpecies\tSex");
			System.out.println(result.getString(1) + "\t" + 
					result.getString(2) + "\t" + 
					result.getString(3) + "\t" + 
					result.getString(4));
		}
	}	
}
