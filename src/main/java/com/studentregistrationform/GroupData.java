package com.studentregistrationform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class GroupData implements Prototype {
    private static final GroupData instance = new GroupData();
    private final ObservableList<Group> groups = FXCollections.observableArrayList();
    private GroupData() {}
    public static GroupData getInstance() {
        return instance;
    }

    @Override
    public ObservableList<Group> getGroups(){
        return groups;
    }
    @Override
    public void addGroup(Group group) {
        groups.add(group);
    }
    @Override
    public void removeGroup(Group group) {
        groups.remove(group);
    }

    @Override
    public void addStudent(Group group, Student student) {
        group.getStudents().add(student);
    }
    @Override
    public void removeStudent(Group group, Student student) {
        group.getStudents().remove(student);
    }
    @Override
    public void markAttendance(Group group, Student student, LocalDate date) {
        group.markAttendance(student, date);
    }
    @Override
    public boolean attended(Group group, Student student, LocalDate date) {
        return group.attended(student, date);
    }
    @Override
    public List<LocalDate> getAttendanceDates(Group group, Student student) {
        return group.getAttendanceDates(student);
    }
}