package br.com.southsystem.voto.dao;

import java.sql.Connection;
import java.util.List;

public interface ICrudDAO<Bean, Dto> {

	public int insert(Connection conn, Bean bean) throws Exception;
	public Dto findById(Connection conn, int id)  throws Exception;
	public List<Dto> findAll(Connection conn)     throws Exception;
	public int update(Connection conn, Bean bean) throws Exception;
	public int delete(Connection conn, int id)    throws Exception;
}
