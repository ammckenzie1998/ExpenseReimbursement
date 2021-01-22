package com.revature.ers.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	private static final String URL = System.getenv("ERS_DB_URL");
	private static final String USERNAME = System.getenv("ERS_USERNAME");
	private static final String PASSWORD = System.getenv("ERS_PASSWORD");
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
