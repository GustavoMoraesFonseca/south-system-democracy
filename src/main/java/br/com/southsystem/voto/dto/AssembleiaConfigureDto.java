package br.com.southsystem.voto.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssembleiaConfigureDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer durationInMs;
	private int pautaId;
	
}
