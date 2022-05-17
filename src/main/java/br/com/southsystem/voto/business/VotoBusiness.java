package br.com.southsystem.voto.business;

import static br.com.southsystem.voto.commons.CommonUtils.verifyIsSecondVote;
import static br.com.southsystem.voto.commons.CommonUtils.kafkaSendMessage;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.adapter.VotoAdapter;
import br.com.southsystem.voto.bean.VotoBean;
import br.com.southsystem.voto.command.CrudCommand;
import br.com.southsystem.voto.config.KafkaProducerConfig;
import br.com.southsystem.voto.dao.VotoDAOJdbc;
import br.com.southsystem.voto.dto.VotoConsultDto;
import br.com.southsystem.voto.dto.VotoDto;

public class VotoBusiness {

	private static Logger log = Logger.getLogger(VotoBusiness.class);
	static CrudCommand<VotoBean, VotoConsultDto> command = new CrudCommand<VotoBean, VotoConsultDto>(new VotoDAOJdbc());
	
	public static int createVoto(VotoDto votoDto) throws Exception {
		VotoBean votoBean = new VotoBean();
		int id = 0;
		try {
			votoDto.setPautaId(KafkaProducerConfig.pautaId);
			if (!KafkaProducerConfig.isAssembleiaConfirured) return -1;
			
			if (verifyIsSecondVote(votoDto)) return -2;
			
			if (votoDto.getIsFavor().equals("Sim") || votoDto.getIsFavor().equals("Não")) {
				votoBean = VotoAdapter.votoAdapter(votoDto, 0, false);
				id = command.create(votoBean);
				votoDto.setId(id);
				kafkaSendMessage(votoDto);
			}
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return id;
	}
	
	public static VotoConsultDto findVotoById(int id) throws Exception {
		VotoConsultDto votoDto = new VotoConsultDto();
		try {
			votoDto = command.findById(id);
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return votoDto;
	}

	public static List<VotoConsultDto> findAllVotos() throws Exception {
		List<VotoConsultDto> votosDto = new ArrayList<VotoConsultDto>();
		try {
			votosDto = command.findAll();
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return votosDto;
	}
	
	public static int updateVoto(VotoDto votoDto, int id) throws Exception {
		VotoBean votoBean = new VotoBean();
		int retorno = 0;
		try {
			votoBean = VotoAdapter.votoAdapter(votoDto, id, true);
			retorno = command.update(votoBean);
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return retorno;
	}
	
	public static int deleteVoto(int id) throws Exception {
		int retorno = 0;
		try {
			retorno = command.delete(id);
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return retorno;
	}
	
}