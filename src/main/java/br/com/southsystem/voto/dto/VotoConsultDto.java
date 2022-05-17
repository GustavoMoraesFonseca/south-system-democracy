package br.com.southsystem.voto.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class VotoConsultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
    private String associadoNome;
    private String associadoEmail;
    private String pautaNome;
    private String votou;
}
