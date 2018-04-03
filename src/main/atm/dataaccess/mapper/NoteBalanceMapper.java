package main.atm.dataaccess.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.atm.dataaccess.AutomaticTellerMachineDao;
import main.atm.domain.NoteBalance;
import main.atm.util.MapperUtility;

import org.springframework.jdbc.core.RowMapper;

public class NoteBalanceMapper implements RowMapper<NoteBalance> {

	@Override
	public NoteBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
		NoteBalance automaticTellerMachine = new NoteBalance();
		
		if(MapperUtility.hasColumn(rs, AutomaticTellerMachineDao.NOTE_TYPE)) automaticTellerMachine.setNoteType(rs.getInt(AutomaticTellerMachineDao.NOTE_TYPE));
		if(MapperUtility.hasColumn(rs, AutomaticTellerMachineDao.NOTE_BALANCE)) automaticTellerMachine.setNoteBalance(rs.getInt(AutomaticTellerMachineDao.NOTE_BALANCE));
		if(MapperUtility.hasColumn(rs, AutomaticTellerMachineDao.UPDATED_DATE)) automaticTellerMachine.setUpdatedDate(rs.getDate(AutomaticTellerMachineDao.UPDATED_DATE));

		return automaticTellerMachine;
	}

}
