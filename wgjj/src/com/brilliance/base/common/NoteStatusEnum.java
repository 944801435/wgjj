package com.brilliance.base.common;

public enum NoteStatusEnum {
	//状态，1：待申请 2：已申请(提交给大队和民航) 3:审核中   4：批准  5：驳回 
	NOTE_STATUS_DSQ(1),  
	NOTE_STATUS_YSQ(2), 
	NOTE_STATUS_SHZ(3),  
	NOTE_STATUS_PZ(4), 
	NOTE_STATUS_BH(5);  
    
  
    private Integer noteStatus;
    
    private NoteStatusEnum(int noteStatus) {
        this.noteStatus = noteStatus;
    }
    public Integer getNoteStatus() {
        return noteStatus;
    }
}
