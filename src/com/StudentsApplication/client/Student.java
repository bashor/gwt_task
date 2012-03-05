package com.StudentsApplication.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Student implements IsSerializable {
    private String firstName;
    private String sureName;
    private double avgBall;
    private int groupNumber;

    public Student(String firstName, String sureName, double avgBall, int groupNumber) {
        this.firstName = firstName;
        this.sureName = sureName;
        this.avgBall = avgBall;
        this.groupNumber = groupNumber;
    }

    public Student() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSureName() {
        return sureName;
    }

    public double getAvgBall() {
        return avgBall;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (Double.compare(student.avgBall, avgBall) != 0) return false;
        if (groupNumber != student.groupNumber) return false;
        if (!firstName.equals(student.firstName)) return false;
        if (!sureName.equals(student.sureName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
//        long temp;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + sureName.hashCode();
//        temp = avgBall != +0.0d ? Double.doubleToLongBits(avgBall) : 0L;
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + groupNumber;
        return result;
    }
}
