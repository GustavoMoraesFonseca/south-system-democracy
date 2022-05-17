package br.com.southsystem.voto.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.adapter.PautaAdapter;
import br.com.southsystem.voto.bean.PautaBean;
import br.com.southsystem.voto.command.CrudCommand;
import br.com.southsystem.voto.dao.PautaDAOJdbc;
import br.com.southsystem.voto.dto.PautaDto;

public class PautaBusiness {

	private static Logger log = Logger.getLogger(PautaBusiness.class);
	static CrudCommand<PautaBean, PautaDto> command = new CrudCommand<PautaBean, PautaDto>(new PautaDAOJdbc());
	
	public static int createPauta(PautaDto pautaDto) throws Exception {
		PautaBean pautaBean = new PautaBean();
		int id = 0;
		try {
			pautaBean = PautaAdapter.pautaAdapter(pautaDto, 0);
			id = command.create(pautaBean);
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return id;
	}
	
	public static PautaDto findPautaById(int id) throws Exception {
		PautaDto pautaDto = new PautaDto();
		try {
			pautaDto = command.findById(id);
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return pautaDto;
	}

	public static List<PautaDto> findAllPautas() throws Exception {
		List<PautaDto> pautasDto = new ArrayList<PautaDto>();
		try {
			pautasDto = command.findAll();
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return pautasDto;
	}
	
	public static int updatePauta(PautaDto pautaDto, int id) throws Exception {
		PautaBean pautaBean = new PautaBean();
		int retorno = 0;
		try {
			pautaBean = PautaAdapter.pautaAdapter(pautaDto, id);
			retorno = command.update(pautaBean);
		} catch (Exception e) {
			log.error("Error: " + e);
			throw e;
		}
		return retorno;
	}
	
	public static int deletePauta(int id) throws Exception {
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
