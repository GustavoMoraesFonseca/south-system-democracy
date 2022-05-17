package br.com.southsystem.voto.dao;

import static br.com.southsystem.voto.commons.CommonUtils.getVotesResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.bean.PautaBean;
import br.com.southsystem.voto.dao.sql.DeleteSql;
import br.com.southsystem.voto.dao.sql.InsertSql;
import br.com.southsystem.voto.dao.sql.SelectSql;
import br.com.southsystem.voto.dao.sql.UpdateSql;
import br.com.southsystem.voto.dto.PautaDto;

public class PautaDAOJdbc implements ICrudDAO<PautaBean, PautaDto> {

	private static Logger logger = Logger.getLogger(PautaDAOJdbc.class);
	
	@Override
	public int insert(Connection conn, PautaBean bean) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		try {
			ps = conn.prepareStatement(InsertSql.INSERT_PAUTA, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, bean.getDescricao());
			ps.setObject(2, bean.getDthrCriacao());
			ps.setObject(3, bean.getDthrAtualizacao());
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
	public PautaDto findById(Connection conn, int id) throws Exception {
		PautaDto pautaDto = new PautaDto();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_PAUTA_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				pautaDto.setId(rs.getInt("pauta_id"));
				pautaDto.setDescricao(rs.getString("pauta_descricao"));
				pautaDto.setQtdVotosNegativos(rs.getInt("pauta_qtd_votos_negativos"));
				pautaDto.setQtdVotosPositivos(rs.getInt("pauta_qtd_votos_positivos"));
				pautaDto.setResultado(
						getVotesResult(rs.getInt("pauta_qtd_votos_positivos"), 
									   rs.getInt("pauta_qtd_votos_negativos"))
				);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return pautaDto;
	}

	@Override
	public List<PautaDto> findAll(Connection conn) throws Exception {
		List<PautaDto> lstPautaDto = new ArrayList<PautaDto>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_ALL_PAUTAS);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				PautaDto pautaDto = new PautaDto();
				pautaDto.setId(rs.getInt("pauta_id"));
				pautaDto.setDescricao(rs.getString("pauta_descricao"));
				pautaDto.setQtdVotosNegativos(rs.getInt("pauta_qtd_votos_negativos"));
				pautaDto.setQtdVotosPositivos(rs.getInt("pauta_qtd_votos_positivos"));
				pautaDto.setResultado(
						getVotesResult(rs.getInt("pauta_qtd_votos_positivos"), 
									   rs.getInt("pauta_qtd_votos_negativos"))
				);
				lstPautaDto.add(pautaDto);
				pautaDto = null;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return lstPautaDto;
	}

	@Override
	public int update(Connection conn, PautaBean bean) throws Exception {
		PreparedStatement ps = null;
		int retorno = 0;
		try {
			ps = conn.prepareStatement(UpdateSql.UPDATE_PAUTA);
			ps.setLong(1, bean.getQtdVotosPositivos());
			ps.setLong(2, bean.getQtdVotosNegativos());
			ps.setObject(3, bean.getDthrAtualizacao());
			ps.setInt(4, bean.getId());
			
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
			ps = conn.prepareStatement(DeleteSql.DELETE_PAUTA);
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
