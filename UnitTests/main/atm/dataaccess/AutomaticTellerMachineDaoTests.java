package main.atm.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.atm.dataaccess.mapper.NoteBalanceMapper;
import main.atm.domain.NoteBalance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public class AutomaticTellerMachineDaoTests {
	
	@InjectMocks
	AutomaticTellerMachineDao dao;
	
	@Mock
	NamedParameterJdbcOperations mockJdbcOperations;
	
	@Before
	public void setup(){
		this.mockJdbcOperations = mock(NamedParameterJdbcOperations.class);
		dao = new AutomaticTellerMachineDao(mockJdbcOperations);
	}
	
	@After
	public void tearDown() {
		dao = null;
	}

	@Test
	public void testGetNoteBalances() throws Throwable {
		// arrange
		Integer noteType = 1000;
		Integer noteBalance = 100;
		Date updatedDate = new Date();
		
		List<NoteBalance> expectedList = new ArrayList<NoteBalance>();
		NoteBalance expected = new NoteBalance();
		expected.setNoteType(noteType);
		expected.setNoteBalance(noteBalance);
		expected.setUpdatedDate(updatedDate);
		expectedList.add(expected);
		
		// mock
		when(this.mockJdbcOperations.query(anyString(), any(MapSqlParameterSource.class), any(NoteBalanceMapper.class))).thenReturn(expectedList);
		
		String param = "1000";
		// act
		List<NoteBalance> actualList = dao.getNoteBalances(param);
		
		// assert
		assertNotNull(actualList);
		NoteBalance actual = actualList.get(0);
		assertEquals(actual.getNoteType(), expected.getNoteType());
		assertEquals(actual.getNoteBalance(), expected.getNoteBalance());
		assertEquals(actual.getUpdatedDate(), expected.getUpdatedDate());
	}
	
	@Test
	public void testUpdateNoteBalance() throws Throwable {
		// arrange
		Integer noteType = 1000;
		Integer noteBalance = 100;
		
		NoteBalance expected = new NoteBalance();
		expected.setNoteType(noteType);
		expected.setNoteBalance(noteBalance);
		
		NoteBalance param = new NoteBalance();
		param.setNoteType(noteType);
		param.setNoteBalance(noteBalance);
		
		//mock
		when(mockJdbcOperations.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);
		
		// act
		NoteBalance actual = dao.updateNoteBalance(param);
		
		// assert
		assertEquals(actual.getNoteType(), expected.getNoteType());
		assertEquals(actual.getNoteBalance(), expected.getNoteBalance());
		
	}
}
