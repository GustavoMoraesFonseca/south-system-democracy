package br.com.southsystem.voto.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.southsystem.voto.business.VotoBusiness;
import br.com.southsystem.voto.commons.CommonUtils;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.ResponseDto;
import br.com.southsystem.voto.dto.VotoConsultDto;
import br.com.southsystem.voto.dto.VotoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("voto")
@Api(tags = "Voto Métodos")
public class VotoController {
	
	@PostMapping
	@ApiOperation(value = "Vota em uma Pauta", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDto> createVoto(@RequestHeader HttpHeaders headers, @RequestBody VotoDto votoDto) {
    	LocalDateTime dtHrInicio = LocalDateTime.now();
		ResponseEntity<ResponseDto> response = null;
		ResponseDto body = new ResponseDto();
		int id = 0;
		try {
			id = VotoBusiness.createVoto(votoDto);
			switch (id) {
			case 0:
				body.setError("Os votos só poderão ser 'Sim' ou 'Não'");
				response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
				break;
			case -1:
				body.setError("A Assembleia ainda não foi configurada.");
				response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
				break;
			case -2:
				body.setError("Seu voto já foi cadastrado nessa Pauta.");
				response = ResponseEntity.status(HttpStatus.CONFLICT).body(body);
				break;
			default:
				body.setData(id);
				response = ResponseEntity.status(HttpStatus.CREATED).body(body);
				break;
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			body.setError("Associado não cadastrado.");
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
		} catch (Exception e) {
			body.setError(Constants.MESSAGE_ERROR_500);
			response = ResponseEntity.internalServerError().body(body);
		} finally {
			CommonUtils.registerTrace(headers, dtHrInicio, response.getStatusCodeValue());
		}
		return response;
	}
	
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Busca uma Voto pelo seu ID")
	public ResponseEntity<ResponseDto> findVotoById(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
		LocalDateTime dtHrInicio = LocalDateTime.now();
    	ResponseDto body = new ResponseDto();
    	VotoConsultDto votoDto = new VotoConsultDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			votoDto = VotoBusiness.findVotoById(id);
			if (votoDto.equals(new VotoConsultDto())) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData(votoDto);
				response = ResponseEntity.ok(body);
			}
		} catch (Exception e) {
			body.setError(Constants.MESSAGE_ERROR_500);
			response = ResponseEntity.internalServerError().body(body);
		} finally {
			CommonUtils.registerTrace(headers, dtHrInicio, response.getStatusCodeValue());
		}
		return response;
	}
	
	@GetMapping
	@ApiOperation(value = "Busca todos as Votos cadastrados")
    public ResponseEntity<ResponseDto> findAllVotos(@RequestHeader HttpHeaders headers) {
    	LocalDateTime dtHrInicio = LocalDateTime.now();
    	List<VotoConsultDto> votoDto = new ArrayList<VotoConsultDto>();
    	ResponseDto body = new ResponseDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			votoDto = VotoBusiness.findAllVotos();
			if (votoDto.isEmpty()) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData(votoDto);
				response = ResponseEntity.ok(body);
			}
		} catch (Exception e) {
			body.setError(Constants.MESSAGE_ERROR_500);
			response = ResponseEntity.internalServerError().body(body);
		} finally {
			CommonUtils.registerTrace(headers, dtHrInicio, response.getStatusCodeValue());
		}
		return response;
	}
	
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value = "Exclui uma Voto pelo seu ID")
    public ResponseEntity<ResponseDto> delete(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
		LocalDateTime dtHrInicio = LocalDateTime.now();
    	ResponseDto body = new ResponseDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			if (VotoBusiness.deleteVoto(id) == 0) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData("Exclusão feita com sucesso.");
				response = ResponseEntity.ok(body);
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
