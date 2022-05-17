package br.com.southsystem.voto.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.southsystem.voto.business.PautaBusiness;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.PautaDto;

@WebMvcTest(PautaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes={PautaController.class, PautaBusiness.class})
public class PautaControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    private static final HttpHeaders HEADERS = new HttpHeaders();
    private static MockedStatic<PautaBusiness> pautaBusinessMock = Mockito.mockStatic(PautaBusiness.class);
    private static PautaDto pautaDto = new PautaDto();
    
    @BeforeAll
    public static void setup() {
    	HEADERS.add("Content-Type", "application/json;charset=UTF-8");
    	HEADERS.add("Accept", "application/json;charset=UTF-8");
    }
    
    @AfterAll
    public static void close() {
    	pautaBusinessMock.close();
    }
    
    @Nested
    @DisplayName("Testes do CreatePauta")
    class WhenCreatePauta {
    	
    	@BeforeEach
    	public void setupCreatePauta() {
        	pautaDto.setDescricao("Gustavo deve se juntar a equipe?");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando criar uma Pauta")
		public void shouldReturnOk_whenCreatePauta() throws Exception {
			pautaBusinessMock.when(() -> PautaBusiness.createPauta(pautaDto)).thenReturn(1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/pauta")
					.headers(HEADERS)
					.content("{\"descricao\":\"Gustavo deve se juntar a equipe?\"}"))
	                .andExpect(status().isCreated())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":1,\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Conflict, quando criar uma Pauta já existente")
		public void shouldReturnConflict_whenCreatePautaAlreadyExists() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.createPauta(pautaDto)).thenThrow(SQLIntegrityConstraintViolationException.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/pauta")
					.headers(HEADERS)
					.content("{\"descricao\":\"Gustavo deve se juntar a equipe?\"}"))
	                .andExpect(status().isConflict())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\"Pauta já cadastrada.\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando criar uma pauta que lança alguma exceção")
		public void shouldReturnInternalServerError_whenCreatePautaThatThrowsSameException() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.createPauta(pautaDto)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/pauta")
					.headers(HEADERS)
					.content("{\"descricao\":\"Gustavo deve se juntar a equipe?\"}"))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do FindPautaById")
    class WhenFindPautaById {
    	
    	@BeforeEach
    	public void setupFindVoteById() {
			pautaDto.setId(1);
			pautaDto.setDescricao("Gustavo deve se juntar a equipe?");
			pautaDto.setQtdVotosNegativos(0);
			pautaDto.setQtdVotosPositivos(100);
			pautaDto.setResultado("Maioria optou por SIM");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando buscar uma pauta pelo seu Id")
		public void shouldReturnOk_whenFindPautaById() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.findPautaById(1)).thenReturn(pautaDto);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pauta/1")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":{\"id\":1,\"descricao\":\"Gustavo deve se juntar a equipe?\",\"resultado\":\"Maioria optou por SIM\"},\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando buscar uma pauta pelo seu Id que não existe")
		public void shouldReturnNotFound_whenFindPautaByIdThatNotExists() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.findPautaById(1)).thenReturn(new PautaDto());
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pauta/1")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando buscar uma pauta pelo seu Id lança alguma exceção")
		public void shouldReturnInternalServerError_whenFindPautaByIdThrowsSameException() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.findPautaById(-1)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pauta/-1")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do FindAllPautas")
	class WhenFindAllPautas {
    	
    	@BeforeEach
    	public void setupFindAllVotes() {
			pautaDto.setId(1);
			pautaDto.setDescricao("Gustavo deve se juntar a equipe?");
			pautaDto.setQtdVotosNegativos(0);
			pautaDto.setQtdVotosPositivos(100);
			pautaDto.setResultado("Maioria optou por SIM");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando buscar todas as pautas gravadas")
		public void shouldReturnOk_whenFindAllPautas() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.findAllPautas()).thenReturn(List.of(pautaDto));
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pauta")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":[{\"id\":1,\"descricao\":\"Gustavo deve se juntar a equipe?\",\"resultado\":\"Maioria optou por SIM\"}],\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando não achar nenhuma pauta gravada")
		public void shouldReturnNotFound_whenFindAllVotesNotFoundAnyPauta() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.findAllPautas()).thenReturn(List.of());
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pauta")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando buscar todos as pautas lança alguma exceção")
		public void shouldReturnInternalServerError_whenFindAllPautasThrowsSameException() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.findAllPautas()).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pauta")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do DeletePauta")
    class WhenDeletePauta {
    	
		@Test
		@DisplayName("Deve retornar OK, quando excluir uma pauta pelo seu Id")
		public void shouldReturnOk_whenDeletePautaById() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.deletePauta(1)).thenReturn(1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/pauta/1")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":\"Exclusão feita com sucesso.\",\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando não achar a pauta")
		public void shouldReturnNotFound_whenNotFoundPauta() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.deletePauta(0)).thenReturn(0);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/pauta/0")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando deletar uma pauta lança alguma exceção")
		public void shouldReturnInternalServerError_whenDeletePautaThrowsSameException() throws Exception {
		    pautaBusinessMock.when(() -> PautaBusiness.deletePauta(-1)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/pauta/-1")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
}