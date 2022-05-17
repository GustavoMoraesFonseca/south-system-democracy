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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.southsystem.voto.business.AssociadoBusiness;
import br.com.southsystem.voto.commons.CommonUtils;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.AssociadoDto;
import br.com.southsystem.voto.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("associado")
@Api(tags = "Associado Métodos")
public class AssociadoController {
	
	@PostMapping
	@ApiOperation(value = "Cadastra um Associado")
    public ResponseEntity<ResponseDto> createAssociado(@RequestHeader HttpHeaders headers, @RequestBody AssociadoDto associadoDto) {
    	LocalDateTime dtHrInicio = LocalDateTime.now();
		ResponseEntity<ResponseDto> response = null;
		ResponseDto body = new ResponseDto();
		int id = 0;
		try {
			id = AssociadoBusiness.createAssociado(associadoDto);
			if (id == 0) {
				body.setError("O CPF informado é inválido.");
				response = ResponseEntity.badRequest().body(body);
			} else {
				body.setData("Seu ID de Associado é: "+id);
				response = ResponseEntity.status(HttpStatus.CREATED).body(body);
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			body.setError("Associado já cadastrado.");
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
	@ApiOperation(value = "Busca um Associado pelo seu ID")
	public ResponseEntity<ResponseDto> findAssociadoById(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
		LocalDateTime dtHrInicio = LocalDateTime.now();
    	ResponseDto body = new ResponseDto();
    	AssociadoDto associadoDto = new AssociadoDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			associadoDto = AssociadoBusiness.findAssociadoById(id);
			if (associadoDto.getId() == 0) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData(associadoDto);
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
	@ApiOperation(value = "Busca todos os Associados cadastrados")
    public ResponseEntity<ResponseDto> findAllAssociados(@RequestHeader HttpHeaders headers) {
    	LocalDateTime dtHrInicio = LocalDateTime.now();
    	List<AssociadoDto> associadoDto = new ArrayList<AssociadoDto>();
    	ResponseDto body = new ResponseDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			associadoDto = AssociadoBusiness.findAllAssociados();
			if (associadoDto.isEmpty()) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData(associadoDto);
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
	
	@PutMapping(value = "/{id}")
	@ApiOperation(value = "Altera uma Associado cadastrado")
	public ResponseEntity<ResponseDto> updateAssociado(@RequestHeader HttpHeaders headers,
													   @PathVariable("id") int id,
												   	   @RequestBody AssociadoDto associadoDto) {
		LocalDateTime dtHrInicio = LocalDateTime.now();
		ResponseDto body = new ResponseDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			if (AssociadoBusiness.updateAssociado(associadoDto, id) == 0) {
				body.setError(Constants.MESSAGE_ERROR_404);
				response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
			} else {
				body.setData("Alteração feita com sucesso.");
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
	@ApiOperation(value = "Exclui um Associado pelo seu ID")
    public ResponseEntity<ResponseDto> delete(@RequestHeader HttpHeaders headers, @PathVariable("id") int id) {
		LocalDateTime dtHrInicio = LocalDateTime.now();
    	ResponseDto body = new ResponseDto();
		ResponseEntity<ResponseDto> response = null;
		try {
			if (AssociadoBusiness.deleteAssociado(id) == 0) {
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
