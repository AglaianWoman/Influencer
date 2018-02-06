package com.socialbeat.influencer;

public class CampValues {

//	private String success;
//	private String message;
	private String campImg;
	private String campName;
	private String campShortNote;
	private String campCat;
	private String campLongNote;
	private String campGoal;
	private String campDos;
	private String campDont;
	private String campBacklink;
	private String campTag;
	private String campid;
	private String campApplyTill;
	private String campRewards;
	private String campRewardType;
	private String fixedamount;


	public CampValues() {
		// TODO Auto-generated constructor stub
	}

	public CampValues(String campImg, String campName, String campShortNote, String campCat, String campLongNote, String campGoal, String campDos, String campDont,
					  String campBacklink,String campTag,String campid, String campApplyTill,String campRewards,String campRewardType, String fixedamount) {
		super();
		this.campImg = campImg;
		this.campName = campName;
		this.campShortNote = campShortNote;
		this.campCat = campCat;
		this.campLongNote = campLongNote;
		this.campGoal = campGoal;
		this.campDos = campDos;
		this.campDont = campDont;
		this.campBacklink = campBacklink;
		this.campTag =campTag;
		this.campid = campid;
		this.campApplyTill = campApplyTill;
		this.campRewards = campRewards;
		this.campRewardType = campRewardType;
		this.fixedamount = fixedamount;
	}


	public String getCampImg() {
		return campImg;
	}

	public void setCampImg(String campImg) {
		this.campImg = campImg;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
	}

	public String getCampShortNote() {
		return campShortNote;
	}

	public void setCampShortNote(String campShortNote) {
		this.campShortNote = campShortNote;
	}

	public String getCampCat() {
		return campCat;
	}

	public void setCampCat(String campCat) {
		this.campCat = campCat;
	}

	public String getCampLongNote() {
		return campLongNote;
	}

	public void setCampLongNote(String campLongNote) {
		this.campLongNote = campLongNote;
	}

	public String getCampGoal() {
		return campGoal;
	}

	public void setCampGoal(String campGoal) {
		this.campGoal = campGoal;
	}

	public String getCampDos() {
		return campDos;
	}

	public void setCampDos(String campDos) {this.campDos = campDos;}

	public String getCampDont() {
		return campDont;
	}

	public void setCampDont(String campDont) {
		this.campDont = campDont;
	}

	public String getCampBacklink() {
		return campBacklink;
	}

	public void setCampBacklink(String campBacklink) {this.campBacklink = campBacklink;}

	public String getCampTag() {
		return campTag;
	}

	public void setCampTag(String campTag) {
		this.campTag = campTag;
	}

	public String getCampid() {
		return campid;
	}

	public void setCampid(String campid) {
		this.campid = campid;
	}

	public String getCampApplyTill() {
		return campApplyTill;
	}

	public void setCampApplyTill(String campApplyTill) {
		this.campApplyTill = campApplyTill;
	}

	public String getCampRewards() {
		return campRewards;
	}

	public void setCampRewards(String campRewards) {
		this.campRewards = campRewards;
	}

	public String getCampRewardType() {
		return campRewardType;
	}

	public void setCampRewardType(String campRewardType) {
		this.campRewardType = campRewardType;
	}

	public String getFixedamount() {
		return fixedamount;
	}

	public void setFixedamount(String fixedamount) {this.fixedamount = fixedamount;}
}
