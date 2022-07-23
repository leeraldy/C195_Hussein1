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

/**
 * AddAppointment Controller Class: Handles the control logic for all reports
 *
 * @author Hussein Coulibaly
 */


public class Reports implements Initializable {

    @FXML private RadioButton ReportOne;
    @FXML private RadioButton ReportTwo;
    @FXML private RadioButton ReportThree;
    @FXML private ToggleGroup viewReport;
    @FXML private Button GenerateButton;
    @FXML private Button ResetButton;
    @FXML private Button backButton;
    @FXML private TextArea ReportTextArea;


    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        System.out.println("Back Button was pressed");

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }

    @FXML
    public void generateReport(ActionEvent event) {
        if (ReportOne.isSelected()) {
            ReportTextArea.setText(DBAppointment.reportAppointmentTypeMonth());
        }
        if (ReportTwo.isSelected()) {
            ReportTextArea.setText(DBAppointment.reportAppointmentContact());
        }
        if (ReportThree.isSelected()) {
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

    }

}