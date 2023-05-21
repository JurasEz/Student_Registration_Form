package com.studentregistrationform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class MenuController extends Controller {
    @FXML void form(ActionEvent event) throws IOException {
        switchScene(event, "form.fxml");
    }
    @FXML void attendance(ActionEvent event) throws IOException {
        switchScene(event, "attendance.fxml");
    }
    @FXML void exportData(ActionEvent event) throws IOException {
        switchScene(event, "export.fxml");
    }
    @FXML void report(ActionEvent event) throws IOException {
        switchScene(event, "report.fxml");
    }

    protected void switchScene(ActionEvent event, String filename) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(filename)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        root.setStyle("-fx-background-color: LIGHTBLUE;");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}