package br.com.southsystem.voto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.dao.sql.SelectSql;
import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoDto;

public class AssembleiaDAOJdbc implements IAssembleiaDAO {

	private static Logger logger = Logger.getLogger(AssembleiaDAOJdbc.class);

	@Override
	public Integer getQtdDeVotos(Connection conn, boolean isFavor) throws Exception {
		Integer qtdDeVotos = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_VOTOS_VALORES);
			ps.setBoolean(1, isFavor);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				qtdDeVotos = rs.getInt("votos");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return qtdDeVotos;
	}

	@Override
	public List<VotoDto> getListVotos(Connection conn) throws Exception {
		List<VotoDto> lstVotoDto = new ArrayList<VotoDto>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_VOTOS);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				VotoDto votoDto = new VotoDto();
				votoDto.setId(rs.getInt("voto_id"));
				votoDto.setPautaId(rs.getInt("voto_pauta_id"));
				votoDto.setAssociadoId(rs.getInt("voto_associado_id"));
				lstVotoDto.add(votoDto);
				votoDto = null;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return lstVotoDto;
	}
	
	@Override
	public PautaDto findOpenPautaById(Connection conn, int pautaId) throws Exception {
		PautaDto pautaDto = new PautaDto();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 
			ps = conn.prepareStatement(SelectSql.SELECT_OPEN_PAUTA_BY_ID);
			ps.setInt(1, pautaId);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				pautaDto.setId(rs.getInt("pauta_id"));
				pautaDto.setDescricao(rs.getString("pauta_descricao"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return pautaDto;
	}
}
