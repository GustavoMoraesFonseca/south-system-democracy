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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import br.com.southsystem.voto.bean.VotoBean;
import br.com.southsystem.voto.command.CrudCommand;
import br.com.southsystem.voto.commons.CommonUtils;
import br.com.southsystem.voto.config.KafkaProducerConfig;
import br.com.southsystem.voto.dao.VotoDAOJdbc;
import br.com.southsystem.voto.dto.VotoConsultDto;
import br.com.southsystem.voto.dto.VotoDto;

public class VotoBusinessTests {

	private static MockedStatic<CommonUtils> commonsMock;
    private static MockedConstruction<CrudCommand> commandMock;
	private static CrudCommand<VotoBean, VotoConsultDto> command;
    
	private static VotoDto votoDto = new VotoDto();
	
    @BeforeAll
    public static void init(){
    	commonsMock = Mockito.mockStatic(CommonUtils.class);
    	commandMock = Mockito.mockConstruction(CrudCommand.class, withSettings().useConstructor(new VotoDAOJdbc()));
    	command = new CrudCommand<VotoBean, VotoConsultDto>(new VotoDAOJdbc());
    	VotoBusiness.command = command;
    }

    @AfterAll
    public static void close() {
    	commonsMock.close();
    	commandMock.close();
    }
    
    @Nested
    @DisplayName("Testes do CreateVote")
    class WhenCreateVote {

    	int retorno;
    	
    	@BeforeEach
    	public void setupCreateVote() {
    		retorno = 0;
    		
    		votoDto.setPautaId(1);
    		votoDto.setAssociadoId(1);
    		votoDto.setIsFavor("Sim");
    		
    		command = new CrudCommand<VotoBean, VotoConsultDto>(new VotoDAOJdbc());
        	VotoBusiness.command = command; 
    	}
    	
