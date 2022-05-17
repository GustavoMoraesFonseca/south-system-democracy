package br.com.southsystem.voto.adapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.bean.VotoBean;
import br.com.southsystem.voto.business.PautaBusiness;
import br.com.southsystem.voto.dto.VotoDto;

public class VotoAdapter {

	private static Logger log = Logger.getLogger(PautaBusiness.class);
	
	public static VotoBean votoAdapter(VotoDto votoDto, int id, boolean isClose) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Timestamp(System.currentTimeMillis()));
		VotoBean votoBean = new VotoBean();
		try {
			if (!votoDto.equals(new VotoDto()) && votoDto.hashCode() != new VotoDto().hashCode()) {
				votoBean.setId(id);
				votoBean.setPautaId(votoDto.getPautaId());
				votoBean.setAssociadoId(votoDto.getAssociadoId());
				votoBean.setFavor(votoDto.getIsFavor().equals("Sim")? true:false);
				votoBean.setClose(isClose);
				votoBean.setDthrCriacao(date);
				votoBean.setDthrAtualizacao(date);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return votoBean;
	}
}
