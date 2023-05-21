package com.studentregistrationform;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class AttendanceController extends Controller implements Initializable {
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private DatePicker datePicker;

    @FXML private TreeTableView<Object> treeTableView;
    @FXML private TreeTableColumn<Student, String> nameCol;
    @FXML private TreeTableColumn<Student, String> surnameCol;
    @FXML private TreeTableColumn<Object, Boolean> attendanceCol;

    TreeItem<Object> root = new TreeItem<>();
    private LocalDate date;
    private Group group;
    private final ObservableMap<Student, SimpleBooleanProperty> attendanceMap = FXCollections.observableHashMap();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(Group group : groups) {
            choiceBox.getItems().add(group.getName());
        }

        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("surname"));
        attendanceCol.setCellValueFactory(cellData -> {
            Student student = (Student) cellData.getValue().getValue();
            return attendanceMap.computeIfAbsent(student, k -> new SimpleBooleanProperty(false));
        });
        attendanceCol.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(attendanceCol));

        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
        treeTableView.setEditable(true);
    }

    @FXML
    void seeAttendance() {
        root.getChildren().clear();

        group = groups.get(0);
        date = datePicker.getValue();
        for(Group groupIterable : groups) {
            if(Objects.equals(groupIterable.getName(), choiceBox.getValue())){
                group = groupIterable;
                break;
            }
        }

        for (Student student : group.getStudents()) {
            TreeItem<Object> studentItem = new TreeItem<>(student);
            root.getChildren().add(studentItem);
        }

        for (Student student : group.getStudents()) {
            boolean present = groupData.attended(group, student, date);
            attendanceMap.put(student, new SimpleBooleanProperty(present));
        }
        treeTableView.refresh();
    }

    @FXML
    void save() {
        for (TreeItem<Object> item : root.getChildren()) {
            if (item.getValue() instanceof Student student) {
                boolean present = attendanceMap.get(student).get();
                if (present)
                    groupData.markAttendance(group, student, date);
            }
        }
    }

}
