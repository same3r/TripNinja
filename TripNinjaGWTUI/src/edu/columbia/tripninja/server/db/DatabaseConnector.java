package edu.columbia.tripninja.server.db;

import java.sql.*;

import edu.columbia.tripninja.shared.Constants;

public class DatabaseConnector {

	public static Connection getConn() {

		Connection conn = null;
		try {
			conn = dbConnect(Constants.DB_CONNECT_URL, Constants.DB_USER,
					Constants.DB_PASSWORD);
			System.out.println("connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection dbConnect(String path, String user, String password)
			throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(path, user, password);

		return conn;

	}
}
