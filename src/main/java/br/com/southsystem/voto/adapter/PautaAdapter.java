package br.com.southsystem.voto.adapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.bean.PautaBean;
import br.com.southsystem.voto.business.PautaBusiness;
import br.com.southsystem.voto.dto.PautaDto;

public class PautaAdapter {

	private static Logger log = Logger.getLogger(PautaBusiness.class);
	
	public static PautaBean pautaAdapter(PautaDto pautaDto, int id) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Timestamp(System.currentTimeMillis()));
		PautaBean pautaBean = new PautaBean();
		try {
			if (!pautaDto.equals(new PautaDto()) && pautaDto.hashCode() != new PautaDto().hashCode()) {
				pautaBean.setId(id);
				pautaBean.setQtdVotosPositivos(pautaDto.getQtdVotosPositivos());
				pautaBean.setQtdVotosNegativos(pautaDto.getQtdVotosNegativos());
				pautaBean.setDescricao(pautaDto.getDescricao());
				pautaBean.setDthrCriacao(date);
				pautaBean.setDthrAtualizacao(date);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return pautaBean;
	}
}
