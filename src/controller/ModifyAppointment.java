package controller;

import DBAccess.DBAppointment;
import DBAccess.DBContact;
import DBAccess.DBCustomer;
import DBAccess.DBUser;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
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


public class ModifyAppointment implements Initializable {

//    @FXML private Label AppointmentIDLabel;
//    @FXML private Label UserIDLabel;
//    @FXML private Label CustomerIDLabel;
//    @FXML private Label TitleLabel;
//    @FXML private Label DescriptionLabel;
//    @FXML private Label LocationLabel;
//    @FXML private Label ContactLabel;
//    @FXML private Label TypeLabel;
//    @FXML private Label StartDateLabel;
//    @FXML private Label StartTimeLabel;
//    @FXML private Label EndDateLabel;
//    @FXML private Label EndTime;
    @FXML private TextField appointmentIDTextField;
    @FXML private ComboBox<Integer> userIDComboBox;
    @FXML private ComboBox<Integer> customerComboBox;
    @FXML private TextField titleTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private ComboBox<Integer> contactComboBox;
    @FXML private TextField typeTextField;
    @FXML private DatePicker startDatePicker;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> endTimeComboBox;
    @FXML private Button saveButton;
    @FXML private Button clearButton;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> nameColumn;

    private ObservableList<Customer> setCustomers;

    private Appointment getAppointment;

    int index;


    public static int idNum;

    private ZoneId zoneID = ZoneId.of("UTC");
    private ZoneId zoneIDEST = ZoneId.of("America/New_York");
    private ZoneId zoneIDDef = ZoneId.of(TimeZone.getDefault().getID());


    public Appointment getAppointment(Appointment selectAppt) {
        Appointment getAppointment = selectAppt;
        try {

            String startZID = selectAppt.getStart().atZone(zoneIDDef).format(DateTimeFormatter.ofPattern("HH:mm"));
            String endZID = selectAppt.getEnd().atZone(zoneIDDef).format(DateTimeFormatter.ofPattern("HH:mm"));

            appointmentIDTextField.setText(String.valueOf(getAppointment.getAppointmentID()));
            userIDComboBox.getSelectionModel().select(getAppointment.getUserID() - 1);
            customerComboBox.getSelectionModel().select(getAppointment.getCustomerID() - 1);
            titleTextField.setText(getAppointment.getTitle());
            descriptionTextField.setText(String.valueOf(getAppointment.getDescription()));
            locationTextField.setText(getAppointment.getLocation());
            contactComboBox.getSelectionModel().select(getAppointment.getContactID() - 1);
            typeTextField.setText(getAppointment.getType());
            startDatePicker.setValue(getAppointment.getStart().toLocalDate());
            startTimeComboBox.getSelectionModel().select(String.valueOf(startZID));
            endDatePicker.setValue(getAppointment.getEnd().toLocalDate());
            endTimeComboBox.getSelectionModel().select(String.valueOf(endZID));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getAppointment;
    }


    @FXML
    public void saveButtonHandler(ActionEvent event) throws SQLException, IOException {


        comboBoxAlert();
        try {
            int appointmentID = Integer.parseInt(appointmentIDTextField.getText());
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
                if (endTS.before(startTS)) {
                    //appointmentAlertsEN(5);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setHeaderText("Warning: Invalid Appointment Time Or Date");
                    alert.setContentText("Appointment start time cannot be before the end time. Appointments must be start and end on the same day.");
                    alert.showAndWait();
                    return;
                }
            }

            if (appointmentIDTextField.getText().isEmpty() || titleTextField.getText().isEmpty()
                    || descriptionTextField.getText().isEmpty() || locationTextField.getText().isEmpty()
                    || typeTextField.getText().isEmpty()) {
                //appointmentAlertsEN(4);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Warning: Blank Fields");
                alert.setContentText("One or more fields have been left blank. Please fill them and try again.");
                alert.showAndWait();

            } else {

                DBAppointment.updateAppointments(title, description, location, type, startTS, endTS, customerID, userID, contactID, appointmentID);

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
                for (Customer c : selectCustomer) {
                    customerIDCombo.add(c.getCustomerID());
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
    public void selectStartDateHandler(ActionEvent event) {

    }

    @FXML
    public void selectEndDateHandler(ActionEvent event) {

    }


    @FXML
    public void clearButtonHandler(ActionEvent event) throws IOException {
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

            customerTableView.setItems(setCustomers);
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

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
        startTimeComboBox.setItems(time);
        endTimeComboBox.setItems(time);
        appointmentIDTextField.setText(Integer.toString(idNum));

    }

}