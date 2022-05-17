package br.com.southsystem.voto.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AssociadoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden = true)
	private int id;
	private String nome;
	private String cpf;
	private String email;
}
