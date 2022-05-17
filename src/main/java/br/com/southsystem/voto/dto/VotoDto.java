package br.com.southsystem.voto.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString(includeFieldNames = true)
public class VotoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden = true)
	private int id;
	
	@ApiModelProperty(hidden = true)
	private int pautaId;
	private int associadoId;
	private String isFavor;
}
