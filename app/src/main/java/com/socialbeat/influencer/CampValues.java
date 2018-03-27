package com.socialbeat.influencer;

public class CampValues {

	private String campid;
	private String campName;
	private String campImg;
	private String campCat;
	private String campShortNote;

	public CampValues() {
		// TODO Auto-generated constructor stub
	}

	public CampValues(String campid, String campName, String campCat, String campImg, String campShortNote ) {
		super();

		this.campid = campid;
		this.campName = campName;
		this.campImg = campImg;
		this.campCat = campCat;
		this.campShortNote = campShortNote;

	}

	public String getCampid() {
		return campid;
	}

	public void setCampid(String campid) {
		this.campid = campid;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
	}

	public String getCampImg() {
		return campImg;
	}

	public void setCampImg(String campImg) {
		this.campImg = campImg;
	}

	public String getCampCat() {
		return campCat;
	}

	public void setCampCat(String campCat) {
		this.campCat = campCat;
	}

	public String getCampShortNote() {
		return campShortNote;
	}

	public void setCampShortNote(String campShortNote) {
		this.campShortNote = campShortNote;
	}

}
