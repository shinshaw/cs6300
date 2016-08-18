package edu.gatech.seclass.gradescalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.script.ScriptException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Course {
	private static final String String = null;
	static Students students;
    static Grades grades; 
    String formula;
    
    	

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	
	public Course(Students students, Grades grades) {
		super();
		this.students = students;
		this.grades = grades;
	}

	public static int getNumStudents() throws IOException{
		return students.getNumStudents();
	}
	public static int getNumAssignments() throws IOException{
		return grades.getNumAssignments();
		
	}
	public static int getNumProjects() throws IOException{
		return grades.getNumProjects();
	}
	

	public static HashSet<Student> getStudents() throws IOException{
		return students.getStudents();
	}
	public static Student getStudentByName(String name) throws IOException{
		return students.getStudentByName(name);
	}
	public static int  getAttendance(Student student) throws IOException{
		return students.getAttendance(student);
	}
	public static String  getTeam(Student student) throws IOException{
		return students.getTeam(student);
	}
	
	public static Student getStudentByID(String gtid) throws IOException{
		return students.getStudentByID(gtid);
	}
	public static void addAssignment(String assignmentName) throws IOException{
		
		grades.addAssignment(assignmentName);
	}
	public static void updateGrades(Grades grade) throws IOException{
		
		grades.updateGrades(grade);
	}
	public static void addGradesForAssignment(String assignmentName, HashMap grade) throws IOException{
		
		 grades.addGradesForAssignment(assignmentName, grade);
	}
	public static int getAverageAssignmentsGrade(Student s) throws IOException{
		return grades.getAverageAssignmentsGrade(s);
	}
	public static int getAverageProjectsGrade(Student s) throws IOException{
		return grades.getAverageProjectsGrade(s);
	}
	
	public static void addIndividualContributions(String projectName, HashMap<Student, Integer>contributions) throws IOException{
		grades.addIndividualContributions(projectName,contributions);
	}
			
	

	public static int getOverallGrade(Student student) throws GradeFormulaException, ScriptException, IOException{
		return grades.getOverallGrade(student);
	}

	public static String getEmail(Student student) throws IOException {		
		return students.getEmail(student);
	}

	
}
