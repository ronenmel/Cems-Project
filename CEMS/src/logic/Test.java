package logic;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Test implements Serializable {
	private String ID;
	private String Profession;
	private String Course;
	private int Duration;
	private String Points;

	@Override
	public String toString() {
		return "Test [ID=" + ID + ", Profession=" + Profession + ", Course=" + Course + ", Duration=" + Duration
				+ ", Points=" + Points + "]";
	}

	public Test(String iD, String profession, String course, int duration, String points) {

		ID = iD;
		Profession = profession;
		Course = course;
		Duration = duration;
		Points = points;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setProfession(String profession) {
		Profession = profession;
	}

	public void setCourse(String course) {
		Course = course;
	}
	public void setDuration(int duration) {
		Duration = duration;
	}

	public void setPoints(String points) {
		Points = points;
	}

	public String getID() {
		return ID;
	}

	public String getProfession() {
		return Profession;
	}

	public int getDuration() {
		return Duration;
	}

	public String getPoints() {
		return Points;
	}

	public String getCourse() {
		return Course;
	}



}
