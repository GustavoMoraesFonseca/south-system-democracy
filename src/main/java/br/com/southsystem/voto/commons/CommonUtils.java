package br.com.southsystem.voto.commons;

import static br.com.southsystem.voto.business.AssembleiaBusiness.getListVotos;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;

import br.com.southsystem.voto.config.KafkaProducerConfig;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.VotoDto;

public class CommonUtils {

	private static Logger logger = Logger.getLogger(CommonUtils.class);

	public static boolean verifyIsSecondVote(VotoDto votoDto) throws Exception {
		return getListVotos().stream()
				.filter(voto-> voto.getAssociadoId() == votoDto.getAssociadoId() 
								&& voto.getPautaId() == votoDto.getPautaId())
				.findFirst().isPresent();
	}
	
	public static void kafkaSendMessage(VotoDto votoDto) {
		KafkaProducerConfig producer = new KafkaProducerConfig();
		producer.kafkaTemplate().send(Constants.TOPIC, votoDto);
		System.out.println("Produziu: "+ votoDto.toString());
		System.out.println("Em: "+ Constants.BOOSTRAP_SERVERS + " no tópico: "+ Constants.TOPIC);
	}
	
	public static String getVotesResult(int qtdVotosPossitivos, int qtdVotosNegativos) {
		if (qtdVotosPossitivos == 0 && qtdVotosNegativos == 0) {
			return "Ainda não houve votos.";
		} else if (qtdVotosPossitivos == qtdVotosNegativos) {
			return "EMPATE"; 
		}
		return qtdVotosPossitivos > qtdVotosNegativos? "Maioria optou por SIM" : "Maioria optou por NÃO";
	}
	
	public static String removeNonNumericChars(String str) {
		return str.replaceAll("[^0-9]", "");
	}
	
	public static void registerTrace(HttpHeaders header, LocalDateTime dtHrInicio, int status) {
		LocalDateTime dtHrFinal = LocalDateTime.now();
		long duracao = Duration.between(dtHrInicio, dtHrFinal).getNano();
		long duracaoEmMilissegundos = TimeUnit.MILLISECONDS.convert(duracao, TimeUnit.NANOSECONDS);
		logger.info(String.format("Duration in ms: %s | Host: %s | Location: %s | Status: %s", 
					  		   	  duracaoEmMilissegundos,
					  		   	  header.getHost() == null? "null":header.getHost().getHostName(),
					  		   	  header.getLocation(),
					  		   	  status)
		);
	}
}
