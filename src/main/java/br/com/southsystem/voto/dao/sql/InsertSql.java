package br.com.southsystem.voto.dao.sql;

public interface InsertSql {

	public static final String INSERT_PAUTA
	= "INSERT INTO southsystem_votos.tab_pauta "
	+ 	"(pauta_descricao, pauta_qtd_votos_positivos, pauta_qtd_votos_negativos, pauta_is_close, dthr_criacao, dthr_atualizacao) "
	+ "VALUES "
	+ 	"(?,0,0,0,?,?)";
	
	public static final String INSERT_ASSOCIADO
	= "INSERT INTO southsystem_votos.tab_associado "
	+ 	"(associado_nome, associado_cpf, associado_email, dthr_criacao, dthr_atualizacao) "
	+ "VALUES "
	+   "(?,?,?,?,?)";
	
	public static final String INSERT_VOTO
	= "INSERT INTO southsystem_votos.tab_voto "
	+ "	(voto_pauta_id, voto_associado_id, voto_is_favor, voto_is_close, dthr_criacao, dthr_atualizacao) "
	+ "VALUES "
	+ "	(?,?,?,?,?,?)";
}
