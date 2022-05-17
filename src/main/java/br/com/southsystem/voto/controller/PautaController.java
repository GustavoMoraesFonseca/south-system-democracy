package br.com.southsystem.voto.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.southsystem.voto.business.PautaBusiness;
import br.com.southsystem.voto.commons.CommonUtils;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("pauta")
@Api(tags = "Pauta Métodos")
public class PautaController {
	
	@PostMapping
	@ApiOperation(value = "Cadastra uma Pauta")
    public ResponseEntity<ResponseDto> createPauta(@RequestHeader HttpHeaders headers, @RequestBody PautaDto pautaDto) {
    	LocalDateTime dtHrInicio = LocalDateTime.now();
		ResponseEntity<ResponseDto> response = null;
		ResponseDto body = new ResponseDto();
		int id = 0;
		try {
			id = PautaBusiness.createPauta(pautaDto);
			body.setData(id);
			response = ResponseEntity.status(HttpStatus.CREATED).body(body);
		} catch (SQLIntegrityConstraintViolationException e) {
			body.setError("Pauta já cadastrada.");
			response = ResponseEntity.status(HttpStatus.CONFLICT).body(body);
		} catch (Exception e) {
			body.setError(Constants.MESSAGE_ERROR_500);
			response = ResponseEntity.internalServerError().body(body);
		} finally {
			CommonUtils.registerTrace(headers, dtHrInicio, response.getStatusCodeValue());
		}
		return response;
	}
	
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Busca uma Pauta pelo seu ID")
	public ResponseEntity<ResponseDto> findPautaById(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
		LocalDateTime dtHrInicio = LocalDateTime.now();
    	ResponseDto body = new ResponseDto();
    	PautaDto pautaDto = new PautaDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			pautaDto = PautaBusiness.findPautaById(id);
			if (pautaDto.getId() == 0) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData(pautaDto);
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
	@ApiOperation(value = "Busca todas as Pautas cadastrados")
    public ResponseEntity<ResponseDto> findAllPautas(@RequestHeader HttpHeaders headers) {
    	LocalDateTime dtHrInicio = LocalDateTime.now();
    	List<PautaDto> pautaDto = new ArrayList<PautaDto>();
    	ResponseDto body = new ResponseDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			pautaDto = PautaBusiness.findAllPautas();
			if (pautaDto.isEmpty()) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData(pautaDto);
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
	@ApiOperation(value = "Exclui uma Pauta pelo seu ID")
    public ResponseEntity<ResponseDto> delete(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
		LocalDateTime dtHrInicio = LocalDateTime.now();
    	ResponseDto body = new ResponseDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			if (PautaBusiness.deletePauta(id) == 0) {
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
