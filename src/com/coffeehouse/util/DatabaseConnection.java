package com.coffeehouse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
	
	private DatabaseConnection() {}

	public static Connection getConnection() throws SQLException {
		String url = EnvConfig.get("db.url");
		String user = EnvConfig.get("db.user");
		String password = EnvConfig.get("db.password");

		return DriverManager.getConnection(url, user, password);
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.error("Failed to close database connection", e);
			}
		}
	}

	public static void testConnection() {
		try (var connection = getConnection()) {
			Logger.info("Database connection successful");
		} catch (SQLException e) {
			Logger.error("Failed to connect to the database", e);
		}
	}
}