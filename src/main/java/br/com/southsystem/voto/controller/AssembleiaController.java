package br.com.southsystem.voto.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.southsystem.voto.business.AssembleiaBusiness;
import br.com.southsystem.voto.commons.CommonUtils;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.AssembleiaConfigureDto;
import br.com.southsystem.voto.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("assembleia")
@Api(tags = "Assembleia Métodos")
public class AssembleiaController {

	@PostMapping
	@ApiOperation(
		value = "Configura uma Assempleia", 
		notes = "Se quiser usar a duração da votação default(1 min), declare 'durationInMs' como null"
	)
    public ResponseEntity<ResponseDto> configurePauta(@RequestHeader HttpHeaders headers, 
    												  @RequestBody AssembleiaConfigureDto configDto) {
    	LocalDateTime dtHrInicio = LocalDateTime.now();
		ResponseEntity<ResponseDto> response = null;
		ResponseDto body = new ResponseDto();
		try {
			if (AssembleiaBusiness.configureAssembleia(configDto)) {
				body.setData("Assembleia aberta com sucesso! A contagem começara após o primeiro voto.");
				response = ResponseEntity.ok(body);
			} else {
				body.setError("Pauta não encontrada ou Encerrada.");
				response = ResponseEntity.badRequest().body(body);
			}
		} catch (Exception e) {
			body.setError(Constants.MESSAGE_ERROR_500);
			response = ResponseEntity.internalServerError().body(body);
		} finally {
			CommonUtils.registerTrace(headers, dtHrInicio, response.getStatusCodeValue());
		}
		return response;
	}
}
