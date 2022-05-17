package br.com.southsystem.voto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.bean.AssociadoBean;
import br.com.southsystem.voto.dao.sql.DeleteSql;
import br.com.southsystem.voto.dao.sql.InsertSql;
import br.com.southsystem.voto.dao.sql.SelectSql;
import br.com.southsystem.voto.dao.sql.UpdateSql;
import br.com.southsystem.voto.dto.AssociadoDto;

public class AssociadoDAOJdbc implements ICrudDAO<AssociadoBean, AssociadoDto> {

	private static Logger logger = Logger.getLogger(AssociadoDAOJdbc.class);
	
	@Override
	public int insert(Connection conn, AssociadoBean bean) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		try {
			ps = conn.prepareStatement(InsertSql.INSERT_ASSOCIADO, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, bean.getNome());
			ps.setString(2, bean.getCpf());
			ps.setString(3, bean.getEmail());
			ps.setObject(4, bean.getDthrCriacao());
			ps.setObject(5, bean.getDthrAtualizacao());
			ps.execute();
			
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return id;
	}

	@Override
	public AssociadoDto findById(Connection conn, int id) throws Exception {
		AssociadoDto associadoDto = new AssociadoDto();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_ASSOCIADO_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				associadoDto.setId(rs.getInt("associado_id"));
				associadoDto.setNome(rs.getString("associado_nome"));
				associadoDto.setCpf(rs.getString("associado_cpf"));
				associadoDto.setEmail(rs.getString("associado_email"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return associadoDto;
	}

	@Override
	public List<AssociadoDto> findAll(Connection conn) throws Exception {
		List<AssociadoDto> lstAssociadoDto = new ArrayList<AssociadoDto>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_ALL_ASSOCIADOS);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				AssociadoDto associadoDto = new AssociadoDto();
				associadoDto.setId(rs.getInt("associado_id"));
				associadoDto.setNome(rs.getString("associado_nome"));
				associadoDto.setCpf(rs.getString("associado_cpf"));
				associadoDto.setEmail(rs.getString("associado_email"));
				lstAssociadoDto.add(associadoDto);
				associadoDto = null;
			}
			rs.close();
			ps.close();	
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return lstAssociadoDto;
	}

	@Override
	public int update(Connection conn, AssociadoBean bean) throws Exception {
		PreparedStatement ps = null;
		int retorno = 0;
		try {
			ps = conn.prepareStatement(UpdateSql.UPDATE_ASSOCIADO);
			ps.setString(1, bean.getNome());
			ps.setString(2, bean.getCpf());
			ps.setString(3, bean.getEmail());
			ps.setObject(4, bean.getDthrAtualizacao());
			ps.setInt(5, bean.getId());
			
			retorno = ps.executeUpdate();
			ps.close();	
		}  catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return retorno;
	}

	@Override
	public int delete(Connection conn, int id) throws Exception {
		PreparedStatement ps = null;
		int retorno = 0;
		try {
			ps = conn.prepareStatement(DeleteSql.DELETE_ASSOCIADO);
			ps.setInt(1, id);
			retorno = ps.executeUpdate();
			ps.close();	
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return retorno;
	}

}
