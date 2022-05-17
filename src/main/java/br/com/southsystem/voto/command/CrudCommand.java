package br.com.southsystem.voto.command;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.connection.db.BaseDAO;
import br.com.southsystem.voto.dao.ICrudDAO;

public class CrudCommand<Bean, Dto> {
	
	private static Logger logger = Logger.getLogger(CrudCommand.class);
	private ICrudDAO<Bean, Dto> crudDAO;
	
	public CrudCommand(ICrudDAO<Bean, Dto> crudDAOImpl) {
		this.crudDAO = crudDAOImpl;
	}
	
	public int create(Bean bean) throws Exception {
		Connection conn = BaseDAO.getConnection();
		int id = 0;
		try {
			id = crudDAO.insert(conn, bean);
			BaseDAO.commitTransaction(conn);
		} catch (Exception e) {
			BaseDAO.rollbackTransaction(conn);
			logger.error(e.getMessage());
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return id;
	}

	public Dto findById(int id) throws Exception {
		Connection conn = BaseDAO.getConnection();
		Dto dto;
		try {
			dto = crudDAO.findById(conn, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return dto;
	}	
	
	public List<Dto> findAll() throws Exception {
		Connection conn = BaseDAO.getConnection();
		List<Dto> list = new ArrayList<Dto>();
		try {
			list = crudDAO.findAll(conn);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return list;
	}

	public int update(Bean bean) throws Exception {
		Connection conn = BaseDAO.getConnection();
		int retorno = 0;
		try {
			retorno = crudDAO.update(conn, bean);
			BaseDAO.commitTransaction(conn);
		} catch (Exception e) {
			BaseDAO.rollbackTransaction(conn);
			logger.error(e.getMessage());
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return retorno;
	}

	public int delete(int id) throws Exception {
		Connection conn = BaseDAO.getConnection();
		int retorno = 0;
		try {
			retorno = crudDAO.delete(conn, id);
			BaseDAO.commitTransaction(conn);
		} catch (Exception e) {
			BaseDAO.rollbackTransaction(conn);
			logger.error(e.getMessage());
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return retorno;
	}
	
}
