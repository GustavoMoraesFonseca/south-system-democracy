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

import br.com.southsystem.voto.business.AssociadoBusiness;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.AssociadoDto;

@WebMvcTest(AssociadoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes={AssociadoController.class})
public class AssociadoControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    private static final HttpHeaders HEADERS = new HttpHeaders();
    private static MockedStatic<AssociadoBusiness> associadoBusinessMock = Mockito.mockStatic(AssociadoBusiness.class);
    private static AssociadoDto associadoDto = new AssociadoDto();
    
    @BeforeAll
    public static void setup() {
    	HEADERS.add("Content-Type", "application/json;charset=UTF-8");
    	HEADERS.add("Accept", "application/json;charset=UTF-8");
    }
    
    @AfterAll
    public static void close() {
    	associadoBusinessMock.close();
    }
    
    @Nested
    @DisplayName("Testes do CreateAssociado")
    class WhenCreateAssociado {
    	
    	@BeforeEach
    	public void setupCreateAssociado() {
        	associadoDto.setCpf("393.905.650-22");
        	associadoDto.setEmail("gu.moraes@gmail.com");
        	associadoDto.setNome("Gustavo Moraes");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando criar um associado")
		public void shouldReturnOk_whenCreateAssociado() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.createAssociado(associadoDto)).thenReturn(1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/associado")
					.headers(HEADERS)
					.content("{\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes@gmail.com\",\"nome\":\"Gustavo Moraes\"}"))
	                .andExpect(status().isCreated())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":\"Seu ID de Associado é: 1\",\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Bad Request, quando criar um associado com CPF inválido")
		public void shouldReturnBadRequest_whenCreateAssociadoAssembleiaNotConfigured() throws Exception {
			associadoDto.setCpf("333.935.630-33");
		    associadoBusinessMock.when(() -> AssociadoBusiness.createAssociado(associadoDto)).thenReturn(0);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/associado")
					.headers(HEADERS)
					.content("{\"cpf\":\"333.935.630-33\",\"email\":\"gu.moraes@gmail.com\",\"nome\":\"Gustavo Moraes\"}"))
	                .andExpect(status().isBadRequest())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\"O CPF informado é inválido.\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Conflict, quando criar um associado já existente")
		public void shouldReturnConflict_whenCreateAssociadoAlreadyExists() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.createAssociado(associadoDto))
		    	.thenThrow(SQLIntegrityConstraintViolationException.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/associado")
					.headers(HEADERS)
					.content("{\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes@gmail.com\",\"nome\":\"Gustavo Moraes\"}"))
	                .andExpect(status().isConflict())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\"Associado já cadastrado.\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando criar um associado que lança alguma exceção")
		public void shouldReturnInternalServerError_whenCreateAssociadoThatThrowsSameException() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.createAssociado(associadoDto)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/associado")
					.headers(HEADERS)
					.content("{\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes@gmail.com\",\"nome\":\"Gustavo Moraes\"}"))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do FindAssociadoById")
    class WhenFindAssociadoById {
    	
    	@BeforeEach
    	public void setupFindAssociadoById() {
			associadoDto.setId(1);
			associadoDto.setNome("Gustavo Moraes");
			associadoDto.setCpf("393.905.650-22");
			associadoDto.setEmail("gu.moraes.fonseca@gmail.com");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando buscar um associado pelo seu Id")
		public void shouldReturnOk_whenFindAssociadoById() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.findAssociadoById(1)).thenReturn(associadoDto);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/associado/1")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":{\"id\":1,\"nome\":\"Gustavo Moraes\",\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes.fonseca@gmail.com\"},\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando buscar um associado pelo seu Id que não existe")
		public void shouldReturnNotFound_whenFindAssociadoByIdThatNotExists() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.findAssociadoById(1)).thenReturn(new AssociadoDto());
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/associado/1")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando buscar um associado pelo seu Id lança alguma exceção")
		public void shouldReturnInternalServerError_whenFindAssociadoByIdThrowsSameException() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.findAssociadoById(-1)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/associado/-1")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do FindAllAssociados")
	class WhenFindAllAssociados {
    	
    	@BeforeEach
    	public void setupFindAllAssociados() {
			associadoDto.setId(1);
			associadoDto.setNome("Gustavo Moraes");
			associadoDto.setCpf("393.905.650-22");
			associadoDto.setEmail("gu.moraes.fonseca@gmail.com");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando buscar todos os associados gravados")
		public void shouldReturnOk_whenFindAllAssociados() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.findAllAssociados()).thenReturn(List.of(associadoDto));
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/associado")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":[{\"id\":1,\"nome\":\"Gustavo Moraes\",\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes.fonseca@gmail.com\"}],\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando não achar nenhum associado gravado")
		public void shouldReturnNotFound_whenFindAllAssociadosNotFoundAnyAssociado() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.findAllAssociados()).thenReturn(List.of());
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/associado")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando buscar todos os associados lança alguma exceção")
		public void shouldReturnInternalServerError_whenFindAllAssociadosThrowsSameException() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.findAllAssociados()).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/associado")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do UpdateAssociado")
    class WhenUpdateAssociado {
    	
		@Test
		@DisplayName("Deve retornar OK, quando alterar um associado")
		public void shouldReturnOk_whenUpdateAssociado() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.updateAssociado(Mockito.any(AssociadoDto.class), Mockito.anyInt())).thenReturn(1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/associado/1")
					.headers(HEADERS)
					.content("{\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes@hotmail.com\",\"nome\":\"Gustavo Moraes Fonseca\"}"))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":\"Alteração feita com sucesso.\",\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando não achar nenhum associado gravado")
		public void shouldReturnNotFound_whenFindAllAssociadosNotFoundAnyAssociado() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.updateAssociado(associadoDto, 0)).thenReturn(0);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/associado/0")
					.headers(HEADERS)
					.content("{\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes@hotmail.com\",\"nome\":\"Gustavo Moraes Fonseca\"}"))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando altera um associado que lança alguma exceção")
		public void shouldReturnInternalServerError_whenupdateAssociadoThatThrowsSameException() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.updateAssociado(Mockito.any(AssociadoDto.class), Mockito.anyInt())).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/associado/-1")
					.headers(HEADERS)
					.content("{\"cpf\":\"393.905.650-22\",\"email\":\"gu.moraes@gmail.com\",\"nome\":\"Gustavo Moraes\"}"))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do DeleteAssociado")
    class WhenDeleteAssociado {
    	
		@Test
		@DisplayName("Deve retornar OK, quando excluir um associado pelo seu Id")
		public void shouldReturnOk_whenDeleteAssociadoById() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.deleteAssociado(1)).thenReturn(1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/associado/1")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":\"Exclusão feita com sucesso.\",\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando não achar o associado")
		public void shouldReturnNotFound_whenNotFoundAssociado() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.deleteAssociado(0)).thenReturn(0);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/associado/0")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando deletar um associado lança alguma exceção")
		public void shouldReturnInternalServerError_whenDeleteAssociadoThrowsSameException() throws Exception {
		    associadoBusinessMock.when(() -> AssociadoBusiness.deleteAssociado(-1)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/associado/-1")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
}