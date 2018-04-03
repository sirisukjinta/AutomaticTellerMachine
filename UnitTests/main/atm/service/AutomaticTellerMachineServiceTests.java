package main.atm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.atm.dataaccess.AutomaticTellerMachineDao;
import main.atm.domain.NoteBalance;
import main.atm.model.NoteBalanceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AutomaticTellerMachineServiceTests {
	@InjectMocks
	private AutomaticTellerMachineService service;
	
	@Mock
	private AutomaticTellerMachineDao mockAutomaticTellerMachineDao;
	
	@Mock
	private DozerMapperService mockMapperService;
	
	@Before
	public void setup() throws Exception {
		mockAutomaticTellerMachineDao = mock(AutomaticTellerMachineDao.class);
		
		service = new AutomaticTellerMachineService(mockAutomaticTellerMachineDao);
		
		MockitoAnnotations.initMocks(this);	//injects mock
	}
	
	@Test
	public void testGetNoteBalances() throws Throwable {
		// arrange
		Integer noteType = 1000;
		Integer noteBalance = 100;
		Date updatedDate = new Date();
		
		List<NoteBalanceModel> expectedList = new ArrayList<NoteBalanceModel>();
		NoteBalanceModel expected = new NoteBalanceModel();
		expected.setNoteType(noteType.toString());
		expected.setNoteBalance(noteBalance.toString());
		expected.setUpdatedDate(updatedDate.toString());
		expectedList.add(expected);
		
		List<NoteBalance> dataList = new ArrayList<NoteBalance>();
		NoteBalance data = new NoteBalance();
		data.setNoteType(noteType);
		data.setNoteBalance(noteBalance);
		data.setUpdatedDate(updatedDate);
		dataList.add(data);
				
		when(mockAutomaticTellerMachineDao.getNoteBalances(anyString())).thenReturn(dataList);
		
		when(mockMapperService.mapObject(any(NoteBalance.class), any())).thenReturn(expected);
		
		// act
		List<NoteBalanceModel> actualList = this.service.getNoteBalances("");
		
		// assert
		assertNotNull(actualList);
		assertEquals(actualList.get(0).getNoteType(), expectedList.get(0).getNoteType());
		assertEquals(actualList.get(0).getNoteBalance(), expectedList.get(0).getNoteBalance());
		assertEquals(actualList.get(0).getUpdatedDate(), expectedList.get(0).getUpdatedDate());
	}
	
	@Test
	public void testUpdateNoteBalance() {
		Integer noteType = 1000;
		Integer noteBalance = 90;
		Date updatedDate = new Date();
		
		NoteBalanceModel expected = new NoteBalanceModel();
		expected.setNoteType(noteType.toString());
		expected.setNoteBalance(noteBalance.toString());
		expected.setUpdatedDate(updatedDate.toString());
		
		NoteBalance data = new NoteBalance();
		data.setNoteType(noteType);
		data.setNoteBalance(noteBalance);
		data.setUpdatedDate(updatedDate);

		noteType = 1000;
		noteBalance = 100;
		
		List<NoteBalance> dataList = new ArrayList<NoteBalance>();
		NoteBalance balance = new NoteBalance();
		balance.setNoteType(noteType);
		balance.setNoteBalance(noteBalance);
		balance.setUpdatedDate(updatedDate);
		dataList.add(balance);
				
		when(mockAutomaticTellerMachineDao.getNoteBalances(anyString())).thenReturn(dataList);
		
		when(mockMapperService.mapObject(any(), any())).thenReturn(data).thenReturn(expected);
		
		when(mockAutomaticTellerMachineDao.updateNoteBalance(any(NoteBalance.class))).thenReturn(data);
		
		noteType = 1000;
		noteBalance = 10;
		
		NoteBalanceModel param = new NoteBalanceModel();
		param.setNoteType(noteType.toString());
		param.setNoteBalance(noteBalance.toString());
		param.setUpdatedDate(updatedDate.toString());
		
		// act
		NoteBalanceModel actual = service.updateNoteBalance(param);
		
		// assert
		assertEquals(actual.getNoteType(), expected.getNoteType());
		assertEquals(actual.getNoteBalance(), expected.getNoteBalance());
		assertEquals(actual.getUpdatedDate(), expected.getUpdatedDate());
	}
	
}
