package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
	private static final String url = "jdbc:mysql://localhost:3306/food_management";
	private static final String user = "user_account";
	private static final String pass = "pass";
	
	public static Connection getConnection()  throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(url, user, pass);
	}
}
