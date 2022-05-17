package br.com.southsystem.voto.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.mockito.stubbing.Answer;

import br.com.southsystem.voto.bean.PautaBean;
import br.com.southsystem.voto.command.CrudCommand;
import br.com.southsystem.voto.connection.db.BaseDAO;
import br.com.southsystem.voto.dao.sql.DeleteSql;
import br.com.southsystem.voto.dao.sql.InsertSql;
import br.com.southsystem.voto.dao.sql.SelectSql;
import br.com.southsystem.voto.dao.sql.UpdateSql;
import br.com.southsystem.voto.dto.PautaDto;

public class PautaDAOTests {

	private static MockedStatic<BaseDAO> baseDAOmock;
	private static CrudCommand<PautaBean, PautaDto> command = new CrudCommand<PautaBean, PautaDto>(new PautaDAOJdbc());
	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;
	private static PautaDto pautaDto = new PautaDto();
	private int retorno = 0;
	private static final int ID = 1;
	
	@BeforeAll
	public static void init() throws SQLException {
		baseDAOmock = Mockito.mockStatic(BaseDAO.class);
		
		pautaDto.setId(1);
		pautaDto.setDescricao("Gustavo deve se juntar a equipe?");
		pautaDto.setQtdVotosPositivos(100);
		pautaDto.setQtdVotosNegativos(0);
		pautaDto.setResultado("Maioria optou por SIM");
	}
	
	@AfterAll
	public static void close() {
		baseDAOmock.close();
	}
	
	@Nested
    @DisplayName("Testes do Create")
    class WhenCreate {
		
		@BeforeEach
		public void setup() throws SQLException {
			rs = Mockito.mock(ResultSet.class);
			Mockito.when(rs.next()).thenReturn(true);
			Mockito.when(rs.getInt(1)).thenReturn(ID);

			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.getGeneratedKeys()).thenReturn(rs);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(InsertSql.INSERT_PAUTA, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar um inteiro positivo, quando chamar criar pauta com Sucesso")
    	public void shouldReturnPositiveInteger_whenCallsCreate() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		baseDAOmock.when(() -> BaseDAO.commitTransaction(conn)).thenAnswer((Answer<Void>) invocation -> null);
    		
    		retorno = command.create(new PautaBean());
    		
    		assertEquals(ID, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar criar chama outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		baseDAOmock.when(() -> BaseDAO.rollbackTransaction(conn)).thenAnswer((Answer<Void>) invocation -> null);
    		
    		assertThrows(NullPointerException.class, () -> command.create(null));
    	}
	}
	
	@Nested
    @DisplayName("Testes do FindById")
    class WhenFindById {
		
		@BeforeEach
		public void setup() throws SQLException {
			rs = Mockito.mock(ResultSet.class);
			Mockito.when(rs.next()).thenReturn(true);
			Mockito.when(rs.getInt("pauta_id")).thenReturn(ID);
			Mockito.when(rs.getString("pauta_descricao")).thenReturn("Gustavo deve se juntar a equipe?");
			Mockito.when(rs.getInt("pauta_qtd_votos_negativos")).thenReturn(0);
			Mockito.when(rs.getInt("pauta_qtd_votos_positivos")).thenReturn(100);

			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.executeQuery()).thenReturn(rs);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(SelectSql.SELECT_PAUTA_BY_ID)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar um PautaDto, quando chamar busca por Id")
    	public void shouldReturnPautaDto_whenCallsFindById() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		PautaDto retorno = command.findById(ID);
    		
    		assertTrue(pautaDto.equals(retorno));
    		assertEquals(pautaDto, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		Mockito.when(ps.executeQuery()).thenReturn(null);
    		
    		assertThrows(NullPointerException.class, () -> command.findById(0));
    	}
	}
	
	@Nested
    @DisplayName("Testes do FindAll")
    class WhenFindAll {
		
		@BeforeEach
		public void setup() throws SQLException {
			rs = Mockito.mock(ResultSet.class);
			Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
			Mockito.when(rs.getInt("pauta_id")).thenReturn(ID);
			Mockito.when(rs.getString("pauta_descricao")).thenReturn("Gustavo deve se juntar a equipe?");
			Mockito.when(rs.getInt("pauta_qtd_votos_negativos")).thenReturn(0);
			Mockito.when(rs.getInt("pauta_qtd_votos_positivos")).thenReturn(100);

			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.executeQuery()).thenReturn(rs);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(SelectSql.SELECT_ALL_PAUTAS)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar uma Lista de PautaDto, quando chamar busca todos")
    	public void shouldReturnPautaDtoList_whenCallsFindAll() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		List<PautaDto> retorno = command.findAll();
    		
    		assertEquals(List.of(pautaDto), retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		Mockito.when(ps.executeQuery()).thenReturn(null);
    		
    		assertThrows(NullPointerException.class, () -> command.findAll());
    	}
	}
	
	@Nested
    @DisplayName("Testes do Update")
    class WhenUpdate {
		
		@BeforeEach
		public void setup() throws SQLException {
			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.executeUpdate()).thenReturn(1);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(UpdateSql.UPDATE_PAUTA)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar atualizar com sucesso")
    	public void shouldReturn1_whenCallsUpdateWithSuccess() throws Exception {
    		PautaBean pautaBean = new PautaBean();
    		pautaBean.setQtdVotosPositivos(100);
    		pautaBean.setQtdVotosNegativos(0);
    		
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		baseDAOmock.when(() -> BaseDAO.commitTransaction(conn)).thenAnswer((Answer<Void>) invocation -> null);
    		
    		retorno = command.update(pautaBean);
    		
    		assertEquals(ID, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		baseDAOmock.when(() -> BaseDAO.rollbackTransaction(conn)).thenAnswer((Answer<Void>) invocation -> null);
    		
    		assertThrows(NullPointerException.class, () -> command.update(null));
    	}
	}
	
	@Nested
    @DisplayName("Testes do Delete")
    class WhenDelete {
		
		@BeforeEach
		public void setup() throws SQLException {
			ps = Mockito.mock(PreparedStatement.class);
			Mockito.when(ps.executeUpdate()).thenReturn(1);

			conn = Mockito.mock(Connection.class);
			Mockito.when(conn.prepareStatement(DeleteSql.DELETE_PAUTA)).thenReturn(ps);
		}
		
    	@Test
    	@DisplayName("Deve retornar 1, quando chamar deletar com sucesso")
    	public void shouldReturn1_whenCallsDeleteWithSuccess() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(conn);

    		baseDAOmock.when(() -> BaseDAO.commitTransaction(conn)).thenAnswer((Answer<Void>) invocation -> null);
    		
    		retorno = command.delete(ID);
    		
    		assertEquals(ID, retorno);
    	}
    	
    	@Test
    	@DisplayName("Deve lançar uma esseção, quando chamar outro método que lançou uma esseção")
    	public void shouldThrowsException_whenCallsAnotherMethodThatThrowException() throws Exception {
    		baseDAOmock.when(() -> BaseDAO.getConnection()).thenReturn(null);

    		baseDAOmock.when(() -> BaseDAO.rollbackTransaction(conn)).thenAnswer((Answer<Void>) invocation -> null);
    		
    		assertThrows(NullPointerException.class, () -> command.delete(ID));
    	}
	}
}
