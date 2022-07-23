package controller;

import DBAccess.DBAppointment;
import DBAccess.DBContact;
import DBAccess.DBCustomer;
import DBAccess.DBUser;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

/**
 * AddAppoitment Class: Manages new appointment
 *
 * @author Hussein Coulibaly
 */


public class AddAppointment implements Initializable {

//    @FXML Label AppointmentIDLabel;
//    @FXML Label UserIDLabel;
//    @FXML Label CustomerIDLabel;
//    @FXML Label TitleLabel;
//    @FXML Label DescriptionLabel;
//    @FXML Label LocationLabel;
//    @FXML Label ContactLabel;
//    @FXML Label TypeLabel;
//    @FXML Label StartDateLabel;
//    @FXML Label StartTimeLabel;
//    @FXML Label EndDateLabel;
//    @FXML Label EndTime;
//    @FXML TextField appointmentIDTextField;

    @FXML TextField titleTextField;
    @FXML TextField descriptionTextField;
    @FXML TextField locationTextField;
    @FXML ComboBox<Integer> contactComboBox;
    @FXML TextField typeTextField;
    @FXML DatePicker startDatePicker;
    @FXML ComboBox<String> startTimeComboBox;
    @FXML DatePicker endDatePicker;
    @FXML ComboBox<String> endTimeComboBox;
    @FXML ComboBox<Integer> userIDComboBox;
    @FXML ComboBox<Integer> customerComboBox;
    @FXML Button saveButton;
    @FXML Button cancelButton;
    @FXML Button clearButton;

//    @FXML TableView<Customer> CustomerTableView;
//    @FXML TableColumn<Customer, Integer> CustomerIDColumn;
//    @FXML TableColumn<Customer, String> NameColumn;

     ObservableList<Customer> setCustomers;


//    public static int idNum;

    private ZoneId zoneID = ZoneId.of("UTC");
    private ZoneId zoneIDEST = ZoneId.of("America/New_York");
    private ZoneId zoneIDDef = ZoneId.systemDefault();

    @FXML
    public void clearButtonHandler() {
//        appointmentIDTextField.clear();
        userIDComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        contactComboBox.getSelectionModel().clearSelection();
        titleTextField.clear();
        startTimeComboBox.getSelectionModel().clearSelection();
        startDatePicker.getEditor().clear();
        endTimeComboBox.getSelectionModel().clearSelection();
        endDatePicker.getEditor().clear();

    }


    @FXML
    public void saveButtonHandler(ActionEvent event) throws SQLException, IOException {

        comboBoxAlert();
        try {
//          int appointmentID = idNum++;
            int userID = userIDComboBox.getValue();
            int customerID = customerComboBox.getValue();
            String title = titleTextField.getText();
            String description = descriptionTextField.getText();
            String location = locationTextField.getText();
            int contactID = contactComboBox.getValue();
            String type = typeTextField.getText();
            LocalDateTime start = LocalDateTime.of(startDatePicker.getValue(), LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem()));
            LocalDateTime end = LocalDateTime.of(endDatePicker.getValue(), LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem()));;
            ZonedDateTime startUTC = start.atZone(zoneID).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = end.atZone(zoneID).withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp startTS = Timestamp.valueOf(startUTC.toLocalDateTime());
            Timestamp endTS = Timestamp.valueOf(endUTC.toLocalDateTime());

            ZonedDateTime businessStartEST = ZonedDateTime.of(start, zoneIDEST);
            ZonedDateTime businessEndEST = ZonedDateTime.of(end, zoneIDEST);

            if (businessStartEST.toLocalTime().isAfter(LocalTime.of(22, 0)) || businessEndEST.toLocalTime().isAfter(LocalTime.of(22, 0))
                    || businessStartEST.toLocalTime().isBefore(LocalTime.of(8, 0)) || businessEndEST.toLocalTime().isBefore(LocalTime.of(8, 0))) {
                //appointmentAlertsEN(3);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Warning: Outside Business Hours");
                alert.setContentText("Please select a time within business hours.");
                alert.showAndWait();
                return;
            };

