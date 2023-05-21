package com.studentregistrationform;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

// With abstract classes you:
// Can't create objects
// Can inherit methods and information
// Can only extend one class
public abstract class Controller {
    @FXML
    void backToMenu(ActionEvent event) throws IOException { // All windows have to have a "back to menu" button to go back to the menu
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        root.setStyle("-fx-background-color: LIGHTBLUE;");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    GroupData groupData = GroupData.getInstance(); // All windows have to have group data - STUDENTS
    ObservableList<Group> groups = groupData.getGroups(); // All windows have to have GROUPS
}