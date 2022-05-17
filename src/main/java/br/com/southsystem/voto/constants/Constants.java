package br.com.southsystem.voto.constants;

public interface Constants {

	//Kafka
	public static final String BOOSTRAP_SERVERS = System.getenv("BOOSTRAP_SERVER");
	public static final String TOPIC = "tp-votos";
	public static final String GROUP_ID = "group-votos";
	
	//DataBase
	public static final String IP_DB = System.getenv("MYSQL_IP");
	public static final String USER_DB = System.getenv("MYSQL_USER");
	public static final String PASSWORD_DB = System.getenv("MYSQL_PASSWORD");
	
	//Rest Responses
	public static final String MESSAGE_ERROR_404 = "Nenhum registro encontrado.";
	public static final String MESSAGE_ERROR_500 = "Erro interno do servidor.";
}
