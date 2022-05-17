package br.com.southsystem.voto.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Object data;
	private String error;

}
