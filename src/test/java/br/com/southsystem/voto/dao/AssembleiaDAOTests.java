package br.com.southsystem.voto.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import br.com.southsystem.voto.command.AssembleiaCommand;
import br.com.southsystem.voto.connection.db.BaseDAO;
import br.com.southsystem.voto.dao.sql.SelectSql;
import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoDto;

public class AssembleiaDAOTests {

	private static MockedStatic<BaseDAO> baseDAOmock;
	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;
	private static VotoDto votoDto = new VotoDto();
	private static PautaDto pautaDto = new PautaDto();
	private int retorno = 0;
	private static final int ID = 1;
	
	@BeforeAll
	public static void init() throws SQLException {
		baseDAOmock = Mockito.mockStatic(BaseDAO.class);
		
		votoDto.setId(ID);
		votoDto.setAssociadoId(ID);
		votoDto.setPautaId(ID);
		
		pautaDto.setId(ID);
		pautaDto.setDescricao("Gustavo deve se juntar a equipe?");
	}
	
	@AfterAll
	public static void close() {
		baseDAOmock.close();
	}
		
	@Nested
    @DisplayName("Testes do FindById")
    class WhenFindById {
		
		@BeforeEach
		public void setup() throws SQLException {
			rs = Mockito.mock(ResultSet.class);
			Mockito.when(rs.next()).thenReturn(true);
			Mockito.when(rs.getInt("votos")).thenReturn(100);

			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.executeQuery()).thenReturn(rs);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(SelectSql.SELECT_VOTOS_VALORES)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar uma quantidade de votos, quando chamar busca por quantidade de votos")
    	public void shouldReturnIntegerQuantity_whenCallsGetQtdDeVotos() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		retorno = AssembleiaCommand.getQtdDeVotos(true);
    		
    		assertEquals(100, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		Mockito.when(ps.executeQuery()).thenReturn(null);
    		
    		assertThrows(NullPointerException.class, () -> AssembleiaCommand.getQtdDeVotos(false));
    	}
	}
	
	@Nested
    @DisplayName("Testes do FindAll")
    class WhenFindAll {
		
		@BeforeEach
		public void setup() throws SQLException {
			rs = Mockito.mock(ResultSet.class);
			Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
			Mockito.when(rs.getInt("voto_id")).thenReturn(ID);
			Mockito.when(rs.getInt("voto_pauta_id")).thenReturn(ID);
			Mockito.when(rs.getInt("voto_associado_id")).thenReturn(ID);

			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.executeQuery()).thenReturn(rs);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(SelectSql.SELECT_VOTOS)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar uma Lista de VotoDto, quando chamar busca todos")
    	public void shouldReturnVotoDtoList_whenCallsFindAll() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		List<VotoDto> retorno = AssembleiaCommand.getListVotos();
    		
    		assertEquals(List.of(votoDto), retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		Mockito.when(ps.executeQuery()).thenReturn(null);
    		
    		assertThrows(NullPointerException.class, () -> AssembleiaCommand.getListVotos());
    	}
	}
	
	@Nested
    @DisplayName("Testes do FindOpenPautaById")
    class WhenFindOpenPautaById {
		
		@BeforeEach
		public void setup() throws SQLException {
			rs = Mockito.mock(ResultSet.class);
			Mockito.when(rs.next()).thenReturn(true);
			Mockito.when(rs.getInt("pauta_id")).thenReturn(ID);
			Mockito.when(rs.getString("pauta_descricao")).thenReturn("Gustavo deve se juntar a equipe?");

			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.executeQuery()).thenReturn(rs);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(SelectSql.SELECT_OPEN_PAUTA_BY_ID)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar um PautaDto, quando chamar busca pauta aberta por Id")
    	public void shouldReturnPautaDto_whenCallsFindOpenPautaById() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		PautaDto retorno = AssembleiaCommand.findOpenPautaById(ID);
    		
    		assertEquals(pautaDto, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		Mockito.when(ps.executeQuery()).thenReturn(null);
    		
    		assertThrows(NullPointerException.class, () -> AssembleiaCommand.findOpenPautaById(ID));
    	}
	}
}
