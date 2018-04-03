package main.atm.dataaccess;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import main.atm.dataaccess.mapper.NoteBalanceMapper;
import main.atm.domain.NoteBalance;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

public class AutomaticTellerMachineDao {
	private static Logger log = Logger.getLogger(AutomaticTellerMachineDao.class);

	private static final String NOTE_BALANCE_SELECT = 		"SELECT NOTE_TYPE, NOTE_BALANCE, UPDATED_DATE "; 
	private static final String NOTE_BALANCE_FROM = 		"FROM T_NOTE_BALANCE ";
	private static final String NOTE_BALANCE_WHERE =		"WHERE NOTE_TYPE = :NOTE_TYPE ";
	private static final String NOTE_BALANCE_ORDER =		"ORDER BY NOTE_TYPE DESC ";
	
	private static final String NOTE_BALANCE_UPDATE =		"UPDATE T_NOTE_BALANCE SET NOTE_BALANCE = :noteBalance, UPDATED_DATE = :updatedDate " +
															"WHERE NOTE_TYPE = :noteType AND NOTE_BALANCE <> :noteBalance ";
	
	public static final String NOTE_TYPE = "NOTE_TYPE";
	public static final String NOTE_BALANCE = "NOTE_BALANCE";
	public static final String UPDATED_DATE = "UPDATED_DATE";
	
	private NamedParameterJdbcOperations jdbcOperations;

	public AutomaticTellerMachineDao(NamedParameterJdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}
	
	public List<NoteBalance> getNoteBalances(String noteType) {
		List<NoteBalance> noteBalances = null;
		try{
			if(noteType != null && !noteType.equals("")){
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				namedParameters.addValue(NOTE_TYPE, noteType, Types.INTEGER);
				noteBalances = jdbcOperations.query(NOTE_BALANCE_SELECT+NOTE_BALANCE_FROM+NOTE_BALANCE_WHERE+NOTE_BALANCE_ORDER, namedParameters, new NoteBalanceMapper());
			}else{
				noteBalances = jdbcOperations.query(NOTE_BALANCE_SELECT+NOTE_BALANCE_FROM+NOTE_BALANCE_ORDER, new NoteBalanceMapper());
			}
		} catch (DataAccessException e) {
			log.error("Unable to getNoteBalances.", e);
		}
		return noteBalances;
	}
	
	public NoteBalance updateNoteBalance(NoteBalance noteBalance) {
		Date currentDate = new Date();
		noteBalance.setUpdatedDate(currentDate);
		
		BeanPropertySqlParameterSource beanParam = new BeanPropertySqlParameterSource(noteBalance);
		
		jdbcOperations.update(NOTE_BALANCE_UPDATE, beanParam);
		
		return noteBalance;
	}
}
