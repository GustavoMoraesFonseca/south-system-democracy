package br.com.southsystem.voto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.bean.VotoBean;
import br.com.southsystem.voto.dao.sql.DeleteSql;
import br.com.southsystem.voto.dao.sql.InsertSql;
import br.com.southsystem.voto.dao.sql.SelectSql;
import br.com.southsystem.voto.dao.sql.UpdateSql;
import br.com.southsystem.voto.dto.VotoConsultDto;

public class VotoDAOJdbc implements ICrudDAO<VotoBean, VotoConsultDto> {

	private static Logger logger = Logger.getLogger(VotoDAOJdbc.class);
	
	@Override
	public int insert(Connection conn, VotoBean bean) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = 0;
		try {
			ps = conn.prepareStatement(InsertSql.INSERT_VOTO, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, bean.getPautaId());
			ps.setInt(2, bean.getAssociadoId());
			ps.setBoolean(3, bean.isFavor());
			ps.setBoolean(4, bean.isClose());
			ps.setObject(5, bean.getDthrCriacao());
			ps.setObject(6, bean.getDthrAtualizacao());
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
	public VotoConsultDto findById(Connection conn, int id) throws Exception {
		VotoConsultDto votoDto = new VotoConsultDto();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_VOTO_BY_ID);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				votoDto.setId(rs.getInt("id"));
				votoDto.setAssociadoNome(rs.getString("nome"));
				votoDto.setAssociadoEmail(rs.getString("email"));
				votoDto.setPautaNome(rs.getString("pauta"));
				votoDto.setVotou(rs.getBoolean("votou")? "Sim":"Não");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return votoDto;
	}

	@Override
	public List<VotoConsultDto> findAll(Connection conn) throws Exception {
		List<VotoConsultDto> lstVotoConsultDto = new ArrayList<VotoConsultDto>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_ALL_VOTOS);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				VotoConsultDto votoDto = new VotoConsultDto();
				votoDto.setId(rs.getInt("id"));
				votoDto.setAssociadoNome(rs.getString("nome"));
				votoDto.setAssociadoEmail(rs.getString("email"));
				votoDto.setPautaNome(rs.getString("pauta"));
				votoDto.setVotou(rs.getBoolean("votou")? "Sim":"Não");
				lstVotoConsultDto.add(votoDto);
				votoDto = null;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return lstVotoConsultDto;
	}

	@Override
	public int update(Connection conn, VotoBean bean) throws Exception {
		PreparedStatement ps = null;
		int retorno = 0;
		try {
			ps = conn.prepareStatement(UpdateSql.UPDATE_VOTO);
			ps.setBoolean(1, bean.isClose());
			ps.setObject(2, bean.getDthrAtualizacao());
			ps.setInt(3, bean.getPautaId());
			
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
			ps = conn.prepareStatement(DeleteSql.DELETE_VOTO);
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
