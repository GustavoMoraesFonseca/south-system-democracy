package br.com.southsystem.voto.connection.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceLocator {

	private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);

	public static Connection getConnection(String DB_URL, String DB_USER_NAME, String DB_PASSWORD) {
		Connection conn = null;
		try {

			Properties props = new Properties();
			props.put("user", DB_USER_NAME);
			props.put("password", DB_PASSWORD);
			conn = DriverManager.getConnection(DB_URL, props);

			if (conn != null) {
				conn.setAutoCommit(false);
				logger.debug("Connect realized successfully");
			}
		} catch (Exception e) {
			logger.error("Error to connect using DriverManager: ", e);
		}
		return conn;
	}

}
