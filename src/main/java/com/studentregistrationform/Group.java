package com.studentregistrationform;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.*;

public class Group {
    private final StringProperty name;
    private static final StringProperty surname = null;
    private static final IntegerProperty ID = null;
    private final ObservableList<Student> students;
    private final Map<LocalDate, Set<Student>> attendanceRecords;

    public Group(String name, ObservableList<Student> students) {
        this.name = new SimpleStringProperty(name);
        this.students = students;
        this.attendanceRecords = new HashMap<>();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public IntegerProperty IDProperty() {
        return ID;
    }

    public String getName() {
        return name.get();
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void markAttendance(Student student, LocalDate date) {
        Set<Student> presentStudents = attendanceRecords.getOrDefault(date, new HashSet<>());
        presentStudents.add(student);
        attendanceRecords.put(date, presentStudents);
    }

    public boolean attended(Student student, LocalDate date) {
        Set<Student> presentStudents = attendanceRecords.get(date);
        return presentStudents != null && presentStudents.contains(student);
    }

    public List<LocalDate> getAttendanceDates(Student student) {
        List<LocalDate> dates = new ArrayList<>();
        for (Map.Entry<LocalDate, Set<Student>> entry : attendanceRecords.entrySet()) {
            if (entry.getValue().contains(student)) {
                dates.add(entry.getKey());
            }
        }
        return dates;
    }

    public Map<Student, List<LocalDate>> getAttendanceRecordsForDateRange(LocalDate startDate, LocalDate endDate) {
        Map<Student, List<LocalDate>> attendanceMap = new HashMap<>();

        for (Student student : students) {
            List<LocalDate> attendanceDates = new ArrayList<>();
            for (LocalDate attendanceDate : getAttendanceDates(student)) {
                if (attendanceDate.isEqual(startDate) || attendanceDate.isEqual(endDate)
                        || (attendanceDate.isAfter(startDate) && attendanceDate.isBefore(endDate))) {
                    attendanceDates.add(attendanceDate);
                }
            }
            attendanceMap.put(student, attendanceDates);
        }

        return attendanceMap;
    }
}

