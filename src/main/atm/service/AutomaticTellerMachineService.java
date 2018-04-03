package main.atm.service;

import java.util.ArrayList;
import java.util.List;

import main.atm.dataaccess.AutomaticTellerMachineDao;
import main.atm.domain.NoteBalance;
import main.atm.model.NoteBalanceModel;

import org.springframework.beans.factory.annotation.Autowired;

public class AutomaticTellerMachineService {

	private AutomaticTellerMachineDao automaticTellerMachineDao;
	
	public AutomaticTellerMachineService(AutomaticTellerMachineDao automaticTellerMachineDao) {
		this.automaticTellerMachineDao = automaticTellerMachineDao;
	}
	
	@Autowired
	public DozerMapperService dozerMapperService;
	
	public List<NoteBalanceModel> getNoteBalances(String noteType) {
		List<NoteBalance> noteBalanceList = this.automaticTellerMachineDao.getNoteBalances(noteType);
		List<NoteBalanceModel> noteBalanceModelList = new ArrayList<NoteBalanceModel>();
		if(noteBalanceList != null && noteBalanceList.size() != 0){
			for(NoteBalance noteBalance : noteBalanceList){
				NoteBalanceModel noteBalanceModel = new NoteBalanceModel();
				noteBalanceModel = this.dozerMapperService.mapObject(noteBalance, NoteBalanceModel.class);
				noteBalanceModelList.add(noteBalanceModel);
			}
		}
		return noteBalanceModelList;
	}
	
	public NoteBalanceModel updateNoteBalance(NoteBalanceModel paramModel) {
		NoteBalance param = new NoteBalance();
		param = this.dozerMapperService.mapObject(paramModel, NoteBalance.class);
				
		List<NoteBalance> noteBalanceList = this.automaticTellerMachineDao.getNoteBalances(paramModel.getNoteType());
		NoteBalance noteBalance = null;
		if(noteBalanceList != null && noteBalanceList.size() != 0){
			noteBalance = noteBalanceList.get(0);
			if(noteBalance != null){
				param.setNoteBalance(noteBalance.getNoteBalance() - param.getNoteBalance());
			}
		}
		
		NoteBalance noteBalanceReturn = this.automaticTellerMachineDao.updateNoteBalance(param);
		
		NoteBalanceModel noteBalanceModelReturn = null;
		if(noteBalanceReturn != null) {
			noteBalanceModelReturn = new NoteBalanceModel();
			noteBalanceModelReturn = this.dozerMapperService.mapObject(noteBalanceReturn, NoteBalanceModel.class);
		}
		
		return noteBalanceModelReturn;
	}
}
