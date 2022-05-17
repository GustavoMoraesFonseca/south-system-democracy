package br.com.southsystem.voto.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PautaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String descricao;
	private Integer qtdVotosPositivos;
	private Integer qtdVotosNegativos;
	private String dthrCriacao;
	private String dthrAtualizacao;
	
}
