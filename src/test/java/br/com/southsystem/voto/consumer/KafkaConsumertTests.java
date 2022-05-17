package br.com.southsystem.voto.consumer;

import javax.mail.Transport;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import br.com.southsystem.voto.business.AssembleiaBusiness;
import br.com.southsystem.voto.business.PautaBusiness;
import br.com.southsystem.voto.business.VotoBusiness;
import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoConsultDto;

public class KafkaConsumertTests {

    private static MockedStatic<AssembleiaBusiness> assembleiaBusinessMock = Mockito.mockStatic(AssembleiaBusiness.class);
    private static MockedStatic<PautaBusiness> pautaBusinessMock = Mockito.mockStatic(PautaBusiness.class);
    private static MockedStatic<VotoBusiness> votoBusinessMock = Mockito.mockStatic(VotoBusiness.class);
    private static MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class);
    private static KafkaConsumer consumer = new KafkaConsumer();
    private static VotoConsultDto votoConsultDto = new VotoConsultDto();
    private static PautaDto pautaDto = new PautaDto();
    
    @BeforeAll
    public static void setup() {
    	votoConsultDto.setAssociadoEmail("gu.moraes@gmail.com");
    }
    
    @AfterAll
    public static void close() {
    	assembleiaBusinessMock.close();
    	pautaBusinessMock.close();
    	votoBusinessMock.close();
    	transportMock.close();
    }
    
	@Test
	@DisplayName("Quando consumir a primeira mensagem")
	public void whenConsumeTheFirstMessage() throws Exception {
		pautaDto.setResultado("Ainda não houve votos.");
		
		pautaBusinessMock.when(() -> PautaBusiness.findPautaById(Mockito.anyInt())).thenReturn(pautaDto);
		votoBusinessMock.when(() -> VotoBusiness.updateVoto(Mockito.any(), Mockito.anyInt())).thenReturn(1);
		assembleiaBusinessMock.when(() -> AssembleiaBusiness.getQtdDeVotos(false)).thenReturn(1);
		assembleiaBusinessMock.when(() -> AssembleiaBusiness.getQtdDeVotos(true)).thenReturn(99);
		pautaBusinessMock.when(() -> PautaBusiness.updatePauta(Mockito.any(), Mockito.anyInt())).thenReturn(1);
		pautaBusinessMock.when(() -> PautaBusiness.findPautaById(Mockito.anyInt())).thenReturn(pautaDto);
		votoBusinessMock.when(() -> VotoBusiness.findVotoById(Mockito.anyInt())).thenReturn(votoConsultDto);
		transportMock.when(() -> Transport.send(Mockito.any())).thenAnswer((Answer<Void>) invocation -> null);

		Assertions.assertDoesNotThrow(() -> consumer.listener("{\"id\":1,\"pautaId\":1,\"associadoId\":1}"));
		Assertions.assertTrue(consumer.listener("{\"id\":1,\"pautaId\":1,\"associadoId\":1}"));
	}
	
	@Test
	@DisplayName("Quando consumir a segunda mensagem em diante")
	public void whenConsumeTheSecondMessageOnwards() throws Exception {
    	pautaDto.setResultado("EMPATE");
		
		pautaBusinessMock.when(() -> PautaBusiness.findPautaById(Mockito.anyInt())).thenReturn(pautaDto);
		votoBusinessMock.when(() -> VotoBusiness.findVotoById(Mockito.anyInt())).thenReturn(votoConsultDto);
		transportMock.when(() -> Transport.send(Mockito.any())).thenAnswer((Answer<Void>) invocation -> null);

		Assertions.assertDoesNotThrow(() -> consumer.listener("{\"id\":1,\"pautaId\":1,\"associadoId\":1}"));
		Assertions.assertTrue(consumer.listener("{\"id\":1,\"pautaId\":1,\"associadoId\":1}"));
	}
	
	@Test
	@DisplayName("Quando lançar uma esseção ao consumir um mensagem")
	public void whenThrowsExceptionConsumeAnMessage() throws Exception {
		Assertions.assertFalse(consumer.listener(null));
	}
}
