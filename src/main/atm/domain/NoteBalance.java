package main.atm.domain;

import java.util.Date;

public class NoteBalance {
	
	private Integer noteType;
	private Integer noteBalance;
	private Date updatedDate;
	
	public Integer getNoteType() {
		return noteType;
	}
	public void setNoteType(Integer noteType) {
		this.noteType = noteType;
	}
	public Integer getNoteBalance() {
		return noteBalance;
	}
	public void setNoteBalance(Integer noteBalance) {
		this.noteBalance = noteBalance;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}
