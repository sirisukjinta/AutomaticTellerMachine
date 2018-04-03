package main.atm.presentation.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.atm.model.AjaxResponse;
import main.atm.model.DispenseModel;
import main.atm.model.NoteBalanceModel;
import main.atm.service.AutomaticTellerMachineService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;

public class AutomaticTellerMachineControllerTests {
	@InjectMocks
	private AutomaticTellerMachineController controller;
	
	@Mock
	AutomaticTellerMachineService mockAutomaticTellerMachineService;
	@Mock
	HttpServletRequest mockRequest;
	@Mock
	HttpServletResponse mockResponse;
	@Mock
	BindingResult mockBindingResult;
	@Mock
	HttpSession mockSession;
	
	@Before
	public void setUp() throws Exception {
		this.controller = new AutomaticTellerMachineController();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testDispensePost() throws Exception {
		// arrange
		AjaxResponse expected = new AjaxResponse();
		expected.setStatus(AjaxResponse.STATUS_SUCCESS);
		expected.setResult(null);

		Integer amount = 10000;
		
		DispenseModel data = new DispenseModel();
		data.setAmount(amount.toString());

		Integer noteType = 1000;
		Integer noteBalance = 100;
		Date updatedDate = new Date();

		List<NoteBalanceModel> noteBalanceModelList = new ArrayList<NoteBalanceModel>();
		NoteBalanceModel noteBalanceModel = new NoteBalanceModel();
		noteBalanceModel.setNoteType(noteType.toString());
		noteBalanceModel.setNoteBalance(noteBalance.toString());
		noteBalanceModel.setUpdatedDate(updatedDate.toString());
		noteBalanceModelList.add(noteBalanceModel);

		noteType = 500;
		noteBalance = 100;
		updatedDate = new Date();
		
		noteBalanceModel = new NoteBalanceModel();
		noteBalanceModel.setNoteType(noteType.toString());
		noteBalanceModel.setNoteBalance(noteBalance.toString());
		noteBalanceModel.setUpdatedDate(updatedDate.toString());
		noteBalanceModelList.add(noteBalanceModel);

		noteType = 100;
		noteBalance = 100;
		updatedDate = new Date();
		
		noteBalanceModel = new NoteBalanceModel();
		noteBalanceModel.setNoteType(noteType.toString());
		noteBalanceModel.setNoteBalance(noteBalance.toString());
		noteBalanceModel.setUpdatedDate(updatedDate.toString());
		noteBalanceModelList.add(noteBalanceModel);

		noteType = 50;
		noteBalance = 100;
		updatedDate = new Date();
		
		noteBalanceModel = new NoteBalanceModel();
		noteBalanceModel.setNoteType(noteType.toString());
		noteBalanceModel.setNoteBalance(noteBalance.toString());
		noteBalanceModel.setUpdatedDate(updatedDate.toString());
		noteBalanceModelList.add(noteBalanceModel);

		noteType = 20;
		noteBalance = 100;
		updatedDate = new Date();
		
		noteBalanceModel = new NoteBalanceModel();
		noteBalanceModel.setNoteType(noteType.toString());
		noteBalanceModel.setNoteBalance(noteBalance.toString());
		noteBalanceModel.setUpdatedDate(updatedDate.toString());
		noteBalanceModelList.add(noteBalanceModel);
		
		noteType = 1000;
		noteBalance = 90;
		updatedDate = new Date();
		
		NoteBalanceModel noteBalanceModelReturn = new NoteBalanceModel();
		noteBalanceModelReturn.setNoteType(noteType.toString());
		noteBalanceModelReturn.setNoteBalance(noteBalance.toString());
		noteBalanceModelReturn.setUpdatedDate(updatedDate.toString());

		when(mockAutomaticTellerMachineService.getNoteBalances(anyString())).thenReturn(noteBalanceModelList);
		
		when(mockAutomaticTellerMachineService.updateNoteBalance(any(NoteBalanceModel.class))).thenReturn(noteBalanceModelReturn);
				
		// act
		AjaxResponse actual = this.controller.dispense(data, mockBindingResult, mockRequest, mockResponse);
		
		// assert
		assertEquals(expected.getStatus(), actual.getStatus());
		assertEquals(expected.getResult(), actual.getResult());
	}
}


