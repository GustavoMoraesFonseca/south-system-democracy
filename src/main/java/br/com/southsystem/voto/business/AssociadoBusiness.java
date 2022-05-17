package br.com.southsystem.voto.business;

import static br.com.southsystem.voto.client.AssiciadoClient.requestValidateCpfWeb;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.adapter.AssociadoAdapter;
import br.com.southsystem.voto.bean.AssociadoBean;
import br.com.southsystem.voto.command.CrudCommand;
import br.com.southsystem.voto.dao.AssociadoDAOJdbc;
import br.com.southsystem.voto.dto.AssociadoDto;

public class AssociadoBusiness {

	private static Logger log = Logger.getLogger(AssociadoBusiness.class);
	static CrudCommand<AssociadoBean, AssociadoDto> command = new CrudCommand<AssociadoBean, AssociadoDto>(new AssociadoDAOJdbc());
	
	public static int createAssociado(AssociadoDto associadoDto) throws Exception {
		AssociadoBean associadoBean = new AssociadoBean();
		int id = 0;
		try {
			associadoBean = AssociadoAdapter.associadoAdapter(associadoDto, 0);
			if (requestValidateCpfWeb(associadoBean.getCpf())) {
				id = command.create(associadoBean);
			}
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return id;
	}
	
	public static AssociadoDto findAssociadoById(int id) throws Exception {
		AssociadoDto associadoDto = new AssociadoDto();
		try {
			associadoDto = command.findById(id);
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return associadoDto;
	}

	public static List<AssociadoDto> findAllAssociados() throws Exception {
		List<AssociadoDto> associadosDto = new ArrayList<AssociadoDto>();
		try {
			associadosDto = command.findAll();
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return associadosDto;
	}
	
	public static int updateAssociado(AssociadoDto associadoDto, int id) throws Exception {
		AssociadoBean associadoBean = new AssociadoBean();
		int retorno = 0;
		try {
			associadoBean = AssociadoAdapter.associadoAdapter(associadoDto, id);
			if (requestValidateCpfWeb(associadoBean.getCpf())) {
				retorno = command.update(associadoBean);
			}
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return retorno;
	}
	
	public static int deleteAssociado(int id) throws Exception {
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
