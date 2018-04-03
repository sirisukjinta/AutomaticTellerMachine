package main.atm.model;

public class NoteBalanceModel {

	private String noteType;
	private String noteBalance;
	private String updatedDate;
	
	public String getNoteType() {
		return noteType;
	}
	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
	public String getNoteBalance() {
		return noteBalance;
	}
	public void setNoteBalance(String noteBalance) {
		this.noteBalance = noteBalance;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
