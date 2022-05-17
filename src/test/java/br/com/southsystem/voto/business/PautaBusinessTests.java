package br.com.southsystem.voto.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import br.com.southsystem.voto.bean.PautaBean;
import br.com.southsystem.voto.command.CrudCommand;
import br.com.southsystem.voto.dao.PautaDAOJdbc;
import br.com.southsystem.voto.dto.PautaDto;

public class PautaBusinessTests {

    private static MockedConstruction<CrudCommand> commandMock;
	private static CrudCommand<PautaBean, PautaDto> command;
    
	private static PautaDto pautaDto = new PautaDto();
	
    @BeforeAll
    public static void init(){
    	commandMock = Mockito.mockConstruction(CrudCommand.class, withSettings().useConstructor(new PautaDAOJdbc()));
    	command = new CrudCommand<PautaBean, PautaDto>(new PautaDAOJdbc());
    	PautaBusiness.command = command;
    }

    @AfterAll
    public static void close() {
    	commandMock.close();
    }
    
	int retorno;
	
	@BeforeEach
	public void setUp() {
		retorno = 0;
		
		pautaDto.setId(1);
		pautaDto.setDescricao("Gustavo deve se juntar a equipe?");
		
		command = new CrudCommand<PautaBean, PautaDto>(new PautaDAOJdbc());
    	PautaBusiness.command = command; 
	}
    
    @Nested
    @DisplayName("Testes do CreatePauta")
    class WhenCreatePauta {
    	
    	@Test
    	@DisplayName("Deve retornar um inteiro positivo, quando chamar criar pauta com Sucesso")
    	public void shouldReturnPositiveInteger_whenCallCreatePauta() throws Exception {
    		when(command.create(Mockito.any())).thenReturn(1);
    		
    		retorno = PautaBusiness.createPauta(pautaDto);
    		
    		assertEquals(1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar criar pauta chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.create(Mockito.any())).thenReturn(1);
    		
        	assertThrows(Exception.class, () -> PautaBusiness.createPauta(null));
    	}
    }
    
    @Nested
    @DisplayName("Testes do FindPautaById")
    class WhenFindPautaById {
    	
    	@Test
    	@DisplayName("Deve retornar um PautaDto, quando chamar busca pauta por Id")
    	public void shouldReturnPautaDto_whenCallFindPautaById() throws Exception {
    		pautaDto.setQtdVotosPositivos(100);
    		pautaDto.setQtdVotosNegativos(0);
    		pautaDto.setResultado("Maioria optou por SIM");
    		
    		PautaDto pautaDtoRetrono = new PautaDto();

    		when(command.findById(Mockito.anyInt())).thenReturn(pautaDto);

    		pautaDtoRetrono = PautaBusiness.findPautaById(1);

    		assertEquals(pautaDto, pautaDtoRetrono);
    	}

    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.findById(Mockito.anyInt())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> PautaBusiness.findPautaById(Mockito.anyInt()));
    	}

    }
    
    @Nested
    @DisplayName("Testes do FindAllPautas")
    class WhenFindAllPautas {
    	
    	@Test
    	@DisplayName("Deve retornar uma lista de PautaDto, quando chamar busca todos os pautas")
    	public void shouldReturnPautaDto_whenCallFindPautaById() throws Exception {
    		List<PautaDto> pautaDtoEsperada = new ArrayList<PautaDto>();
    		
    		pautaDto.setQtdVotosPositivos(100);
    		pautaDto.setQtdVotosNegativos(100);
    		pautaDto.setResultado("EMPATE");
    		pautaDtoEsperada.add(pautaDto);

    		pautaDto.setQtdVotosPositivos(0);
    		pautaDto.setQtdVotosNegativos(0);
    		pautaDto.setResultado("Ainda não houve votos.");
    		pautaDtoEsperada.add(pautaDto);
    		
    		pautaDto.setQtdVotosPositivos(0);
    		pautaDto.setQtdVotosNegativos(1);
    		pautaDto.setResultado("Maioria optou por NÃO");
    		pautaDtoEsperada.add(pautaDto);
    		
    		when(command.findAll()).thenReturn(pautaDtoEsperada);

    		List<PautaDto> pautaDtoRetrono = PautaBusiness.findAllPautas();

    		assertEquals(pautaDtoEsperada, pautaDtoRetrono);
    	}

    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.findAll()).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> PautaBusiness.findAllPautas());
    	}

    }
    
    @Nested
    @DisplayName("Testes do UpdatePauta")
    class WhenUpdatePauta {
    	
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar atualiza pauta com Sucesso")
    	public void shouldReturn1_whenCallUpdatePauta() throws Exception {
    		when(command.update(Mockito.any())).thenReturn(1);
    		
    		retorno = PautaBusiness.updatePauta(pautaDto, 1);
    		
    		assertEquals(1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar criar pauta chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.update(Mockito.any())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> PautaBusiness.updatePauta(pautaDto, 1));
    	}
    	
    }
    
    @Nested
    @DisplayName("Testes do DeletePautaById")
    class WhenDeletePautaById {
        
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar delete pauta")
    	public void shouldReturn1_whenCallDeletePauta() throws Exception {
    		when(command.delete(Mockito.anyInt())).thenReturn(1);

    		int retorno = PautaBusiness.deletePauta(1);

    		assertEquals(1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.delete(Mockito.anyInt())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> PautaBusiness.deletePauta(Mockito.anyInt()));
    	}
    	
    }

}
