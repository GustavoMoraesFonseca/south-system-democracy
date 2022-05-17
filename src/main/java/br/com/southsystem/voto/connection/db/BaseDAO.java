package br.com.southsystem.voto.connection.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.southsystem.voto.constants.Constants;

public class BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(BaseDAO.class);
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			String url = "jdbc:mysql://"+Constants.IP_DB+":3307/southsystem_votos?allowPublicKeyRetrieval=true&useTimezone=true&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull";

			conn = ServiceLocator.getConnection(url, Constants.USER_DB, Constants.PASSWORD_DB);
		} catch (Exception e) {
			logger.error("Error: " + e);
		}
		return conn;
	}

	// Commit
	public static void commitTransaction(Connection conn) throws SQLException {
		if (conn != null) {
			conn.commit();
		}
	}

	// Rollback
	public static void rollbackTransaction(Connection conn) {
		try {
			if (conn != null) {
				conn.rollback();
				// conn.close();
			}
		} catch (SQLException sqle) {
			logger.error("Error: " + sqle);
		}
	}

	//Close Connection
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			logger.error("Erro ao fechar Connection. ", e);
		}
	}
}
