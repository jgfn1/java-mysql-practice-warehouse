import java.sql.*;  
class mysql{ 
	public static Object query(String query){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/warehouse",
				"root",
				""
			);  
			//here warehouse is database name, root is username and password  
			Statement stmt = con.createStatement();  
			stmt.executeUpdate("insert into clients values ('Max', 'WA', '2020202020', null, 300);"); 
			ResultSet rs = stmt.executeQuery("select * from clients;"); 
			while(rs.next()){
				System.out.println(
					rs.getString("name")+
					"  "+
					rs.getString("address")+
					"  "+
					rs.getString("phoneNumber")+
					"  "+
					rs.getInt("clientID")+
					"  "+
					rs.getDouble("balance")
				);
			}
			con.close();
			return 1;
		}catch(Exception e){ 
			System.out.println(e);
			return -1;
		}  
	}
	public static 
}