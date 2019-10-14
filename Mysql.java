import java.sql.*;

class Mysql{ 
	public ResultSet query(String query){
		try{  
            //here warehouse is dat abase name, root is username and password  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/warehouse",
				"root",
				""
			);  
			Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("Success!");
            return rs;
		}catch(Exception e){ 
            System.out.println(e);
            return null;
		}  
	}
	public void update(String update){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
            //here warehouse is database name, root is username and password  
			Connection con=DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/warehouse",
				"root",
				""
			);  
			Statement stmt = con.createStatement();  
            stmt.executeUpdate(update);
            con.close();
            System.out.println("Success!");
		}catch(Exception e){
			System.out.println(e);
		}
	} 
}