package controller;

import DBAccess.DBCountry;
import DBAccess.DBCustomer;
import DBAccess.DBDivision;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import model.Country;
import model.Customer;
import model.Division;

/**
 * ModifyCustomer Class: Manages any changes of the customer in DB
 *
 * @author Hussein Coulibaly
 */


public class ModifyCustomer implements Initializable {

//    @FXML
//    private Label CustomerIDLabel;
//
//    @FXML
//    private Label NameLabel;
//
//    @FXML
//    private Label Address1Label;
//
//    @FXML
//    private Label FLDivisionLabel;
//
//    @FXML
//    private Label PostalCodeLabel;
//
//    @FXML
//    private Label CountryLabel;
//
//    @FXML
//    private Label PhoneNumberLabel;

    @FXML private TextField customerIDTextField;
    @FXML private ComboBox<Country> countryComboBox;
    @FXML private ComboBox<Division> divisionComboBox;
    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField phoneNumberTextField;

//    @FXML private TableView<Customer> customerTableView;
//    @FXML private TableColumn<Customer, Integer> customerIDColumn;
//    @FXML private TableColumn<Customer, String> nameColumn;
//    @FXML private TableColumn<Customer, String> addressColumn;
//    @FXML private TableColumn<Customer, String> divisionColumn;
//    @FXML private TableColumn<Customer, String> postalCodeColumn;
//    @FXML private TableColumn<Customer, String> countryColumn;
//    @FXML private TableColumn<Customer, String> phoneNumberColumn;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    @FXML private Button clearButton;

    private ObservableList<Customer> setCustomers;


//    public static int id;


    public Customer getCustomer(Customer customer) throws SQLException {
        Customer getCustomer = customer;
        Country c = DBCountry.getCountryByDivisionID(getCustomer.getDivisionID());
        ObservableList<Country> countries = DBCountry.getAllCountries();
        ObservableList<Division> division = DBDivision.getDivisionsByCountryID(c.getCountryID());

        customerIDTextField.setText(String.valueOf(getCustomer.getCustomerID()));
        nameTextField.setText(getCustomer.getCustomerName());
        addressTextField.setText(getCustomer.getAddress());
        postalCodeTextField.setText(getCustomer.getPostalCode());

        //lambda 2
        divisionComboBox.setItems(division);
        division.forEach(FirstLevelDivisions -> {
            if(FirstLevelDivisions.getDivisionID() == customer.getDivisionID())
                divisionComboBox.setValue(FirstLevelDivisions);
        });

        countryComboBox.setItems(countries);
        countryComboBox.setValue(c);
        phoneNumberTextField.setText(getCustomer.getPhoneNumber());

        return getCustomer;
    }

    @FXML
    public void clearButtonHandler(ActionEvent event) {
        countryComboBox.getSelectionModel().clearSelection();
        divisionComboBox.getSelectionModel().clearSelection();
        nameTextField.clear();
        addressTextField.clear();
        postalCodeTextField.clear();
        phoneNumberTextField.clear();
    }

    @FXML
    public void selectCountryHandler(ActionEvent event) {
        int countryID = countryComboBox.getValue().getCountryID();
        try {
            divisionComboBox.setItems(DBDivision.getDivisionsByCountryID(countryID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void selectDivision(ActionEvent event) {

    }


    @FXML
    public void saveButtonHandler(ActionEvent event) throws IOException {
        int customerID = Integer.parseInt(customerIDTextField.getText());
        String customerName = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        Division flDivision = divisionComboBox.getValue();
        int divisionID = flDivision.getDivisionID();
        Country country = countryComboBox.getValue();

        if (customerIDTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || addressTextField.getText().isEmpty() || divisionComboBox.getSelectionModel().isEmpty()
                || postalCodeTextField.getText().isEmpty() || countryComboBox.getSelectionModel().isEmpty() || phoneNumberTextField.getText().isEmpty()) {
            //appointmentAlertsEN(4);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: Blank Fields");
            alert.setContentText("One or more fields have been left blank. Please fill them and try again.");
            alert.showAndWait();
        }

        DBCustomer.updateCustomer(customerName, address, postalCode, phoneNumber, divisionID, customerID);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }


    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            setCustomers = DBCustomer.getAllCustomers();

//            customerTableView.setItems(setCustomers);
//            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
//            nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
//            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
//            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
//            postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
//            countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
//            phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

            countryComboBox.setItems(DBCountry.getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}