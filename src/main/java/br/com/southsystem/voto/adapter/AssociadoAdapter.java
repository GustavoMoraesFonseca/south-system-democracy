package br.com.southsystem.voto.adapter;

import static br.com.southsystem.voto.commons.CommonUtils.removeNonNumericChars;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.bean.AssociadoBean;
import br.com.southsystem.voto.business.AssociadoBusiness;
import br.com.southsystem.voto.dto.AssociadoDto;

public class AssociadoAdapter {

	private static Logger log = Logger.getLogger(AssociadoBusiness.class);
	
	public static AssociadoBean associadoAdapter(AssociadoDto associadoDto, int id) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Timestamp(System.currentTimeMillis()));
		AssociadoBean associadoBean = new AssociadoBean();
		try {
			if (!associadoDto.equals(new AssociadoDto()) && associadoDto.hashCode() != new AssociadoDto().hashCode()) {
				associadoBean.setId(id);
				associadoBean.setNome(associadoDto.getNome());
				associadoBean.setCpf(removeNonNumericChars(associadoDto.getCpf()));
				associadoBean.setEmail(associadoDto.getEmail());
				associadoBean.setDthrCriacao(date);
				associadoBean.setDthrAtualizacao(date);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return associadoBean;
	}
}
