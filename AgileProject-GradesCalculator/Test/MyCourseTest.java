package edu.gatech.seclass.gradescalculator;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyCourseTest {
	Students students = null;
    Grades grades = null;
    Course course = null;
    static final String GRADES_DB = "DB" + File.separator + "GradesDatabase6300-grades.xlsx";
    static final String GRADES_DB_GOLDEN = "DB" + File.separator + "GradesDatabase6300-grades-goldenversion.xlsx";
    static final String STUDENTS_DB = "DB" + File.separator + "GradesDatabase6300-students.xlsx";
    static final String STUDENTS_DB_GOLDEN = "DB" + File.separator + "GradesDatabase6300-students-goldenversion.xlsx";
    
    @Before
    public void setUp() throws Exception {
        FileSystem fs = FileSystems.getDefault();
        Path gradesdbfilegolden = fs.getPath(GRADES_DB_GOLDEN);
        Path gradesdbfile = fs.getPath(GRADES_DB);
        Files.copy(gradesdbfilegolden, gradesdbfile, REPLACE_EXISTING);
        Path studentsdbfilegolden = fs.getPath(STUDENTS_DB_GOLDEN);
        Path studentsdbfile = fs.getPath(STUDENTS_DB);
        Files.copy(studentsdbfilegolden, studentsdbfile, REPLACE_EXISTING);    	
    	students = new Students(STUDENTS_DB);
        grades = new Grades(GRADES_DB);
        course = new Course(students, grades);
    }

    @After
    public void tearDown() throws Exception {
        students = null;
        grades = null;
        course = null;
    }
    @Test
    public void testAddStudents() throws IOException {
        course.addStudents("Mike Aaron");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(17, course.getNumStudents());
        course.addStudents("Allen Speer");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(18, course.getNumStudents());
    }
    @Test
    public void testAddProjects() throws IOException {
        course.addProjects("Project: Grade calculate");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(4, course.getNumProjects());
        course.addProjects("Project: QRcode");
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(5, course.getNumProjects());
    }
    @Test
    public void testAddGradesForProjects() throws IOException {
        String projectName = "Project: Grade calculate";
        Student student1 = new Student("Josepha Jube", "901234502", course);
        Student student2 = new Student("Christine Schaeffer", "901234508", course);
        HashMap<Student, Integer> contributions1 = new HashMap<Student, Integer>();
        contributions1.put(student1, 96);
        contributions1.put(student2, 87);
        course.addIndividualContributions(projectName, contributions1);
        course.updateGrades(new Grades(GRADES_DB));
        Map<String, Integer> teamgrades = new HashMap<String, Integer>();
        teamgrades.put("Team 1", 80);
        teamgrades.put("Team 2", 90);
        teamgrades.put("Team 3", 96);
        teamgrades.put("Team 4", 104);
        course.addGradesForProjects(projectName, teamgrades);
        course.updateGrades(new Grades(GRADES_DB));
        assertEquals(87, course.getAverageProjectsGrade(student1));
        assertEquals(81, course.getAverageProjectsGrade(student2));
    }

}
