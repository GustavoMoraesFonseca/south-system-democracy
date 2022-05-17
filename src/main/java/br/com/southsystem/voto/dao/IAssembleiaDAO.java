package br.com.southsystem.voto.dao;

import java.sql.Connection;
import java.util.List;

import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoDto;

public interface IAssembleiaDAO {

	public Integer getQtdDeVotos(Connection conn, boolean isFavor) throws Exception;
	public List<VotoDto> getListVotos(Connection conn) throws Exception;
	public PautaDto findOpenPautaById(Connection conn, int pautaId) throws Exception;
}
