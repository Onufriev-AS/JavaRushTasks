package com.javarush.task.task29.task2909.human;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class University {

    private List<Student> students = new ArrayList<>();
    private String name;
    private int age;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public University(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student getStudentWithAverageGrade(double averageGrade) {
        Student student = null;
        for (Student s: students){
            if (s.getAverageGrade() == averageGrade) student = s;
        }
        return student;
    }

    public Student getStudentWithMaxAverageGrade() {
//        Student student = null;
//        double maxAverageGrade = students.get(0).getAverageGrade();
//        for (Student s: students){
//            if (s.getAverageGrade() >= maxAverageGrade) student = s;
//        }
        return Collections.max(students, Comparator.comparingDouble(Student::getAverageGrade));
    }

    public Student getStudentWithMinAverageGrade(){
        Student student = null;
        double maxAverageGrade = students.get(0).getAverageGrade();
        for (Student s: students){
            if (s.getAverageGrade() <= maxAverageGrade) student = s;
        }
        return student;
    }

    public void expel(Student student){
        students.remove(student);
    }

//    public void getStudentWithMinAverageGradeAndExpel() {
//        //TODO:
//    }
}