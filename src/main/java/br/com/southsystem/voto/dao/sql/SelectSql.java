package br.com.southsystem.voto.dao.sql;

public interface SelectSql {

	public static final String SELECT_PAUTA_BY_ID
	= "SELECT tab_pauta.pauta_id, "
	+ "  tab_pauta.pauta_descricao, "
	+ "  tab_pauta.pauta_qtd_votos_positivos, "
	+ "  tab_pauta.pauta_qtd_votos_negativos "
	+ "FROM southsystem_votos.tab_pauta "
	+ "WHERE tab_pauta.pauta_id = ?";
	
	public static final String SELECT_ALL_PAUTAS
	= "SELECT tab_pauta.pauta_id, "
	+ "  tab_pauta.pauta_descricao, "
	+ "  tab_pauta.pauta_qtd_votos_positivos, "
	+ "  tab_pauta.pauta_qtd_votos_negativos "
	+ "FROM southsystem_votos.tab_pauta "
	+ "ORDER BY pauta_id";
	
	public static final String SELECT_OPEN_PAUTA_BY_ID
	= "SELECT tab_pauta.pauta_id, "
	+ "  tab_pauta.pauta_descricao "
	+ "FROM southsystem_votos.tab_pauta "
	+ "WHERE tab_pauta.pauta_is_close = 0 AND tab_pauta.pauta_id = ?";
	
	public static final String SELECT_ASSOCIADO_BY_ID
	= "SELECT tab_associado.associado_id, "
	+ "  tab_associado.associado_nome, "
	+ "  tab_associado.associado_cpf,"
	+ "  tab_associado.associado_email "
	+ "FROM southsystem_votos.tab_associado "
	+ "WHERE tab_associado.associado_id = ?";
	
	public static final String SELECT_ALL_ASSOCIADOS
	= "SELECT tab_associado.associado_id, "
	+ "  tab_associado.associado_nome, "
	+ "  tab_associado.associado_cpf,"
	+ "  tab_associado.associado_email "
	+ "FROM southsystem_votos.tab_associado "
	+ "ORDER BY associado_id";	
	
	public static final String SELECT_VOTO_BY_ID
	= "SELECT vt.voto_id as id, "
	+ "  ad.associado_nome as nome, "
	+ "  ad.associado_email as email, "
	+ "  pt.pauta_descricao as pauta, "
	+ "  vt.voto_is_favor as votou "
	+ "FROM southsystem_votos.tab_voto vt "
	+ "	 JOIN southsystem_votos.tab_pauta pt on pt.pauta_id = vt.voto_pauta_id "
	+ "	 JOIN southsystem_votos.tab_associado ad on ad.associado_id = vt.voto_associado_id "
	+ "WHERE vt.voto_id = ? AND vt.voto_is_close = 1";
	
	public static final String SELECT_ALL_VOTOS
	= "SELECT vt.voto_id as id, "
	+ "  ad.associado_nome as nome, "
	+ "  ad.associado_email as email, "
	+ "  pt.pauta_descricao as pauta, "
	+ "  vt.voto_is_favor as votou "
	+ "FROM southsystem_votos.tab_voto vt "
	+ "	 JOIN southsystem_votos.tab_pauta pt on pt.pauta_id = vt.voto_pauta_id "
	+ "	 JOIN southsystem_votos.tab_associado ad on ad.associado_id = vt.voto_associado_id "
	+ "WHERE vt.voto_is_close = 1 "
	+ "ORDER BY vt.voto_id";
	
	public static final String SELECT_VOTOS_VALORES
	= "SELECT count(DISTINCT tab_voto.voto_associado_id) as votos "
	+ "FROM southsystem_votos.tab_voto "
	+ "WHERE tab_voto.voto_is_favor = ?";
	
	public static final String SELECT_VOTOS
	= "SELECT tab_voto.voto_id, "
	+ "  tab_voto.voto_pauta_id, "
	+ "  tab_voto.voto_associado_id "
	+ "FROM southsystem_votos.tab_voto";
}
