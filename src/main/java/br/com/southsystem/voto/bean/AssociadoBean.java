package br.com.southsystem.voto.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssociadoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String nome;
	private String cpf;
	private String email;
	private String dthrCriacao;
	private String dthrAtualizacao;
}