            ObservableList<Appointment> appointment = DBAppointment.getAppointmentsByCustomerID(customerComboBox.getSelectionModel().getSelectedItem());
            for (Appointment appt : appointment) {

                LocalDateTime apptSt = appt.getStart();
                LocalDateTime apptEnd = appt.getEnd();
                Timestamp apptStTS = Timestamp.valueOf(apptSt);
                Timestamp apptEndTS = Timestamp.valueOf(apptEnd);
                LocalDate startD = startDatePicker.getValue();
                LocalDate endD = endDatePicker.getValue();

                if (startTS.after(apptStTS) && startTS.before(apptEndTS)
                        || endTS.after(apptStTS) && endTS.before(apptEndTS)
                        || startTS.before(apptStTS) && endTS.after(apptStTS)
                        || startTS.equals(apptStTS) && endTS.equals(apptEndTS)
                        || startTS.equals(apptStTS) || endTS.equals(apptStTS)) {
                    //appointmentAlertsEN(2);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setHeaderText("Warning: Overlapping Appointment");
                    alert.setContentText("Chosen appointment is overlapping with another appointment. Please select again.");
                    alert.showAndWait();
                    return;
                }
                if (endTS.before(startTS) || endD.isAfter(startD)) {
                    //appointmentAlertsEN(5);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setHeaderText("Warning: Invalid Appointment Time Or Date");
                    alert.setContentText("Appointment start time cannot be before the end time. Appointments must be start and end on the same day.");
                    alert.showAndWait();
                    return;
                }
            }

            if (titleTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty() || locationTextField.getText().isEmpty()
                    || typeTextField.getText().isEmpty()) {
                //appointmentAlertsEN(4);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Warning: Blank Fields");
                alert.setContentText("One or more fields have been left blank. Please fill them and try again.");
                alert.showAndWait();
            } else {

                DBAppointment.addAppointments(title, description, location, type, startTS, endTS, customerID, userID, contactID);

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Main");
                stage.show();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void comboBoxAlert() {

        if (userIDComboBox.getSelectionModel().isEmpty() || customerComboBox.getSelectionModel().isEmpty()
                || contactComboBox.getSelectionModel().isEmpty() || startDatePicker.getValue() == null
                || startTimeComboBox.getSelectionModel().isEmpty() || endDatePicker.getValue() == null
                || endTimeComboBox.getSelectionModel().isEmpty()) {
            //appointmentAlertsEN(4);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: Blank Fields");
            alert.setContentText("One or more fields have been left blank. Please fill them and try again.");
            alert.showAndWait();
        }
    }


    public void selectUserID() {
        ObservableList<Integer> userIDCombo = FXCollections.observableArrayList();

        try {
            ObservableList<User> selectUser = DBUser.getAllUsers();
            if (selectUser != null) {
                for (User user : selectUser) {
                    userIDCombo.add(user.getUserID());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userIDComboBox.setItems(userIDCombo);
    }


    public void selectCustomerID() {
        ObservableList<Integer> customerIDCombo = FXCollections.observableArrayList();

        try {
            ObservableList<Customer> selectCustomer = DBCustomer.getAllCustomers();
            if (selectCustomer != null) {
                for (Customer cust : selectCustomer) {
                    customerIDCombo.add(cust.getCustomerID());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerComboBox.setItems(customerIDCombo);
    }


    public void selectContact() {
        ObservableList<Integer> contactCombo = FXCollections.observableArrayList();

        ObservableList<Contact> selectContact = DBContact.getAllContacts();
        if (selectContact != null) {
            for (Contact cont : selectContact) {
                contactCombo.add(cont.getContactID());
            }
        }
        contactComboBox.setItems(contactCombo);
    }

    @FXML
    public void selectStartDate(ActionEvent event) {

    }

    @FXML
    public void selectEndDate(ActionEvent event) {

    }


    @FXML
    public void cancelButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("MainScreen");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            setCustomers = DBCustomer.getAllCustomers();

//            CustomerTableView.setItems(setCustomers);
//            CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
//            NameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        selectUserID();
        selectCustomerID();
        selectContact();

        ObservableList<String> time = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);

        time.add(start.toString());
        while (start.isBefore(end)) {
            start = start.plusMinutes(15);
            time.add(start.toString());
        }
        startTimeComboBox.setItems(time);
        endTimeComboBox.setItems(time);
//        appointmentIDTextField.setText(Integer.toString(idNum));

    }

}