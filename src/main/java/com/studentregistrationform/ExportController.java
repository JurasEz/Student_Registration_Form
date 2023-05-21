package com.studentregistrationform;

import com.opencsv.CSVWriter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class ExportController extends Controller {
    @FXML
    void csvExport() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("student_data.csv"))) {
            String[] header = {"Group Name", "Name", "Surname", "ID", "Attendance"};
            writer.writeNext(header);
            for (Group group : groups) {
                for (Student student : group.getStudents()) {
                    String[] record = {group.getName(), student.getName(), student.getSurname(),
                            String.valueOf(student.getID()), getAttendanceRecordAsString(group, student)};
                    writer.writeNext(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void xlsxExport() {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();

        // Loop through the groups
        for (Group group : groups) {
            // Create a new sheet for the group
            Sheet sheet = workbook.createSheet(group.getName());

            // Create a header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Surname");
            headerRow.createCell(2).setCellValue("ID");
            headerRow.createCell(2).setCellValue("Attendance");

            // Get the data for the group
            ObservableList<Student> students = group.getStudents();

            // Add the data to the sheet
            for (int j = 0; j < students.size(); j++) {
                Student student = students.get(j);
                Row row = sheet.createRow(j + 1);
                row.createCell(0).setCellValue(student.getName());
                row.createCell(1).setCellValue(student.getSurname());
                row.createCell(2).setCellValue(student.getID());
                row.createCell(3).setCellValue(getAttendanceRecordAsString(group, student));
            }

            // Autosize the columns
            for (int j = 0; j < 3; j++) {
                sheet.autoSizeColumn(j);
            }
        }

        // Save the workbook to a file
        try (FileOutputStream fileOut = new FileOutputStream("student_data.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAttendanceRecordAsString(Group group, Student student) {
        List<LocalDate> attendanceDates = group.getAttendanceDates(student);
        Collections.sort(attendanceDates);
        if(attendanceDates.size() > 0) {
            int totalDays = calculateDaysBetween(attendanceDates.get(0), attendanceDates.get(attendanceDates.size() - 1));
            int attendedDays = attendanceDates.size();
            double attendanceRate = attendedDays * 100.0 / totalDays;
            return String.format("%.2f%% (%d/%d)", attendanceRate, attendedDays, totalDays);
        }else
            return "-";
    }
    private static int calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
