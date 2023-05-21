package com.studentregistrationform;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FormController extends Controller implements Initializable {
    @FXML
    private TextField groupNameField;
    @FXML
    private TextField studentIDField;
    @FXML
    private TextField studentNameField;
    @FXML
    private TextField studentSurnameField;
    @FXML
    private Label error;

    @FXML
    private TreeTableView<Object> treeTableView;
    @FXML
    private TreeTableColumn<Student, String> nameCol;
    @FXML
    private TreeTableColumn<Student, String> surnameCol;
    @FXML
    private TreeTableColumn<Student, Integer> IDCol;

    TreeItem<Object> root = new TreeItem<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("surname"));
        IDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("ID"));

        nameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        surnameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        Callback<TreeTableColumn<Student, Integer>, TreeTableCell<Student, Integer>> cellFactory =
                column -> new TextFieldTreeTableCell<>(new IntegerStringConverter());
        IDCol.setCellFactory(cellFactory);

        nameCol.setOnEditCommit(event -> {
            TreeItem<Student> editedItem = event.getTreeTablePosition().getTreeItem();
            editedItem.getValue().setName(event.getNewValue());
        });
        surnameCol.setOnEditCommit(event -> {
            TreeItem<Student> editedItem = event.getTreeTablePosition().getTreeItem();
            editedItem.getValue().setSurname(event.getNewValue());
        });
        IDCol.setOnEditCommit(event -> {
            TreeItem<Student> editedItem = event.getTreeTablePosition().getTreeItem();
            editedItem.getValue().setID(event.getNewValue());
        });


        for (Group group : groups) {
            TreeItem<Object> groupItem = new TreeItem<>(group);
            root.getChildren().add(groupItem);

            for (Student student : group.getStudents()) {
                TreeItem<Object> studentItem = new TreeItem<>(student);
                groupItem.getChildren().add(studentItem);
            }
        }

        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
        treeTableView.setEditable(true);
    }

    @FXML
    void addGroup() {
        error.setText("");

        String groupName = groupNameField.getText();
        Group group = new Group(groupName, FXCollections.observableArrayList());
        groupData.addGroup(group);
        root.getChildren().add(new TreeItem<>(group));
    }
    @FXML
    void removeGroup() {
        error.setText("");

        TreeItem<Object> selected = treeTableView.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue() instanceof Group) {
            selected.getParent().getChildren().remove(selected);

            groupData.removeGroup((Group) selected.getValue());
        }
        else
            error.setText("No group selected");
    }

    @FXML
    void addStudent() {
        error.setText("");

        TreeItem<Object> selected = treeTableView.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue() instanceof Group selectedGroup) {
            String name = studentNameField.getText();
            String surname = studentSurnameField.getText();
            int id = Integer.parseInt(studentIDField.getText());

            Student student = new Student(name, surname, id);
            groupData.addStudent(selectedGroup, student);

            TreeItem<Object> studentItem = new TreeItem<>(student);
            selected.getChildren().add(studentItem);
        }
        else
            error.setText("No group selected");
    }
    @FXML
    void removeStudent() {
        error.setText("");

        TreeItem<Object> selected = treeTableView.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getValue() instanceof Student selectedStudent) {
            Group selectedGroup = (Group) selected.getParent().getValue();
            selected.getParent().getChildren().remove(selected);

            groupData.removeStudent(selectedGroup, selectedStudent);
        }
        else
            error.setText("No student selected");
    }

    // IMPORT OPTIONS
    @FXML
    void csvImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                String[] line;
                reader.readNext(); // Skip the header row

                Group group = null;

                while ((line = reader.readNext()) != null) {
                    String rowData = line[0].replaceAll("\"", "");
                    String[] rowDataArray = rowData.split(",");

                    String groupName = rowDataArray[0].trim();
                    String name = rowDataArray[1].trim();
                    String surname = rowDataArray[2].trim();
                    int id = rowDataArray.length > 3 ? Integer.parseInt(rowDataArray[3].trim()) : -1;

                    if (group == null || !group.getName().equals(groupName)) {
                        group = new Group(groupName, FXCollections.observableArrayList());
                        groups.add(group);
                    }

                    Student student = new Student(name, surname, id);
                    group.getStudents().add(student);
                }

                System.out.println("Data imported successfully!");
            } catch (IOException | CsvValidationException e) {
                System.out.println("Failed to read file: " + e.getMessage());
            }
        }
        refreshTableView();
    }

    @FXML
    void xlsxImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                Workbook workbook = WorkbookFactory.create(selectedFile);
                Sheet sheet = workbook.getSheetAt(0);

                String groupName = null;
                Group group = null;

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue; // Skip the header row
                    }

                    String currentGroupName = row.getCell(0).getStringCellValue();

                    if (!currentGroupName.equals(groupName)) {
                        groupName = currentGroupName;
                        group = new Group(groupName, FXCollections.observableArrayList());
                        groups.add(group);
                    }

                    String name = row.getCell(1).getStringCellValue();
                    String surname = row.getCell(2).getStringCellValue();
                    int id = (int) row.getCell(3).getNumericCellValue();
                    Student student = new Student(name, surname, id);
                    group.getStudents().add(student);
                }

                System.out.println("Data imported successfully!");
            } catch (IOException e) {
                System.out.println("Failed to read file: " + e.getMessage());
            }
        }
        refreshTableView();
    }

    private void refreshTableView() {
        // Clear the current root
        root.getChildren().clear();

        // Rebuild the tree table view with the updated group data
        for (Group group : groups) {
            TreeItem<Object> groupItem = new TreeItem<>(group);
            root.getChildren().add(groupItem);

            for (Student student : group.getStudents()) {
                TreeItem<Object> studentItem = new TreeItem<>(student);
                groupItem.getChildren().add(studentItem);
            }
        }

        // Refresh the table view
        treeTableView.refresh();
    }

}
