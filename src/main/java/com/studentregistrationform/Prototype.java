package com.studentregistrationform;

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

// In interfaces all the methods are abstract
// You can implement as many interfaces as you want
// All fields are static and final
// Interfaces can be unrelated unlike abstract classes
public interface Prototype {
    void addGroup(Group group);
    void removeGroup(Group group);
    void addStudent(Group group, Student student);
    void removeStudent(Group group, Student student);
    void markAttendance(Group group, Student student, LocalDate date);
    boolean attended(Group group, Student student, LocalDate date);
    ObservableList<Group> getGroups();
    List<LocalDate> getAttendanceDates(Group group, Student student);
}