package br.com.southsystem.voto.consumer;

import org.apache.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.southsystem.voto.business.AssembleiaBusiness;
import br.com.southsystem.voto.business.PautaBusiness;
import br.com.southsystem.voto.business.VotoBusiness;
import br.com.southsystem.voto.config.KafkaProducerConfig;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoConsultDto;
import br.com.southsystem.voto.dto.VotoDto;
import br.com.southsystem.voto.sender.EmailSender;

@Component
public class KafkaConsumer {

	private static Logger log = Logger.getLogger(KafkaConsumer.class);
	
	@KafkaListener(topics = Constants.TOPIC, groupId = Constants.GROUP_ID)
	public boolean listener(String json) {
		System.out.println("Consumiu: "+json);
		ObjectMapper objectMapper = new ObjectMapper();
		VotoConsultDto votoConsultDto = new VotoConsultDto();
		PautaDto pautaDto = new PautaDto();
		VotoDto votoDto = new VotoDto();
		try {
			votoDto = objectMapper.readValue(json, VotoDto.class);
			pautaDto = PautaBusiness.findPautaById(votoDto.getPautaId());
			
			if (pautaDto.getResultado().equals("Ainda não houve votos.")) {
				VotoBusiness.updateVoto(votoDto, votoDto.getId());
				KafkaProducerConfig.closeAssembleia();
				pautaDto.setQtdVotosNegativos(AssembleiaBusiness.getQtdDeVotos(false));
				pautaDto.setQtdVotosPositivos(AssembleiaBusiness.getQtdDeVotos(true));
				PautaBusiness.updatePauta(pautaDto, pautaDto.getId());
				pautaDto = PautaBusiness.findPautaById(pautaDto.getId());
			}
			
			votoConsultDto = VotoBusiness.findVotoById(votoDto.getId());
			EmailSender.emailSender(votoConsultDto, pautaDto);
			log.info("Email enviado para: "+ votoConsultDto.getAssociadoEmail());
		} catch (Exception e) {
			log.error(e);
			return false;
		}
		return true;
	}
}
