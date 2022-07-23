package controller;

import DBAccess.DBAppointment;
import static DBAccess.DBAppointment.deleteAppointments;
import DBAccess.DBCustomer;
import static DBAccess.DBCustomer.deleteCustomer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
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
import model.Customer;

/**
 * MainsScreen Class: Manages the main screen and other screens objects
 *
 * @author Hussein Coulibaly
 */


public class MainScreen implements Initializable {

    @FXML private Label timeZoneLabel;
    @FXML private Button newApptButton;
    @FXML private Button editApptButton;
    @FXML private Button deleteApptButton;
    @FXML private RadioButton viewAllRB;
    @FXML private RadioButton viewWeeklyRB;
    @FXML private RadioButton viewMonthlyRB;
    @FXML private ToggleGroup viewAppointmentTG;
    @FXML private DatePicker viewDate;
    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> apptIDColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, Integer> contactColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn;
    @FXML private TableColumn<Appointment, Integer> customerIDColumn1;
    @FXML private TableColumn<Appointment, Integer> userIDColumn;
    @FXML private Button reportsButton;
    @FXML private Button logOutButton;
    @FXML private Button addCustomerButton;
    @FXML private Button editCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private TableView<Customer> CustomerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn2;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> address1Column;
    @FXML private TableColumn<Customer, String> divisionColumn;
    @FXML private TableColumn<Customer, String> postalCodeColumn;
    @FXML private TableColumn<Customer, String> countryColumn;
    @FXML private TableColumn<Customer, String> phoneNumberColumn;


    @FXML
    public void noFilterButtonHandler(ActionEvent event) throws SQLException {
        if (viewAllRB.isSelected()) {
            try {
                ObservableList<Appointment> appointments = DBAppointment.getAllAppointments();
                appointmentTableView.setItems(appointments);
                appointmentTableView.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "DB connection failed. Please restart", clickOkay);
                invalidInput.showAndWait();
                return;
            }
        }
    }


    @FXML
    public void weeklyFilterButtonHandler(ActionEvent event) throws SQLException {
        if (viewWeeklyRB.isSelected()) {
            try {
                ObservableList<Appointment> appointments = DBAppointment.getAppointmentsByWeek();
                appointmentTableView.setItems(appointments);
                appointmentTableView.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void monthlyFilterButtonHandler(ActionEvent event) throws SQLException {
        if (viewMonthlyRB.isSelected()) {
            try {
                ObservableList<Appointment> appointments = DBAppointment.getAppointmentsByMonth();

                appointmentTableView.setItems(appointments);
                appointmentTableView.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void viewDateButtonHandler(ActionEvent event
    ) {

    }


    @FXML
    public void newAppointmentButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/AddAppointmentForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Add Appointment");
        stage.show();
    }


    @FXML
    void editButtonHandler(ActionEvent event) throws IOException {
        Appointment selectedAppointments = appointmentTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointments == null) {
            //appointmentAlertsEN(1);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: No Appointment Selected");
            alert.setContentText("Please choose an appointment then try again.");
            alert.showAndWait();
            return;

        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/ModifyAppointment.fxml"));
            loader.load();
            ModifyAppointment controller = loader.getController();
            controller.getAppointment(selectedAppointments);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle("Modify Appointment");
            stage.show();
        }
    }


    @FXML
    void deleteButtonHandler(ActionEvent event) throws SQLException {
        Appointment selectedAppointments = appointmentTableView.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> appointments = DBAppointment.getAllAppointments();
        if (selectedAppointments == null) {
            //appointmentAlertsEN(1);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: No Appointment Selected");
            alert.setContentText("Please choose an appointment then try again.");
            alert.showAndWait();
            return;
        }
        try {
            int apptID = selectedAppointments.getAppointmentID();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Confirmation: Appointment Deletion");
            alert.setContentText("Appointment ID: " + apptID + "." + "\n" + "Appointment Type: " + selectedAppointments.getType() + "." + "\n" + "Do you want to proceeed with deletion?");
            Optional<ButtonType> deleteConfirm = alert.showAndWait();

            if (deleteConfirm.isPresent() && deleteConfirm.get() == ButtonType.OK) {
                deleteAppointments(apptID);
                appointmentTableView.setItems(appointments);
                appointmentTableView.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void viewReportsButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/ReportsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Reports");
        stage.show();
    }


    @FXML
    public void logoutButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Login");
        stage.show();
    }


    @FXML
    public void newCustomerButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/AddCustomerForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Add Customer");
        stage.show();
    }


    @FXML
    void editCustomerButtonHandler(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            //customerDeletionEN(1);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: No Customer Selected");
            alert.setContentText("Please choose choose a customer then try again.");
            alert.showAndWait();
            return;

        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/ModifyCustomer.fxml"));
            loader.load();
            ModifyCustomer controller = loader.getController();
            controller.getCustomer(selectedCustomer);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle("Modify Customer");
            stage.show();
        }
    }


    @FXML
    void deleteCustomerButtonHandler(ActionEvent event) throws SQLException {
        Customer selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> appointments = DBAppointment.getAllAppointments();
        ObservableList<Customer> customers = DBCustomer.getAllCustomers();
        if (selectedCustomer == null) {
            //customerDeletionEN(1);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: No Customer Selected");
            alert.setContentText("Please choose choose a customer then try again.");
            alert.showAndWait();
            return;
        }
        try {
            int custID = selectedCustomer.getCustomerID();
            for (Appointment customerAppointments : appointments) {
                if (customerAppointments.getCustomerID() == selectedCustomer.getCustomerID()) {
                    //customerDeletionEN(2);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING");
                    alert.setHeaderText("Warning: Customer Has Existing Appointment.");
                    alert.setContentText("Proceeding with deletion will also delete the existing appointment.");
                    alert.showAndWait();
                    return;
                }
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRMATION");
            alert.setHeaderText("Confirmation: Customer Deletion");
            alert.setContentText("You are about to proceed with the deletion of a customer.");
            Optional<ButtonType> deleteConfirm = alert.showAndWait();

            if (deleteConfirm.isPresent() && deleteConfirm.get() == ButtonType.OK) {
                deleteCustomer(custID);

                CustomerTableView.setItems(customers);
                CustomerTableView.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ResourceBundle resourceb = ResourceBundle.getBundle("main/Lang", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            ZoneId z = ZoneId.systemDefault();
            timeZoneLabel.setText(z.toString());
        }
        try {
            ObservableList<Appointment> appointments = DBAppointment.getAllAppointments();

            appointmentTableView.setItems(appointments);
            apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIDColumn1.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

            //lambda
            startColumn.setCellFactory(column -> {
                TableCell<Appointment, LocalDateTime> col = new TableCell<Appointment, LocalDateTime>() {
                    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(dtf.format(item));
                        }
                    }
                };
                return col;
            });

            endColumn.setCellFactory(column -> {
                TableCell<Appointment, LocalDateTime> col = new TableCell<Appointment, LocalDateTime>() {
                    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(dtf.format(item));
                        }
                    }
                };
                return col;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ObservableList<Customer> customers = DBCustomer.getAllCustomers();

            CustomerTableView.setItems(customers);
            customerIDColumn2.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            address1Column.setCellValueFactory(new PropertyValueFactory<>("address"));
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
            postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewDate.setValue(LocalDate.now());
    }
}
