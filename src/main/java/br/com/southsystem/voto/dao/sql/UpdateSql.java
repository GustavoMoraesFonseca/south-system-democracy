package br.com.southsystem.voto.dao.sql;

public interface UpdateSql {

	public static final String UPDATE_PAUTA
	= "UPDATE southsystem_votos.tab_pauta SET "
	+ 	"pauta_qtd_votos_positivos = ?, "
	+ 	"pauta_qtd_votos_negativos = ?, "
	+   "pauta_is_close = 1, "
	+ 	"dthr_atualizacao = ? "
	+ "WHERE pauta_id = ?";
	
	public static final String UPDATE_ASSOCIADO
	= "UPDATE southsystem_votos.tab_associado SET "
	+ 	"associado_nome = ?, "
	+ 	"associado_cpf = ?, "
	+   "associado_email = ?, "
	+ 	"dthr_atualizacao = ? "
	+ "WHERE associado_id = ?";
	
	public static final String UPDATE_VOTO
	= "UPDATE southsystem_votos.tab_voto SET "
	+ "	voto_is_close = ?, "
	+ "	dthr_atualizacao = ? "
	+ "WHERE voto_pauta_id = ?";
}
