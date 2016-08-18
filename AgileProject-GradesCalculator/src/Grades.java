package edu.gatech.seclass.gradescalculator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Grades {
	private static Object Student = null;
	public static String GRADES_DB;
	public String formula="AT * 0.2 + AA * 0.4 + AP * 0.4";
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public Grades(String GRADES_DB) {
		super();
		this.GRADES_DB = GRADES_DB;
	}
	public Grades() {
		super();
	}
	
	public int getOverallGrade(Student student)throws ScriptException, IOException{
		Grades g=new Grades(GRADES_DB);
		int  AT= student.getAttendance();
        int AA = g.getAverageAssignmentsGrade(student);
        int AP=g.getAverageProjectsGrade(student);
        String expr=formula;        
        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
        Bindings bindings = engine.createBindings();
        bindings.put("AT", AT);
        bindings.put("AA", AA);
        bindings.put("AP",AP);
        engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        double grade=-1;
        try{
        	expr=formula;
        	grade=(double) engine.eval(expr);
        }catch(ScriptException e){
        	throw new GradeFormulaException("Unable to parse formula");
       	
        }
        return (int)Math.round(grade);
	}
	
	public static int getNumAssignments() throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));
		XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(1);
		Row row=sheet.getRow(0);
		return row.getLastCellNum()-1;
		
	}
	
	public static int getNumProjects() throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));
		XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(2);
		Row row=sheet.getRow(0);
		return row.getLastCellNum()-1;	
	}
	
	public static List getAttendance() throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));		  	XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<Double> attendanceList=new ArrayList();
		for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
			 Row row=sheet.getRow(rowNum);
			 Cell cell=row.getCell(1);
			 attendanceList.add(cell.getNumericCellValue());
		}
		
		return attendanceList;
	}
	
	public static void addAssignment(String assignmentName) throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFFont font=workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		XSSFSheet sheet = workbook.getSheetAt(1);
		Row row=sheet.getRow(0);
		Cell cell=null;
		cell=row.createCell(row.getLastCellNum());
		if(assignmentName instanceof String){
			cell.setCellValue((String)assignmentName);
			cell.setCellStyle(style);
		}
		outputFile(GRADES_DB, workbook);
	}
	public static void updateGrades(Grades grade) throws IOException{
		
	}
	public static void addGradesForAssignment(String assignmentName,HashMap<Student,Integer> grades) throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(1);
		Row row=sheet.getRow(0);
		Students stu=new Students();
		List list=stu.getGtid();		
		Cell cell=null;
		int index=100;
		for(int colNum=0;colNum<row.getLastCellNum();colNum++){
			if(row.getCell(colNum).toString().equals(assignmentName))
				index=colNum;
		}
		for(Student s:grades.keySet()){
			for(int i=0;i<list.size();i++){
				if(String.valueOf((int)(double) stu.getGtid().get(i)).equals(s.getGtid())){
					cell=sheet.getRow(i+1).createCell(index);
					cell.setCellValue(grades.get(s));
				}
			}
			
		}
		file.close();
		outputFile(GRADES_DB, workbook);
	}
	public static int getAverageAssignmentsGrade(Student s) throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(1);
		Row row=sheet.getRow(0);
		Students stu=new Students();
		List list=stu.getGtid();
		double sum=0;
		Cell cell=null;
		for(int i=0;i<list.size();i++){
			if(String.valueOf((int)(double) list.get(i)).equals(s.getGtid())){
				for(int j=1;j<row.getLastCellNum();j++){
					cell=sheet.getRow(i+1).getCell(j);
					sum+=(int)cell.getNumericCellValue();
				}
			}
		}
		int averageGrade=(int) Math.round(sum/(row.getLastCellNum()-1));
		return averageGrade;
	}
	
	public static int getAverageProjectsGrade(Student s) throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet1 = workbook.getSheetAt(2);
		Row row=sheet1.getRow(0);
		XSSFSheet sheet2 = workbook.getSheetAt(3);
		Students stu=new Students();
		Map teamList=stu.getTeamList();
		
		String teamName= teamList.get(s.getName()).toString();
		int index=0;
		for(int rowNum=0;rowNum<sheet2.getLastRowNum();rowNum++){
			if(sheet2.getRow(rowNum).getCell(0).toString().equals(teamName)){
				index=rowNum;
			}
		}
		List list=stu.getGtid();
		double sum=0;
		double product;
		Cell cell1=null;
		Cell cell2=null;
		for(int i=0;i<list.size();i++){
			if(String.valueOf((int)(double) list.get(i)).equals(s.getGtid())){
				for(int j=1;j<row.getLastCellNum();j++){
							cell1=sheet1.getRow(i+1).getCell(j);
							cell2=sheet2.getRow(index).getCell(j);
							product=cell1.getNumericCellValue()*cell2.getNumericCellValue()*0.01;
							sum+=product;					
				}
			}
		}
		int averageProjectsGrade=(int) Math.round(sum/(row.getLastCellNum()-1));
		return averageProjectsGrade;
	}
	public static void addIndividualContributions(String projectName, HashMap<Student, Integer> contributions) throws IOException{
		FileInputStream file = new FileInputStream(new File(GRADES_DB));			XSSFWorkbook workbook = new XSSFWorkbook (file);
		XSSFSheet sheet = workbook.getSheetAt(2);
		Row row=sheet.getRow(0);
		Students stu=new Students();
		List list=stu.getGtid();		
		Cell cell=null;
		int index=0;
		for(int colNum=0;colNum<row.getLastCellNum();colNum++){
			if(row.getCell(colNum).toString().equals(projectName))
				index=colNum;
		}
		for(Student s:contributions.keySet()){
			for(int i=0;i<list.size();i++){
				if(String.valueOf((int)(double) stu.getGtid().get(i)).equals(s.getGtid())){
					cell=sheet.getRow(i+1).createCell(index);
					cell.setCellValue(contributions.get(s));
				}
			}			
		}
		file.close();
		outputFile(GRADES_DB, workbook);
	}
	public static void outputFile(String GRADES_DB, XSSFWorkbook workbook)
			throws FileNotFoundException, IOException {
		FileOutputStream outFile=new FileOutputStream(new File(GRADES_DB));
		workbook.write(outFile);
		outFile.close();
	}
	
	
	public static void main(String[] args) throws IOException   {
		
		
	}
	
}
