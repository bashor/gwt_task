package com.StudentsApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.StudentsApplication.client.StudentsApplicationService;
import com.StudentsApplication.client.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StudentsApplicationServiceImpl extends RemoteServiceServlet implements StudentsApplicationService {
    String STUDENTS_DB = "students.txt";

    @Override
    public Student[] getStudents() {

        final List<Student> students = new ArrayList<Student>();

        File file = new File(STUDENTS_DB);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String firstName = scanner.next();
                String sureName = scanner.next();
                double avgBall = scanner.nextDouble();
                int group = scanner.nextInt();

                students.add(new Student(firstName, sureName, avgBall, group));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return students.toArray(new Student[0]);
    }
}