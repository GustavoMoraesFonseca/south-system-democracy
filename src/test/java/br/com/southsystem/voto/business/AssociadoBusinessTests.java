package br.com.southsystem.voto.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import br.com.southsystem.voto.bean.AssociadoBean;
import br.com.southsystem.voto.command.CrudCommand;
import br.com.southsystem.voto.dao.AssociadoDAOJdbc;
import br.com.southsystem.voto.dto.AssociadoDto;

public class AssociadoBusinessTests {

    private static MockedConstruction<CrudCommand> commandMock;
	private static CrudCommand<AssociadoBean, AssociadoDto> command;
    
	private static AssociadoDto associadoDto = new AssociadoDto();
	
    @BeforeAll
    public static void init(){
    	commandMock = Mockito.mockConstruction(CrudCommand.class, withSettings().useConstructor(new AssociadoDAOJdbc()));
    	command = new CrudCommand<AssociadoBean, AssociadoDto>(new AssociadoDAOJdbc());
    	AssociadoBusiness.command = command;
    }

    @AfterAll
    public static void close() {
    	commandMock.close();
    }
    
	int retorno;
	
	@BeforeEach
	public void setUp() {
		retorno = 0;
		
		associadoDto.setCpf("801.401.300-04");
		associadoDto.setEmail("gu.moraes@gmail.com");
		associadoDto.setNome("Gustavo Moraes");
		
		command = new CrudCommand<AssociadoBean, AssociadoDto>(new AssociadoDAOJdbc());
    	AssociadoBusiness.command = command; 
	}
    
    @Nested
    @DisplayName("Testes do CreateAssociado")
    class WhenCreateAssociado {
    	
    	@Test
    	@DisplayName("Deve retornar um inteiro positivo, quando chamar criar associado com Sucesso")
    	public void shouldReturnPositiveInteger_whenCallCreateAssociado() throws Exception {
    		when(command.create(Mockito.any())).thenReturn(1);
    		
    		retorno = AssociadoBusiness.createAssociado(associadoDto);
    		
    		assertEquals(1, retorno);
    	}
    	    	
    	@Test
    	@DisplayName("Deve retornar 0, quando chamar criar associado com o CPF errado")
    	public void shouldReturn0_whenCallCreateAssociadoWithWrongCPF() throws Exception {
    		associadoDto.setCpf("999.999.999-99");
    		
    		retorno = AssociadoBusiness.createAssociado(associadoDto);
    		
    		assertEquals(0, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar criar associado chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.create(Mockito.any())).thenReturn(1);
    		
        	assertThrows(Exception.class, () -> AssociadoBusiness.createAssociado(null));
    	}
    }
    
    @Nested
    @DisplayName("Testes do FindAssociadoById")
    class WhenFindAssociadoById {
    	
    	@Test
    	@DisplayName("Deve retornar um AssociadoDto, quando chamar busca associado por Id")
    	public void shouldReturnAssociadoDto_whenCallFindAssociadoById() throws Exception {
    		AssociadoDto associadoDto = new AssociadoDto();

    		associadoDto.setId(1);
    		associadoDto.setCpf("801.401.300-04");
    		associadoDto.setEmail("gu.moraes@gmail.com");
    		associadoDto.setNome("Gustavo Moraes");

    		AssociadoDto associadoDtoRetrono = new AssociadoDto();

    		when(command.findById(Mockito.anyInt())).thenReturn(associadoDto);

    		associadoDtoRetrono = AssociadoBusiness.findAssociadoById(1);

    		assertEquals(associadoDto, associadoDtoRetrono);
    	}

    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.findById(Mockito.anyInt())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> AssociadoBusiness.findAssociadoById(Mockito.anyInt()));
    	}

    }
    
    @Nested
    @DisplayName("Testes do FindAllAssociados")
    class WhenFindAllAssociados {
    	
    	@Test
    	@DisplayName("Deve retornar uma lista de AssociadoDto, quando chamar busca todos os associados")
    	public void shouldReturnAssociadoDto_whenCallFindAssociadoById() throws Exception {
    		AssociadoDto associadoDto = new AssociadoDto();

    		associadoDto.setId(1);
    		associadoDto.setCpf("801.401.300-04");
    		associadoDto.setEmail("gu.moraes@gmail.com");
    		associadoDto.setNome("Gustavo Moraes");

    		List<AssociadoDto> associadoDtoRetrono;

    		when(command.findAll()).thenReturn(List.of(associadoDto));

    		associadoDtoRetrono = AssociadoBusiness.findAllAssociados();

    		assertEquals(List.of(associadoDto), associadoDtoRetrono);
    	}

    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.findAll()).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> AssociadoBusiness.findAllAssociados());
    	}

    }
    
    @Nested
    @DisplayName("Testes do UpdateAssociado")
    class WhenUpdateAssociado {
    	
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar atualiza associado com Sucesso")
    	public void shouldReturn1_whenCallUpdateAssociado() throws Exception {
    		when(command.update(Mockito.any())).thenReturn(1);
    		
    		retorno = AssociadoBusiness.updateAssociado(associadoDto, 1);
    		
    		assertEquals(1, retorno);
    	}
    	    	
    	@Test
    	@DisplayName("Deve retornar 0, quando chamar atualiza associado com o CPF errado")
    	public void shouldReturn0_whenCallUpdateAssociadoWithWrongCPF() throws Exception {
    		associadoDto.setCpf("999.999.999-99");
    		
    		retorno = AssociadoBusiness.updateAssociado(associadoDto, 1);
    		
    		assertEquals(0, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar criar associado chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.update(Mockito.any())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> AssociadoBusiness.updateAssociado(associadoDto, 1));
    	}
    	
    }
    
    @Nested
    @DisplayName("Testes do DeleteAssociadoById")
    class WhenDeleteAssociadoById {
        
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar delete associado")
    	public void shouldReturn1_whenCallDeleteAssociado() throws Exception {
    		when(command.delete(Mockito.anyInt())).thenReturn(1);

    		int retorno = AssociadoBusiness.deleteAssociado(1);

    		assertEquals(1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.delete(Mockito.anyInt())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> AssociadoBusiness.deleteAssociado(Mockito.anyInt()));
    	}
    	
    }

}