    	@Test
    	@DisplayName("Deve retornar um inteiro positivo, quando chamar criar voto com Sucesso")
    	public void shouldReturnPositiveInteger_whenCallCreateVote() throws Exception {
    		KafkaProducerConfig.isAssembleiaConfirured = true;

    		commonsMock.when(() -> CommonUtils.verifyIsSecondVote(Mockito.any())).thenReturn(false);
    		
    		when(command.create(Mockito.any())).thenReturn(1);
    		
    		commonsMock.when(() -> CommonUtils.kafkaSendMessage(votoDto)).thenAnswer((Answer<Void>) invocation -> null);
    		
    		retorno = VotoBusiness.createVoto(votoDto);
    		
    		assertEquals(1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve retornar -1, quando chamar criar voto com a Assembleia não configurada")
    	public void shouldReturnNegative1_whenCallCreateVoteThatAssembleiaIsNotConfigured() throws Exception {
    		KafkaProducerConfig.isAssembleiaConfirured = false;

    		retorno = VotoBusiness.createVoto(votoDto);

    		assertEquals(-1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve retornar -2, quando chamar criar voto já existente")
    	public void shouldReturnNegative2_whenCallCreateVoteThatAlreadyExists() throws Exception {
    		KafkaProducerConfig.isAssembleiaConfirured = true;

    		commonsMock.when(() -> CommonUtils.verifyIsSecondVote(Mockito.any())).thenReturn(true);

    		retorno = VotoBusiness.createVoto(votoDto);
    		
    		assertEquals(-2, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve retornar 0, quando chamar criar voto diferente de 'Sim' ou 'Não'")
    	public void shouldReturn0_whenCallCreateVoteThatDiffSimOrNao() throws Exception {
    		votoDto.setIsFavor("sim");
    		
    		KafkaProducerConfig.isAssembleiaConfirured = true;

    		commonsMock.when(() -> CommonUtils.verifyIsSecondVote(Mockito.any())).thenReturn(false);

    		retorno = VotoBusiness.createVoto(votoDto);
    		
    		assertEquals(0, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar criar voto chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		votoDto.setIsFavor("Não");
    		
    		KafkaProducerConfig.isAssembleiaConfirured = true;

    		commonsMock.when(() -> CommonUtils.verifyIsSecondVote(Mockito.any())).thenReturn(false);

    		when(command.create(Mockito.any())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> VotoBusiness.createVoto(votoDto));
    	}
    }
    
    @Nested
    @DisplayName("Testes do FindVoteById")
    class WhenFindVoteById {
    	
    	@Test
    	@DisplayName("Deve retornar um VotoConsultDto, quando chamar busca voto por Id")
    	public void shouldReturnVotoConsultDto_whenCallFindVoteById() throws Exception {
    		VotoConsultDto votoConsultDto = new VotoConsultDto();

    		votoConsultDto.setId(1);
    		votoConsultDto.setPautaNome("Gustavo deve se juntar a equipe?");
    		votoConsultDto.setAssociadoNome("Gustavo");
    		votoConsultDto.setAssociadoEmail("gu.moraes.fonseca@gmail.com");
    		votoConsultDto.setVotou("Sim");

    		VotoConsultDto votoConsultDtoRetrono = new VotoConsultDto();

    		when(command.findById(Mockito.anyInt())).thenReturn(votoConsultDto);

    		votoConsultDtoRetrono = VotoBusiness.findVotoById(1);

    		assertEquals(votoConsultDto, votoConsultDtoRetrono);
    	}

    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.findById(Mockito.anyInt())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> VotoBusiness.findVotoById(Mockito.anyInt()));
    	}

    }
    
    @Nested
    @DisplayName("Testes do FindAllVotes")
    class WhenFindAllVotes {
    	
    	@Test
    	@DisplayName("Deve retornar uma lista de VotoConsultDto, quando chamar busca todos os votos")
    	public void shouldReturnVotoConsultDto_whenCallFindVoteById() throws Exception {
    		VotoConsultDto votoConsultDto = new VotoConsultDto();

    		votoConsultDto.setId(1);
    		votoConsultDto.setPautaNome("Gustavo deve se juntar a equipe?");
    		votoConsultDto.setAssociadoNome("Gustavo");
    		votoConsultDto.setAssociadoEmail("gu.moraes.fonseca@gmail.com");
    		votoConsultDto.setVotou("Sim");

    		List<VotoConsultDto> votoConsultDtoRetrono;

    		when(command.findAll()).thenReturn(List.of(votoConsultDto));

    		votoConsultDtoRetrono = VotoBusiness.findAllVotos();

    		assertEquals(List.of(votoConsultDto), votoConsultDtoRetrono);
    	}

    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.findAll()).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> VotoBusiness.findAllVotos());
    	}

    }
    
    @Nested
    @DisplayName("Testes do UpdateVote")
    class WhenUpdateVote {

    	int retorno;
    	
    	@BeforeEach
    	public void setupCreateVote() {
    		retorno = 0;
    		
    		votoDto.setPautaId(1);
    		votoDto.setAssociadoId(1);
    		votoDto.setIsFavor("Sim");
    	}
    	
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar update voto")
    	public void shouldReturn1_whenCallDeleteVote() throws Exception {
    		when(command.update(Mockito.any())).thenReturn(1);

    		retorno = VotoBusiness.updateVoto(votoDto, 1);

    		assertEquals(1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.update(Mockito.any())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> VotoBusiness.updateVoto(votoDto, Mockito.anyInt()));
    	}
    	
    }
    
    @Nested
    @DisplayName("Testes do DeleteVoteById")
    class WhenDeleteVoteById {
        
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar delete voto")
    	public void shouldReturn1_whenCallDeleteVote() throws Exception {
    		when(command.delete(Mockito.anyInt())).thenReturn(1);

    		int retorno = VotoBusiness.deleteVoto(1);

    		assertEquals(1, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallAnotherMethodThatThrowException() throws Exception {
    		when(command.delete(Mockito.anyInt())).thenThrow(Exception.class);
    		
        	assertThrows(Exception.class, () -> VotoBusiness.deleteVoto(Mockito.anyInt()));
    	}
    	
    }

}
