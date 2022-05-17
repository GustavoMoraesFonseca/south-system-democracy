package br.com.southsystem.voto.dao.sql;

public interface DeleteSql {

	public static final String DELETE_PAUTA
	= "DELETE FROM southsystem_votos.tab_pauta "
	+ "WHERE pauta_id = ?";
	
	public static final String DELETE_ASSOCIADO
	= "DELETE FROM southsystem_votos.tab_associado "
	+ "WHERE associado_id = ?";
	
	public static final String DELETE_VOTO
	= "DELETE FROM southsystem_votos.tab_voto "
	+ "WHERE voto_id = ?";
}
