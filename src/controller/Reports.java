package controller;

import DBAccess.DBAppointment;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;


public class Reports implements Initializable {

    @FXML private RadioButton Report1RB;
    @FXML private RadioButton Report2RB;
    @FXML private RadioButton Report3RB;
    @FXML private ToggleGroup viewReportTG;
    @FXML private Button GenerateButton;
    @FXML private Button ResetButton;
    @FXML private Button backButton;
    @FXML private TextArea ReportTextArea;


    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        System.out.println("Back Button was pressed");

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }

    @FXML
    public void generateReport(ActionEvent event) {
        if (Report1RB.isSelected()) {
            ReportTextArea.setText(DBAppointment.reportAppointmentTypeMonth());
        }
        if (Report2RB.isSelected()) {
            ReportTextArea.setText(DBAppointment.reportAppointmentContact());
        }
        if (Report3RB.isSelected()) {
            ReportTextArea.setText(DBAppointment.reportAppointmentTypeLocation());
        }
    }


    @FXML
    public void reportOne(ActionEvent event) {

    }

    @FXML
    public void reportTwo(ActionEvent event) {

    }

    @FXML
    public void reportThree(ActionEvent event) {

    }


    @FXML
    public void resetReport(ActionEvent event) {
        ReportTextArea.clear();
    }





    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}