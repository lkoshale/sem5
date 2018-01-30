import java.sql.*;  
import java.io.*;
import java.util.*;


class MysqlCon{  

	public static void main(String args[]){  

			String str = null;

			Scanner sc = new Scanner(System.in);
			str = sc.nextLine();

			//System.out.println(str);

			try{  

				Class.forName("com.mysql.jdbc.Driver");  
				
				Connection con=DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/Railway","root","lk");  
				

				Statement stmt=con.createStatement();  
				
				ResultSet rs=stmt.executeQuery(str); 

				ResultSetMetaData rsmd = rs.getMetaData();

				int colmnCount = rsmd.getColumnCount();
				
				//System.out.println(colmnCount);
				 
				while(rs.next())  {

					for(int i=1;i<=colmnCount;i++){
						
						byte[] by =  rs.getBytes(i);
						String st = new String(by);
						System.out.print(st+"  ");
					}

					System.out.println();

				//System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

				}
				con.close();  

			}catch(Exception e)
			{
			 System.out.println(e);
			}  
	}  
}
