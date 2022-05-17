package br.com.southsystem.voto.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

import br.com.southsystem.voto.business.AssembleiaBusiness;
import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.AssembleiaConfigureDto;

@WebMvcTest(AssembleiaController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes={AssembleiaController.class})
public class AssembleiaControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    private static final HttpHeaders HEADERS = new HttpHeaders();
    private static MockedStatic<AssembleiaBusiness> assembleiaBusinessMock = Mockito.mockStatic(AssembleiaBusiness.class);
    
    @BeforeAll
    public static void setup() {
    	HEADERS.add("Content-Type", "application/json;charset=UTF-8");
    	HEADERS.add("Accept", "application/json;charset=UTF-8");
    }
    
    @AfterAll
    public static void close() {
    	assembleiaBusinessMock.close();
    }
    
	@Test
	@DisplayName("Deve retornar OK, quando configurar uma Assembleia")
	public void shouldReturnOk_whenConfigureAssembleia() throws Exception {
	    assembleiaBusinessMock.when(() -> AssembleiaBusiness.configureAssembleia(Mockito.any(AssembleiaConfigureDto.class))).thenReturn(true);
	    
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/assembleia")
				.headers(HEADERS)
				.content("{\"durationInMs\":null,\"pautaId\":1}"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertEquals("{\"data\":\"Assembleia aberta com sucesso! A contagem começara após o primeiro voto.\",\"error\":null}", responseBody);
	}
	
	@Test
	@DisplayName("Deve retornar Bad Request, quando configurar uma Assembleia")
	public void shouldReturnBadRequest_whenConfigureAssembleia() throws Exception {
	    assembleiaBusinessMock.when(() -> AssembleiaBusiness.configureAssembleia(Mockito.any(AssembleiaConfigureDto.class))).thenReturn(false);
	    
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/assembleia")
				.headers(HEADERS)
				.content("{\"durationInMs\": null,\"pautaId\": 0}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertEquals("{\"data\":null,\"error\":\"Pauta não encontrada ou Encerrada.\"}", responseBody);
	}
	
	@Test
	@DisplayName("Deve retornar Internal Server Error, quando configurar uma Assembleia que lança alguma exceção")
	public void shouldReturnInternalServerError_whenConfigureAssembleiaThatThrowsSameException() throws Exception {
	    assembleiaBusinessMock.when(() -> AssembleiaBusiness.configureAssembleia(Mockito.any(AssembleiaConfigureDto.class))).thenThrow(Exception.class);
	    
		MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/assembleia")
				.headers(HEADERS)
				.content("{\"durationInMs\":null,\"pautaId\":0}"))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertEquals("{\"data\":null,\"error\":\""+Constants.MESSAGE_ERROR_500+"\"}", responseBody);
	}
}
