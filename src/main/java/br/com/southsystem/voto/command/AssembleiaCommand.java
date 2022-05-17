package br.com.southsystem.voto.command;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.connection.db.BaseDAO;
import br.com.southsystem.voto.dao.AssembleiaDAOJdbc;
import br.com.southsystem.voto.dao.IAssembleiaDAO;
import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoDto;

public class AssembleiaCommand {

	private static Logger logger = Logger.getLogger(AssembleiaCommand.class);
	private static IAssembleiaDAO assembleiaDAO = new AssembleiaDAOJdbc();
	
	public static Integer getQtdDeVotos(boolean isFavor) throws Exception {
		Connection conn = BaseDAO.getConnection();
		Integer qtdDeVotos = 0;
		try {
			qtdDeVotos = assembleiaDAO.getQtdDeVotos(conn, isFavor);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return qtdDeVotos;
	}

	public static List<VotoDto> getListVotos() throws Exception {
		Connection conn = BaseDAO.getConnection();
		List<VotoDto> lstVotoDto = new ArrayList<VotoDto>();
		try {
			lstVotoDto = assembleiaDAO.getListVotos(conn);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return lstVotoDto;
	}
	
	public static PautaDto findOpenPautaById(int pautaId) throws Exception {
		Connection conn = BaseDAO.getConnection();
		PautaDto pautasDto = new PautaDto();
		try {
			pautasDto = assembleiaDAO.findOpenPautaById(conn, pautaId);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			BaseDAO.closeConnection(conn);
		}
		return pautasDto;
	}
}
