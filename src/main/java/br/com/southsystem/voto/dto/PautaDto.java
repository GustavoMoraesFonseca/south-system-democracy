package br.com.southsystem.voto.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PautaDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden = true)
	private int id;
	
	private String descricao;
	
	@ApiModelProperty(hidden = true)
	private String resultado;

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private Integer qtdVotosPositivos;
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private Integer qtdVotosNegativos;

}
