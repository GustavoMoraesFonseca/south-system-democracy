package br.com.southsystem.voto.bean;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter(value = AccessLevel.NONE)
	private int id;
	private int pautaId;
	private int associadoId;
	private boolean isFavor;
	private boolean isClose;
	private String dthrCriacao;
	private String dthrAtualizacao;
}
