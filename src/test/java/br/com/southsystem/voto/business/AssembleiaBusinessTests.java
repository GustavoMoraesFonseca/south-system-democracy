package br.com.southsystem.voto.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import br.com.southsystem.voto.command.AssembleiaCommand;
import br.com.southsystem.voto.dto.AssembleiaConfigureDto;
import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoDto;

public class AssembleiaBusinessTests {

	private static MockedStatic<AssembleiaCommand> commandMock = Mockito.mockStatic(AssembleiaCommand.class);
    private static PautaDto pautaDto = new PautaDto();
    private static VotoDto votoDto = new VotoDto();
    
    @AfterAll
    public static void close() {
    	commandMock.close();
    }
	
    @Nested
    @DisplayName("Testes do GetQtdDeVotos")
    class WhenGetQtdDeVotos {
        
    	@Test
        @DisplayName("Deve retornar uma quantidade inteira, quando chamar consulta quantidade de votos")
        public void shouldReturnIntegerQuantity_whenCallsGetQtdDeVotos() throws Exception {
        	commandMock.when(() -> AssembleiaCommand.getQtdDeVotos(false)).thenReturn(1);
        	
        	Integer expectedReturn = 1;
        	
        	Integer retorno = AssembleiaBusiness.getQtdDeVotos(false);
        	
        	assertEquals(expectedReturn, retorno);
        }
        
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar consulta quantidade de votos chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
        	commandMock.when(() -> AssembleiaCommand.getQtdDeVotos(false)).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> AssembleiaBusiness.getQtdDeVotos(false));
    	}
    }
	
    @Nested
    @DisplayName("Testes do GetLstVotos")
    class WhenGetLstVotos {
        
    	@Test
        @DisplayName("Deve retornar uma lista de VotoDto, quando chamar consulta lista de votos")
        public void shouldReturnVotoDtoList_whenCallsGetListVotos() throws Exception {
    		votoDto.setId(1);
    		votoDto.setAssociadoId(1);
    		votoDto.setPautaId(1);
    		votoDto.setIsFavor("Sim");
    		
        	commandMock.when(() -> AssembleiaCommand.getListVotos()).thenReturn(List.of(votoDto));
        	
        	List<VotoDto> lstVotoDtoRetorno = AssembleiaBusiness.getListVotos();
        	
        	assertEquals(lstVotoDtoRetorno, List.of(votoDto));
        }
        
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar consulta lista de votos chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
        	commandMock.when(() -> AssembleiaCommand.getListVotos()).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> AssembleiaBusiness.getListVotos());
    	}
    }
    
    @Nested
    @DisplayName("Testes do ConfigureAssembleia")
    class WhenConfigureAssembleia {
    	
        @Test
        @DisplayName("Deve retornar Verdadeiro, quando chamar configura assembleia")
        public void shouldReturnTrue_whenCallsConfigureAssembleia() throws Exception {
    		AssembleiaConfigureDto assembleiaConfigureDto = new AssembleiaConfigureDto();
    		assembleiaConfigureDto.setDurationInMs(null);
    		assembleiaConfigureDto.setPautaId(1);
    		
    		pautaDto.setId(1);
    		
    		commandMock.when(() -> AssembleiaCommand.findOpenPautaById(assembleiaConfigureDto.getPautaId())).thenReturn(pautaDto);
        	
        	assertTrue(AssembleiaBusiness.configureAssembleia(assembleiaConfigureDto));
        }
        
        @Test
        @DisplayName("Deve retornar Falso, quando chamar configura assembleia que não exista uma pauta")
        public void shouldReturnFalse_whenCallsConfigureAssembleiaThatPautaNotExists() throws Exception {
    		AssembleiaConfigureDto assembleiaConfigureDto = new AssembleiaConfigureDto();
    		assembleiaConfigureDto.setDurationInMs(null);
    		assembleiaConfigureDto.setPautaId(1);
    		
    		pautaDto.setId(0);
    		
    		commandMock.when(() -> AssembleiaCommand.findOpenPautaById(assembleiaConfigureDto.getPautaId())).thenReturn(pautaDto);
        	
        	assertFalse(AssembleiaBusiness.configureAssembleia(assembleiaConfigureDto));
        }
        
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar configura assembleia chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		commandMock.when(() -> AssembleiaCommand.findOpenPautaById(Mockito.anyInt())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> AssembleiaBusiness.configureAssembleia(null));
    	}
    }
}
