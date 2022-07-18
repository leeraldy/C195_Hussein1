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
    @FXML TextField AppointmentDTextField;
    @FXML ComboBox<Integer> UserIDComboBox;
    @FXML ComboBox<Integer> CustomerComboBox;
    @FXML TextField TitleTextField;
    @FXML TextField DescriptionTextField;
    @FXML TextField LocationTextField;
    @FXML ComboBox<Integer> ContactComboBox;
    @FXML TextField TypeTextField;
    @FXML DatePicker StartDatePicker;
    @FXML ComboBox<String> StartTimeComboBox;
    @FXML DatePicker EndDatePicker;
    @FXML ComboBox<String> EndTimeComboBox;
    @FXML Button SaveButton;
    @FXML Button CancelButton;
    @FXML TableView<Customer> CustomerTableView;
    @FXML TableColumn<Customer, Integer> CustomerIDColumn;
    @FXML TableColumn<Customer, String> NameColumn;

     ObservableList<Customer> setCustomers;


    public static int idNum;

    private ZoneId zoneID = ZoneId.of("UTC");
    private ZoneId zoneIDEST = ZoneId.of("America/New_York");
    private ZoneId zoneIDDef = ZoneId.systemDefault();


    @FXML
    void saveButtonHandler(ActionEvent event) throws SQLException, IOException {

        comboBoxAlert();
        try {
            int appointmentID = idNum++;
            int userID = UserIDComboBox.getValue();
            int customerID = CustomerComboBox.getValue();
            String title = TitleTextField.getText();
            String description = DescriptionTextField.getText();
            String location = LocationTextField.getText();
            int contactID = ContactComboBox.getValue();
            String type = TypeTextField.getText();
            LocalDateTime start = LocalDateTime.of(StartDatePicker.getValue(), LocalTime.parse(StartTimeComboBox.getSelectionModel().getSelectedItem()));
            LocalDateTime end = LocalDateTime.of(EndDatePicker.getValue(), LocalTime.parse(EndTimeComboBox.getSelectionModel().getSelectedItem()));;
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

            ObservableList<Appointment> appointment = DBAppointment.getAppointmentsByCustomerID(CustomerComboBox.getSelectionModel().getSelectedItem());
            for (Appointment appt : appointment) {

                LocalDateTime apptSt = appt.getStart();
                LocalDateTime apptEnd = appt.getEnd();
                Timestamp apptStTS = Timestamp.valueOf(apptSt);
                Timestamp apptEndTS = Timestamp.valueOf(apptEnd);
                LocalDate startD = StartDatePicker.getValue();
                LocalDate endD = EndDatePicker.getValue();

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

            if (AppointmentDTextField.getText().isEmpty() || TitleTextField.getText().isEmpty()
                    || DescriptionTextField.getText().isEmpty() || LocationTextField.getText().isEmpty()
                    || TypeTextField.getText().isEmpty()) {
                //appointmentAlertsEN(4);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Warning: Blank Fields");
                alert.setContentText("One or more fields have been left blank. Please fill them and try again.");
                alert.showAndWait();
            } else {

                DBAppointment.addAppointments(title, description, location, type, startTS, endTS, customerID, userID, contactID);

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Main");
                stage.show();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void comboBoxAlert() {

        if (UserIDComboBox.getSelectionModel().isEmpty() || CustomerComboBox.getSelectionModel().isEmpty()
                || ContactComboBox.getSelectionModel().isEmpty() || StartDatePicker.getValue() == null
                || StartTimeComboBox.getSelectionModel().isEmpty() || EndDatePicker.getValue() == null
                || EndTimeComboBox.getSelectionModel().isEmpty()) {
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
        UserIDComboBox.setItems(userIDCombo);
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        CustomerComboBox.setItems(customerIDCombo);
    }


    public void selectContact() {
        ObservableList<Integer> contactCombo = FXCollections.observableArrayList();

        ObservableList<Contact> selectContact = DBContact.getAllContacts();
        if (selectContact != null) {
            for (Contact cont : selectContact) {
                contactCombo.add(cont.getContactID());
            }
        }
        ContactComboBox.setItems(contactCombo);
    }

    @FXML
    void onActionSelectStartDate(ActionEvent event) {

    }

    @FXML
    void onActionSelectEndDate(ActionEvent event) {

    }


    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            setCustomers = DBCustomer.getAllCustomers();

            CustomerTableView.setItems(setCustomers);
            CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            NameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        selectUserID();
        selectCustomerID();
        selectContact();

        ObservableList<String> time = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(23, 0);

        time.add(start.toString());
        while (start.isBefore(end)) {
            start = start.plusMinutes(15);
            time.add(start.toString());
        }
        StartTimeComboBox.setItems(time);
        EndTimeComboBox.setItems(time);
        AppointmentDTextField.setText(Integer.toString(idNum));

    }

}