package com.brilliance.base.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="note")
public class Note
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name="note_seq")
  private String noteSeq;

  @Column(name="mission")
  private String mission;

  @Column(name="nationality")
  private String nationality;

  @Column(name="reg_no")
  private String regNo;

  @Column(name="ssr_code")
  private String ssrCode;

  @Column(name="acid")
  private String acid;

  @Column(name="CALL_SIGN")
  private String callSign;

  @Column(name="model")
  private String model;

  @Column(name="person_number")
  private Integer personNumber;

  @Column(name="operator")
  private String operator;

  @Column(name="other")
  private String other;

  @Column(name="letter_unit")
  private String letterUnit;

  @Column(name="person_name")
  private String personName;

  @Column(name="tel_no")
  private String telNo;

  @Column(name="note_file_id")
  private String noteFileId;

  @Column(name="crt_time")
  private String crtTime;

  @Column(name="crt_dept")
  private String crtDept;

  @Column(name="crt_user")
  private String crtUser;

  @Column(name="UPLOAD_FILE_ID")
  private String uploadFileId;

  @Column(name="note_zip_file_id")
  private String noteZipFileId;

  @Column(name="LICENSE_NO")
  private String licenseNo;

  @Column(name="FLIGHT_BODYS")
  private String flightBodys;

  @Column(name="NOTE_NO")
  private String noteNo;

  @Column(name="PLANE_NUMBER")
  private Integer planeNumber;

  @Column(name="NOTE_STS")
  private String noteSts;

  @Transient
  private String begTime;

  @Transient
  private String endTime;

  public String getNoteSeq()
  {
    return this.noteSeq; } 
  public String getMission() { return this.mission; } 
  public String getNationality() { return this.nationality; } 
  public String getRegNo() { return this.regNo; } 
  public String getSsrCode() { return this.ssrCode; } 
  public String getAcid() { return this.acid; } 
  public String getCallSign() { return this.callSign; } 
  public String getModel() { return this.model; } 
  public Integer getPersonNumber() { return this.personNumber; } 
  public String getOperator() { return this.operator; } 
  public String getOther() { return this.other; } 
  public String getLetterUnit() { return this.letterUnit; } 
  public String getPersonName() { return this.personName; } 
  public String getTelNo() { return this.telNo; } 
  public String getNoteFileId() { return this.noteFileId; } 
  public String getCrtTime() { return this.crtTime; } 
  public String getCrtDept() { return this.crtDept; } 
  public String getCrtUser() { return this.crtUser; } 
  public String getUploadFileId() { return this.uploadFileId; } 
  public String getNoteZipFileId() { return this.noteZipFileId; } 
  public String getLicenseNo() { return this.licenseNo; } 
  public String getFlightBodys() { return this.flightBodys; } 
  public String getNoteNo() { return this.noteNo; } 
  public Integer getPlaneNumber() { return this.planeNumber; } 
  public String getNoteSts() { return this.noteSts; } 
  public String getBegTime() { return this.begTime; } 
  public String getEndTime() { return this.endTime; } 
  public void setNoteSeq(String noteSeq) { this.noteSeq = noteSeq; } 
  public void setMission(String mission) { this.mission = mission; } 
  public void setNationality(String nationality) { this.nationality = nationality; } 
  public void setRegNo(String regNo) { this.regNo = regNo; } 
  public void setSsrCode(String ssrCode) { this.ssrCode = ssrCode; } 
  public void setAcid(String acid) { this.acid = acid; } 
  public void setCallSign(String callSign) { this.callSign = callSign; } 
  public void setModel(String model) { this.model = model; } 
  public void setPersonNumber(Integer personNumber) { this.personNumber = personNumber; } 
  public void setOperator(String operator) { this.operator = operator; } 
  public void setOther(String other) { this.other = other; } 
  public void setLetterUnit(String letterUnit) { this.letterUnit = letterUnit; } 
  public void setPersonName(String personName) { this.personName = personName; } 
  public void setTelNo(String telNo) { this.telNo = telNo; } 
  public void setNoteFileId(String noteFileId) { this.noteFileId = noteFileId; } 
  public void setCrtTime(String crtTime) { this.crtTime = crtTime; } 
  public void setCrtDept(String crtDept) { this.crtDept = crtDept; } 
  public void setCrtUser(String crtUser) { this.crtUser = crtUser; } 
  public void setUploadFileId(String uploadFileId) { this.uploadFileId = uploadFileId; } 
  public void setNoteZipFileId(String noteZipFileId) { this.noteZipFileId = noteZipFileId; } 
  public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; } 
  public void setFlightBodys(String flightBodys) { this.flightBodys = flightBodys; } 
  public void setNoteNo(String noteNo) { this.noteNo = noteNo; } 
  public void setPlaneNumber(Integer planeNumber) { this.planeNumber = planeNumber; } 
  public void setNoteSts(String noteSts) { this.noteSts = noteSts; } 
  public void setBegTime(String begTime) { this.begTime = begTime; } 
  public void setEndTime(String endTime) { this.endTime = endTime; } 
  public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Note)) return false; Note other = (Note)o; if (!other.canEqual(this)) return false; Object this$noteSeq = getNoteSeq(); Object other$noteSeq = other.getNoteSeq(); if (this$noteSeq == null ? other$noteSeq != null : !this$noteSeq.equals(other$noteSeq)) return false; Object this$mission = getMission(); Object other$mission = other.getMission(); if (this$mission == null ? other$mission != null : !this$mission.equals(other$mission)) return false; Object this$nationality = getNationality(); Object other$nationality = other.getNationality(); if (this$nationality == null ? other$nationality != null : !this$nationality.equals(other$nationality)) return false; Object this$regNo = getRegNo(); Object other$regNo = other.getRegNo(); if (this$regNo == null ? other$regNo != null : !this$regNo.equals(other$regNo)) return false; Object this$ssrCode = getSsrCode(); Object other$ssrCode = other.getSsrCode(); if (this$ssrCode == null ? other$ssrCode != null : !this$ssrCode.equals(other$ssrCode)) return false; Object this$acid = getAcid(); Object other$acid = other.getAcid(); if (this$acid == null ? other$acid != null : !this$acid.equals(other$acid)) return false; Object this$callSign = getCallSign(); Object other$callSign = other.getCallSign(); if (this$callSign == null ? other$callSign != null : !this$callSign.equals(other$callSign)) return false; Object this$model = getModel(); Object other$model = other.getModel(); if (this$model == null ? other$model != null : !this$model.equals(other$model)) return false; Object this$personNumber = getPersonNumber(); Object other$personNumber = other.getPersonNumber(); if (this$personNumber == null ? other$personNumber != null : !this$personNumber.equals(other$personNumber)) return false; Object this$operator = getOperator(); Object other$operator = other.getOperator(); if (this$operator == null ? other$operator != null : !this$operator.equals(other$operator)) return false; Object this$other = getOther(); Object other$other = other.getOther(); if (this$other == null ? other$other != null : !this$other.equals(other$other)) return false; Object this$letterUnit = getLetterUnit(); Object other$letterUnit = other.getLetterUnit(); if (this$letterUnit == null ? other$letterUnit != null : !this$letterUnit.equals(other$letterUnit)) return false; Object this$personName = getPersonName(); Object other$personName = other.getPersonName(); if (this$personName == null ? other$personName != null : !this$personName.equals(other$personName)) return false; Object this$telNo = getTelNo(); Object other$telNo = other.getTelNo(); if (this$telNo == null ? other$telNo != null : !this$telNo.equals(other$telNo)) return false; Object this$noteFileId = getNoteFileId(); Object other$noteFileId = other.getNoteFileId(); if (this$noteFileId == null ? other$noteFileId != null : !this$noteFileId.equals(other$noteFileId)) return false; Object this$crtTime = getCrtTime(); Object other$crtTime = other.getCrtTime(); if (this$crtTime == null ? other$crtTime != null : !this$crtTime.equals(other$crtTime)) return false; Object this$crtDept = getCrtDept(); Object other$crtDept = other.getCrtDept(); if (this$crtDept == null ? other$crtDept != null : !this$crtDept.equals(other$crtDept)) return false; Object this$crtUser = getCrtUser(); Object other$crtUser = other.getCrtUser(); if (this$crtUser == null ? other$crtUser != null : !this$crtUser.equals(other$crtUser)) return false; Object this$uploadFileId = getUploadFileId(); Object other$uploadFileId = other.getUploadFileId(); if (this$uploadFileId == null ? other$uploadFileId != null : !this$uploadFileId.equals(other$uploadFileId)) return false; Object this$noteZipFileId = getNoteZipFileId(); Object other$noteZipFileId = other.getNoteZipFileId(); if (this$noteZipFileId == null ? other$noteZipFileId != null : !this$noteZipFileId.equals(other$noteZipFileId)) return false; Object this$licenseNo = getLicenseNo(); Object other$licenseNo = other.getLicenseNo(); if (this$licenseNo == null ? other$licenseNo != null : !this$licenseNo.equals(other$licenseNo)) return false; Object this$flightBodys = getFlightBodys(); Object other$flightBodys = other.getFlightBodys(); if (this$flightBodys == null ? other$flightBodys != null : !this$flightBodys.equals(other$flightBodys)) return false; Object this$noteNo = getNoteNo(); Object other$noteNo = other.getNoteNo(); if (this$noteNo == null ? other$noteNo != null : !this$noteNo.equals(other$noteNo)) return false; Object this$planeNumber = getPlaneNumber(); Object other$planeNumber = other.getPlaneNumber(); if (this$planeNumber == null ? other$planeNumber != null : !this$planeNumber.equals(other$planeNumber)) return false; Object this$noteSts = getNoteSts(); Object other$noteSts = other.getNoteSts(); if (this$noteSts == null ? other$noteSts != null : !this$noteSts.equals(other$noteSts)) return false; Object this$begTime = getBegTime(); Object other$begTime = other.getBegTime(); if (this$begTime == null ? other$begTime != null : !this$begTime.equals(other$begTime)) return false; Object this$endTime = getEndTime(); Object other$endTime = other.getEndTime(); return this$endTime == null ? other$endTime == null : this$endTime.equals(other$endTime); } 
  protected boolean canEqual(Object other) { return other instanceof Note; } 
  public int hashCode() { int PRIME = 59; int result = 1; Object $noteSeq = getNoteSeq(); result = result * 59 + ($noteSeq == null ? 43 : $noteSeq.hashCode()); Object $mission = getMission(); result = result * 59 + ($mission == null ? 43 : $mission.hashCode()); Object $nationality = getNationality(); result = result * 59 + ($nationality == null ? 43 : $nationality.hashCode()); Object $regNo = getRegNo(); result = result * 59 + ($regNo == null ? 43 : $regNo.hashCode()); Object $ssrCode = getSsrCode(); result = result * 59 + ($ssrCode == null ? 43 : $ssrCode.hashCode()); Object $acid = getAcid(); result = result * 59 + ($acid == null ? 43 : $acid.hashCode()); Object $callSign = getCallSign(); result = result * 59 + ($callSign == null ? 43 : $callSign.hashCode()); Object $model = getModel(); result = result * 59 + ($model == null ? 43 : $model.hashCode()); Object $personNumber = getPersonNumber(); result = result * 59 + ($personNumber == null ? 43 : $personNumber.hashCode()); Object $operator = getOperator(); result = result * 59 + ($operator == null ? 43 : $operator.hashCode()); Object $other = getOther(); result = result * 59 + ($other == null ? 43 : $other.hashCode()); Object $letterUnit = getLetterUnit(); result = result * 59 + ($letterUnit == null ? 43 : $letterUnit.hashCode()); Object $personName = getPersonName(); result = result * 59 + ($personName == null ? 43 : $personName.hashCode()); Object $telNo = getTelNo(); result = result * 59 + ($telNo == null ? 43 : $telNo.hashCode()); Object $noteFileId = getNoteFileId(); result = result * 59 + ($noteFileId == null ? 43 : $noteFileId.hashCode()); Object $crtTime = getCrtTime(); result = result * 59 + ($crtTime == null ? 43 : $crtTime.hashCode()); Object $crtDept = getCrtDept(); result = result * 59 + ($crtDept == null ? 43 : $crtDept.hashCode()); Object $crtUser = getCrtUser(); result = result * 59 + ($crtUser == null ? 43 : $crtUser.hashCode()); Object $uploadFileId = getUploadFileId(); result = result * 59 + ($uploadFileId == null ? 43 : $uploadFileId.hashCode()); Object $noteZipFileId = getNoteZipFileId(); result = result * 59 + ($noteZipFileId == null ? 43 : $noteZipFileId.hashCode()); Object $licenseNo = getLicenseNo(); result = result * 59 + ($licenseNo == null ? 43 : $licenseNo.hashCode()); Object $flightBodys = getFlightBodys(); result = result * 59 + ($flightBodys == null ? 43 : $flightBodys.hashCode()); Object $noteNo = getNoteNo(); result = result * 59 + ($noteNo == null ? 43 : $noteNo.hashCode()); Object $planeNumber = getPlaneNumber(); result = result * 59 + ($planeNumber == null ? 43 : $planeNumber.hashCode()); Object $noteSts = getNoteSts(); result = result * 59 + ($noteSts == null ? 43 : $noteSts.hashCode()); Object $begTime = getBegTime(); result = result * 59 + ($begTime == null ? 43 : $begTime.hashCode()); Object $endTime = getEndTime(); result = result * 59 + ($endTime == null ? 43 : $endTime.hashCode()); return result; } 
  public String toString() { return "Note(noteSeq=" + getNoteSeq() + ", mission=" + getMission() + ", nationality=" + getNationality() + ", regNo=" + getRegNo() + ", ssrCode=" + getSsrCode() + ", acid=" + getAcid() + ", callSign=" + getCallSign() + ", model=" + getModel() + ", personNumber=" + getPersonNumber() + ", operator=" + getOperator() + ", other=" + getOther() + ", letterUnit=" + getLetterUnit() + ", personName=" + getPersonName() + ", telNo=" + getTelNo() + ", noteFileId=" + getNoteFileId() + ", crtTime=" + getCrtTime() + ", crtDept=" + getCrtDept() + ", crtUser=" + getCrtUser() + ", uploadFileId=" + getUploadFileId() + ", noteZipFileId=" + getNoteZipFileId() + ", licenseNo=" + getLicenseNo() + ", flightBodys=" + getFlightBodys() + ", noteNo=" + getNoteNo() + ", planeNumber=" + getPlaneNumber() + ", noteSts=" + getNoteSts() + ", begTime=" + getBegTime() + ", endTime=" + getEndTime() + ")"; }

}