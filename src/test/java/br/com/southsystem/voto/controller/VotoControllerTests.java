package br.com.southsystem.voto.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import br.com.southsystem.voto.business.VotoBusiness;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.VotoConsultDto;
import br.com.southsystem.voto.dto.VotoDto;

@WebMvcTest(VotoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes={VotoController.class})
public class VotoControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    private static final HttpHeaders HEADERS = new HttpHeaders();
    private static MockedStatic<VotoBusiness> votoBusinessMock = Mockito.mockStatic(VotoBusiness.class);
    private static VotoDto votoDto = new VotoDto();
    private static VotoConsultDto votoConsultDto = new VotoConsultDto();
    
    @BeforeAll
    public static void setup() {
    	HEADERS.add("Content-Type", "application/json;charset=UTF-8");
    	HEADERS.add("Accept", "application/json;charset=UTF-8");
    }
    
    @AfterAll
    public static void close() {
    	votoBusinessMock.close();
    }
    
    @Test
    @Disabled
    public void obtainAccessToken() throws Exception {	 
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "password");
//        params.add("associado", "#joinSouthSystem");
//        params.add("username", "admin");
//        params.add("password", "admin@123");
//
//         mockMvc.perform(post("/oauth/token")
//            .params(params)
//            .with(httpBasic("associado","#joinSouthSystem"))
//            .accept("application/json;charset=UTF-8"))
//            .andExpect(status().isOk());
    }
    
    @Nested
    @DisplayName("Testes do CreateVote")
    class WhenCreateVote {
    	
    	@BeforeEach
    	public void setupCreateVote() {
        	votoDto.setAssociadoId(1);
        	votoDto.setIsFavor("Sim");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando criar um voto")
		public void shouldReturnOk_whenCreateVote() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.createVoto(votoDto)).thenReturn(1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/voto")
					.headers(HEADERS)
					.content("{\"associadoId\":1,\"isFavor\":\"Sim\"}"))
	                .andExpect(status().isCreated())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":1,\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Bad Request, quando criar um voto com valor diferente de 'Sim' ou 'Não'")
		public void shouldReturnBadRequest_whenCreateVoteDiffSimOrNao() throws Exception {
			votoDto.setIsFavor("sim");
		    votoBusinessMock.when(() -> VotoBusiness.createVoto(votoDto)).thenReturn(0);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/voto")
					.headers(HEADERS)
					.content("{\"associadoId\":1,\"isFavor\":\"sim\"}"))
	                .andExpect(status().isBadRequest())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\"Os votos só poderão ser 'Sim' ou 'Não'\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Bad Request, quando criar um voto com a Assembleia não configurada")
		public void shouldReturnBadRequest_whenCreateVoteAssembleiaNotConfigured() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.createVoto(votoDto)).thenReturn(-1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/voto")
					.headers(HEADERS)
					.content("{\"associadoId\":1,\"isFavor\":\"Sim\"}"))
	                .andExpect(status().isBadRequest())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\"A Assembleia ainda não foi configurada.\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Conflict, quando criar um voto já existente")
		public void shouldReturnConflict_whenCreateVoteAlreadyExists() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.createVoto(votoDto)).thenReturn(-2);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/voto")
					.headers(HEADERS)
					.content("{\"associadoId\":1,\"isFavor\":\"Sim\"}"))
	                .andExpect(status().isConflict())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\"Seu voto já foi cadastrado nessa Pauta.\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Bad Request, quando criar um voto com a Associado não cadastrado")
		public void shouldReturnBadRequest_whenCreateVoteAssociadoThatNotExists() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.createVoto(votoDto)).thenThrow(SQLIntegrityConstraintViolationException.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/voto")
					.headers(HEADERS)
					.content("{\"associadoId\":1,\"isFavor\":\"Sim\"}"))
	                .andExpect(status().isBadRequest())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\"Associado não cadastrado.\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando criar um voto que lança alguma exceção")
		public void shouldReturnInternalServerError_whenCreateVoteThatThrowsSameException() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.createVoto(votoDto)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/voto")
					.headers(HEADERS)
					.content("{\"associadoId\":1,\"isFavor\":\"Sim\"}"))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do FindVoteById")
    class WhenFindVoteById {
    	
    	@BeforeEach
    	public void setupFindVoteById() {
    		votoConsultDto.setId(1);
    		votoConsultDto.setAssociadoNome("Gustavo");
    		votoConsultDto.setAssociadoEmail("gu.moraes.fonseca@gmail.com");
    		votoConsultDto.setPautaNome("Gustavo deve se juntar a equipe?");
    		votoConsultDto.setVotou("Sim");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando buscar um voto pelo seu Id")
		public void shouldReturnOk_whenFindVoteById() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.findVotoById(1)).thenReturn(votoConsultDto);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/voto/1")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":{\"id\":1,\"associadoNome\":\"Gustavo\",\"associadoEmail\":\"gu.moraes.fonseca@gmail.com\",\"pautaNome\":\"Gustavo deve se juntar a equipe?\",\"votou\":\"Sim\"},\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando buscar um voto pelo seu Id que não existe")
		public void shouldReturnNotFound_whenFindVoteByIdThatNotExists() throws Exception {
			votoConsultDto = new VotoConsultDto();
			
		    votoBusinessMock.when(() -> VotoBusiness.findVotoById(1)).thenReturn(new VotoConsultDto());
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/voto/1")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando buscar um voto pelo seu Id lança alguma exceção")
		public void shouldReturnInternalServerError_whenFindVoteByIdThrowsSameException() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.findVotoById(-1)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/voto/-1")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do FindAllVotes")
	class WhenFindAllVotes {
    	
    	@BeforeEach
    	public void setupFindAllVotes() {
    		votoConsultDto.setId(1);
    		votoConsultDto.setAssociadoNome("Gustavo");
    		votoConsultDto.setAssociadoEmail("gu.moraes.fonseca@gmail.com");
    		votoConsultDto.setPautaNome("Gustavo deve se juntar a equipe?");
    		votoConsultDto.setVotou("Sim");
    	}
    	
		@Test
		@DisplayName("Deve retornar OK, quando buscar todos os votos gravados")
		public void shouldReturnOk_whenFindAllVotes() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.findAllVotos()).thenReturn(List.of(votoConsultDto));
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/voto")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":[{\"id\":1,\"associadoNome\":\"Gustavo\",\"associadoEmail\":\"gu.moraes.fonseca@gmail.com\",\"pautaNome\":\"Gustavo deve se juntar a equipe?\",\"votou\":\"Sim\"}],\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando não achar nenhum voto gravado")
		public void shouldReturnNotFound_whenFindAllVotesNotFoundAnyVote() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.findAllVotos()).thenReturn(List.of());
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/voto")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando buscar todos os votos lança alguma exceção")
		public void shouldReturnInternalServerError_whenFindAllVotesThrowsSameException() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.findAllVotos()).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/voto")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
    
    @Nested
    @DisplayName("Testes do DeleteVote")
    class WhenDeleteVote {
    	
		@Test
		@DisplayName("Deve retornar OK, quando excluir um voto pelo seu Id")
		public void shouldReturnOk_whenDeleteVoteById() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.deleteVoto(1)).thenReturn(1);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/voto/1")
					.headers(HEADERS))
	                .andExpect(status().isOk())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":\"Exclusão feita com sucesso.\",\"error\":null}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Not Found, quando não achar o voto")
		public void shouldReturnNotFound_whenNotFoundVote() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.deleteVoto(0)).thenReturn(0);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/voto/0")
					.headers(HEADERS))
	                .andExpect(status().isNotFound())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_404+"\"}", responseBody);
		}
		
		@Test
		@DisplayName("Deve retornar Internal Server Error, quando deletar um voto lança alguma exceção")
		public void shouldReturnInternalServerError_whenDeleteVoteThrowsSameException() throws Exception {
		    votoBusinessMock.when(() -> VotoBusiness.deleteVoto(-1)).thenThrow(Exception.class);
		    
			MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/voto/-1")
					.headers(HEADERS))
	                .andExpect(status().isInternalServerError())
	                .andReturn();
	
	        String responseBody = response.getResponse().getContentAsString();
	        assertNotNull(responseBody);
	        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
		}
    }
}