package edu.gatech.seclass.gradescalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Students {
	public static String STUDENTS_DB;
	public Students(String STUDENTS_DB) {
		super();
		this.STUDENTS_DB= STUDENTS_DB;
	}
	
	public Students() {
		super();
	}
		
	public static List getStudentsList() throws IOException{
		FileInputStream file=new FileInputStream(new File(STUDENTS_DB));
		XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<String> studentsList=new ArrayList();
		for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
			 Row row=sheet.getRow(rowNum);
			 Cell cell=row.getCell(0);			 
			 studentsList.add(cell.getStringCellValue());
		}
		
		return studentsList;
	}
	
	public static int getNumStudents() throws IOException{
		Students s=new Students();
		List studentList=s.getStudentsList();
		return studentList.size();
	}
	public static List getGtid() throws IOException{
		FileInputStream file=new FileInputStream(new File(STUDENTS_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<Double> gtidList=new ArrayList();
		for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
			 Row row=sheet.getRow(rowNum);
			 Cell cell=row.getCell(1);			 
			 gtidList.add(cell.getNumericCellValue());
		}
		
		return gtidList;
	}
	
	public static List getEmailList() throws IOException{
		FileInputStream file=new FileInputStream(new File(STUDENTS_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<String> emailList=new ArrayList();
		for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
			 Row row=sheet.getRow(rowNum);
			 Cell cell=row.getCell(2);			 
			 emailList.add(cell.getStringCellValue());
		}
		
		return emailList;
	}
	
	public static Map getTeamList() throws IOException{		
		FileInputStream file=new FileInputStream(new File(STUDENTS_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(1);
		Map teamList=new HashMap();
		for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
			Row row=sheet.getRow(rowNum);
			for(int cellNum=1;cellNum<row.getLastCellNum();cellNum++){
				Cell cell=row.getCell(cellNum);
				teamList.put(cell.getStringCellValue(),"Team"+" "+rowNum);
         }
		}
		
		return teamList;
	}	
			
	public static HashSet getStudents() throws IOException{
		Students s=new Students();
		Grades g=new Grades();
		int num=s.getNumStudents();
		HashSet<Student> studentsRoster=new HashSet();
		for(int i=0;i<num;i++){
			Student stu=new Student();
			stu.setName((String) s.getStudentsList().get(i));
			stu.setEmail((String)s.getEmailList().get(i));
			stu.setGtid(String.valueOf((int)(double) getGtid().get(i)));
			stu.setTeam((String)s.getTeamList().get(stu.getName()));
			stu.setAttendance((int)(double) g.getAttendance().get(i));
			studentsRoster.add(stu);		
		}
		return studentsRoster;
	}
	public static int getAttendance(Student student) throws IOException{		
		return student.getAttendance();
		
	}
	
	public static String getTeam(Student student) throws IOException{		
		return student.getTeam();
		
	}
	public static String getEmail(Student student) throws IOException{		
		return student.getEmail();
		
	}
	public static Student getStudentByName(String name) throws IOException{
		Students s=new Students();
		HashMap map=new HashMap();
		HashSet<Student> studentsRoster=s.getStudents();
		for(Student temp:studentsRoster){
			map.put(temp.getName(), temp);
		}
		return (Student) map.get(name);
	}
	public static Student getStudentByID(String gtid) throws IOException{
		Students s=new Students();
		HashMap map=new HashMap();
		HashSet<Student> studentsRoster=s.getStudents();
		for(Student temp:studentsRoster){
			map.put(temp.getGtid(), temp);
		}
		return (Student) map.get(gtid);
	}
	
	public static void main(String[] args) {
		
		
	}
	
	
}
