package br.com.southsystem.voto.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.command.AssembleiaCommand;
import br.com.southsystem.voto.config.KafkaProducerConfig;
import br.com.southsystem.voto.dto.AssembleiaConfigureDto;
import br.com.southsystem.voto.dto.VotoDto;

public class AssembleiaBusiness {

	private static Logger log = Logger.getLogger(AssembleiaBusiness.class);
	
	public static boolean configureAssembleia(AssembleiaConfigureDto configDto) throws Exception {
		try {
			if (AssembleiaCommand.findOpenPautaById(configDto.getPautaId()).getId() != 0) {;
				KafkaProducerConfig.openAssembleia(configDto);
				return true;
			}
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return false;
	}
	
	public static Integer getQtdDeVotos(boolean isFavor) throws Exception {
		Integer qtdDeVotos = 0;
		try {
			qtdDeVotos = AssembleiaCommand.getQtdDeVotos(isFavor);
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return qtdDeVotos;
	}
	
	public static List<VotoDto> getListVotos() throws Exception {
		List<VotoDto> lstVotoDto = new ArrayList<VotoDto>();
		try {
			lstVotoDto = AssembleiaCommand.getListVotos();
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return lstVotoDto;
	}
}
