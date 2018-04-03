package main.atm.presentation.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.atm.model.AjaxResponse;
import main.atm.model.DispenseModel;
import main.atm.model.NoteBalanceModel;
import main.atm.service.AutomaticTellerMachineService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class AutomaticTellerMachineController {
	private static Logger Log = Logger.getLogger(AutomaticTellerMachineController.class);
	private AutomaticTellerMachineService automaticTellerMachineService;

	int[] POSSIBLE_NOTES = {1000, 500, 100, 50, 20};
	int[] IMPOSSIBLE_VALUES = new int[]{10, 30};
	boolean FINISHED = false;
	
	@Autowired
	void setAutomaticTellerMachineService(AutomaticTellerMachineService automaticTellerMachineService){
		this.automaticTellerMachineService = automaticTellerMachineService;
	}
	
	@RequestMapping(value = {"", "/", "/dispense"}, method = RequestMethod.GET)
	public ModelAndView dispense(HttpServletRequest request) throws Exception{
		Log.debug("In dispense");

		DispenseModel dispenseModel = new DispenseModel();
		ModelAndView modelAndView = new ModelAndView("atm/dispense", "dispenseEntry", dispenseModel);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/dispense", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResponse dispense(@ModelAttribute("dispenseEntry") DispenseModel dispenseEntry, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		Log.debug("In dispense post");
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		String errorMsg = "";
		
		try {
			ajaxResponse = validateDispense(dispenseEntry, result);
			
			if(AjaxResponse.STATUS_ERROR.equals(ajaxResponse.getStatus())){
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else if(AjaxResponse.STATUS_SUCCESS.equals(ajaxResponse.getStatus())){
				int noteAmount = 0;
				int amountCalculation = 0;
				int amountCalculation2 = 0;
				int[] noteAmounts = new int[5];
				int i = 0;
				
				int amountInt = Integer.parseInt(dispenseEntry.getAmount());
				List<Integer> impossibleValueList = Arrays.stream(IMPOSSIBLE_VALUES).boxed().collect(Collectors.toList());
				if(impossibleValueList.contains(amountInt%100)){
					amountCalculation = amountInt - amountInt%100 - 100;
					for(int possibleNote : POSSIBLE_NOTES){
						noteAmount = getNote(possibleNote, amountCalculation, false);
						amountCalculation = amountCalculation - (possibleNote * noteAmount);
						noteAmounts[i] = noteAmount;
						i++;
					}
					
					i = 0;
					amountCalculation2 = amountInt%100 + 100;
					for(int possibleNote : POSSIBLE_NOTES){
						if(possibleNote > 50){
							i++;
							continue;
						}
						noteAmount = getNote(possibleNote, amountCalculation2, true);
						amountCalculation2 = amountCalculation2 - (possibleNote * noteAmount);
						noteAmounts[i] = noteAmounts[i] + noteAmount;
						i++;
					}
				}else{
					amountCalculation = amountInt;
					for(int possibleNote : POSSIBLE_NOTES){
						noteAmount = getNote(possibleNote, amountCalculation, false);
						amountCalculation = amountCalculation - (possibleNote * noteAmount);
						noteAmounts[i] = noteAmount;
						i++;
					}
				}
				
				if(amountCalculation != 0){
					noteAmounts = new int[5];
					advanceCalculate(POSSIBLE_NOTES, noteAmounts, amountInt, 0);
				}else{
					NoteBalanceModel noteBalanceModel = null;
					for (int j = 0; j < noteAmounts.length; j++) {
						System.out.println("Note Type : " + POSSIBLE_NOTES[j] + " Note Amount : " + noteAmounts[j]);
						noteBalanceModel = new NoteBalanceModel();
						noteBalanceModel.setNoteType(Integer.toString(POSSIBLE_NOTES[j]));
						noteBalanceModel.setNoteBalance(Integer.toString(noteAmounts[j]));
						this.automaticTellerMachineService.updateNoteBalance(noteBalanceModel);
					}
				}
			}
		} catch (Exception e){
			errorMsg = "Unknown Error." + e.getMessage();
			Log.error(errorMsg, e);
			throw e;
		}
		
		if(errorMsg.length() > 0){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ajaxResponse.setStatus(AjaxResponse.STATUS_ERROR);
			ajaxResponse.setResult(errorMsg);
		}
		
		return ajaxResponse;
	}
	
	protected AjaxResponse validateDispense(DispenseModel dispenseEntry, BindingResult result){
		AjaxResponse ajaxResponse = new AjaxResponse();
		ajaxResponse.setStatus(AjaxResponse.STATUS_SUCCESS);
		
		List<FieldError> fieldErrorList = new ArrayList<FieldError>();
		if(fieldErrorList.size() == 0 && StringUtils.isBlank(dispenseEntry.getAmount())){
			fieldErrorList.add(new FieldError("amount", "amount", "Amount is required"));
		}
		
		int amountInt = 0;
		try{
			amountInt = Integer.parseInt(dispenseEntry.getAmount());
		}catch(Exception e){
			fieldErrorList.add(new FieldError("amount", "amount", "Unable to parse amount"));
		}
		
		if(fieldErrorList.size() == 0 && amountInt < 0){
			fieldErrorList.add(new FieldError("amount", "amount", "Invalid input (negative)"));
		}
		
		if(fieldErrorList.size() == 0 && amountInt%10 != 0){
			fieldErrorList.add(new FieldError("amount", "amount", "Invalid input (coin)"));
		}
		
		int maxDispense = 20000;
		if(fieldErrorList.size() == 0 && amountInt > maxDispense){
			fieldErrorList.add(new FieldError("amount", "amount", "Invalid input (more than "+maxDispense+")"));
		}
		List<NoteBalanceModel> noteBalanceModelList = null;
		try {
			noteBalanceModelList = this.automaticTellerMachineService.getNoteBalances("");
			TreeMap<Integer, Integer> noteBalanceMap = getNoteBalanceMap(noteBalanceModelList);
			int balance = checkBalance(noteBalanceMap);
			if(amountInt > balance){
				fieldErrorList.add(new FieldError("amount", "amount", "Balance is not enough to dispense"));
			}

			List<Integer> impossibleValueList = Arrays.stream(IMPOSSIBLE_VALUES).boxed().collect(Collectors.toList());
			
			if(impossibleValueList.contains(amountInt)){
				fieldErrorList.add(new FieldError("amount", "amount", "Invalid input (not possible to dispense)"));
			}
		} catch (Exception e){
			Log.error("Exception get data from getNoteBalances ", e);
		}
		
		if(fieldErrorList.size() != 0){
			ajaxResponse.setStatus(AjaxResponse.STATUS_FAIL);
			ajaxResponse.setResult(fieldErrorList);
			return ajaxResponse;
		}

		if(result.hasErrors()){
			// this is a processing/validation error not System error.
			ajaxResponse.setStatus(AjaxResponse.STATUS_FAIL);
			ajaxResponse.setResult(result.getAllErrors());
			return ajaxResponse;
		}

		return ajaxResponse;
	}

	public TreeMap<Integer, Integer> getNoteBalanceMap(List<NoteBalanceModel> noteBalanceModelList){
		TreeMap<Integer, Integer> noteBalanceMap = new TreeMap<Integer, Integer>(Collections.reverseOrder());
		for(NoteBalanceModel noteBalanceModel : noteBalanceModelList){
			noteBalanceMap.put(Integer.parseInt(noteBalanceModel.getNoteType()), Integer.parseInt(noteBalanceModel.getNoteBalance()));
		}
		return noteBalanceMap;
	}

	public int checkBalance(TreeMap<Integer, Integer> noteBalanceMap){
		int balance = 0;
		for (HashMap.Entry<Integer, Integer> entry : noteBalanceMap.entrySet()) {
			balance += (entry.getKey() * entry.getValue());
		}
		return balance;
	}
	
	private int getNote(int noteType, int amount, boolean useOddFifty) throws Exception{
		int noteAmount = 0;
		int availableNoteAmount = 0;

		List<NoteBalanceModel> noteBalanceModelList = null;
		TreeMap<Integer, Integer> noteBalanceMap = null;
		try {
			noteBalanceModelList = this.automaticTellerMachineService.getNoteBalances("");
			noteBalanceMap = getNoteBalanceMap(noteBalanceModelList);
		} catch (Exception e){
			Log.error("Exception get data from getNoteBalances ", e);
		}
		
		try{
			if(useOddFifty && noteType == 50){
				noteAmount = Math.floorDiv(amount, noteType);
				if(noteAmount%2 == 0){
					noteAmount = noteAmount - 1;
				}
				availableNoteAmount = getAvailableNote(noteBalanceMap, noteType, noteAmount);
			}else{
				noteAmount = Math.floorDiv(amount, noteType);
				availableNoteAmount = getAvailableNote(noteBalanceMap, noteType, noteAmount);
			}
		} catch(Exception e) {
			System.out.println("Unable to trunc note, " + e);
			throw e;
		}
		
		return availableNoteAmount;
	}
	
	private int getAvailableNote(TreeMap<Integer, Integer> noteBalanceMap, int noteType, int noteAmount) throws Exception{
		int availableNoteAmount = 0;
		availableNoteAmount = noteBalanceMap.get(noteType);
		if(noteAmount <= availableNoteAmount){
			noteBalanceMap.put(noteType, (availableNoteAmount-noteAmount));
			return noteAmount;
		}else{
			return availableNoteAmount;
		}
	}

	public List<int[]> advanceCalculate(int[] possibleNotes, int[] noteAmounts, int amountInt, int position){
		List<int[]> possibleResultList = new ArrayList<>();
		int value = getSumAmount(possibleNotes, noteAmounts);
		if (value < amountInt){
			for (int i = position; i < possibleNotes.length; i++) {
				int[] noteAmountsTemp = noteAmounts.clone();
				noteAmountsTemp[i]++;
				if(!FINISHED){
					List<int[]> newList = advanceCalculate(possibleNotes, noteAmountsTemp, amountInt, i);
					if (newList != null){
						possibleResultList.addAll(newList);
					}
				}else{
					break;
				}
			}
		} else if (value == amountInt) {
			List<NoteBalanceModel> noteBalanceModelList = null;
			TreeMap<Integer, Integer> noteBalanceMap = null;
			try {
				noteBalanceModelList = this.automaticTellerMachineService.getNoteBalances("");
				noteBalanceMap = getNoteBalanceMap(noteBalanceModelList);
			} catch (Exception e){
				Log.error("Exception get data from getNoteBalances ", e);
			}
			boolean allNotesPossible = true;
			for (int i = 0; i < noteAmounts.length; i++) {
				if (noteAmounts[i] > noteBalanceMap.get(possibleNotes[i])){
					allNotesPossible = false;
				}
			}
			if(allNotesPossible){
				System.out.println("Advance Calculate");
				NoteBalanceModel noteBalanceModel = null;
				for (int i = 0; i < noteAmounts.length; i++) {
					System.out.println("Note Type : " + possibleNotes[i] + " Note Amount : " + noteAmounts[i]);
					noteBalanceModel = new NoteBalanceModel();
					noteBalanceModel.setNoteType(Integer.toString(possibleNotes[i]));
					noteBalanceModel.setNoteBalance(Integer.toString(noteAmounts[i]));
					this.automaticTellerMachineService.updateNoteBalance(noteBalanceModel);
				}
				FINISHED = true;
			}
		}
		return possibleResultList;
	}

	public int getSumAmount(int[] possibleNotes, int[] noteAmounts){
		int ret = 0;
		for (int i = 0; i < noteAmounts.length; i++) {
			ret += possibleNotes[i] * noteAmounts[i];
		}
		return ret;
	}
}
