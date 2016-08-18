package edu.gatech.seclass.gradescalculator;

import org.apache.poi.ss.usermodel.Cell;

public class Student {
	public  String name;
	public  String gtid;
	public  int attendance;
	public  String team;
	public String email;
	public Course course;
	public int AA;
	public int AP;
	
	
	public int getAA() {
		return AA;
	}
	public void setAA(int aA) {
		AA = aA;
	}
	public int getAP() {
		return AP;
	}
	public void setAP(int aP) {
		AP = aP;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Student(String name, String gtid, Course course) {
		super();
		this.name = name;
		this.gtid = gtid;
		this.course = course;
	}
	
	public Student(String name, String gtid) {
		super();
		this.name = name;
		this.gtid = gtid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String cell) {
		this.name = cell;
	}
	public String getGtid() {
		return gtid;
	}
	public void setGtid(String d) {
		this.gtid = d;
	}
	public int getAttendance() {
		return attendance;
	}
	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public Student() {
		super();
	}

}
