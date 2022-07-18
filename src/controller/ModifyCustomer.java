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
    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private ComboBox<Division> divisionComboBox;
    @FXML private TextField PostalCodeTextField;
    @FXML private ComboBox<Country> CountryComboBox;
    @FXML private TextField PhoneNumberTextField;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    @FXML private Button clearButton;
    @FXML private TableView<Customer> CustomerTableView;
    @FXML private TableColumn<Customer, Integer> CustomerIDColumn;
    @FXML private TableColumn<Customer, String> NameColumn;
    @FXML private TableColumn<Customer, String> Address1Column;
    @FXML private TableColumn<Customer, String> FLDivisionColumn;
    @FXML private TableColumn<Customer, String> PostalCodeColumn;
    @FXML private TableColumn<Customer, String> CountryColumn;
    @FXML private TableColumn<Customer, String> PhoneNumberColumn;

    private ObservableList<Customer> setCustomers;


    public static int id;


    public Customer getCustomer(Customer customer) throws SQLException {
        Customer getCustomer = customer;
        Country c = DBCountry.getCountryByDivisionID(getCustomer.getDivisionID());
        ObservableList<Country> countries = DBCountry.getAllCountries();
        ObservableList<Division> flDivision = DBDivision.getDivisionsByCountryID(c.getCountryID());

        customerIDTextField.setText(String.valueOf(getCustomer.getCustomerID()));
        nameTextField.setText(getCustomer.getCustomerName());
        addressTextField.setText(getCustomer.getAddress());
        PostalCodeTextField.setText(getCustomer.getPostalCode());

        //lambda 2 to set the first level divisions for the modify page. Lambda 1 is in the MainScreenController.java at line 370.
        divisionComboBox.setItems(flDivision);
        flDivision.forEach(FirstLevelDivisions -> {
            if(FirstLevelDivisions.getDivisionID() == customer.getDivisionID())
                divisionComboBox.setValue(FirstLevelDivisions);
        });

        CountryComboBox.setItems(countries);
        CountryComboBox.setValue(c);
        PhoneNumberTextField.setText(getCustomer.getPhoneNumber());

        return getCustomer;
    }


    public void clearButtonHandler(ActionEvent event) {

    }

    @FXML
    public void selectCountryHandler(ActionEvent event) {
        int countryID = CountryComboBox.getValue().getCountryID();
        try {
            divisionComboBox.setItems(DBDivision.getDivisionsByCountryID(countryID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onActionSelectFLD(ActionEvent event) {

    }


    @FXML
    public void saveButtonHandler(ActionEvent event) throws IOException {
        int customerID = Integer.parseInt(customerIDTextField.getText());
        String customerName = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = PostalCodeTextField.getText();
        String phoneNumber = PhoneNumberTextField.getText();
        Division flDivision = divisionComboBox.getValue();
        int divisionID = flDivision.getDivisionID();
        Country country = CountryComboBox.getValue();

        if (customerIDTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || addressTextField.getText().isEmpty() || divisionComboBox.getSelectionModel().isEmpty()
                || PostalCodeTextField.getText().isEmpty() || CountryComboBox.getSelectionModel().isEmpty() || PhoneNumberTextField.getText().isEmpty()) {
            //appointmentAlertsEN(4);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: Blank Fields");
            alert.setContentText("One or more fields have been left blank. Please fill them and try again.");
            alert.showAndWait();
        }

        DBCustomer.updateCustomer(customerName, address, postalCode, phoneNumber, divisionID, customerID);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }


    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
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
            Address1Column.setCellValueFactory(new PropertyValueFactory<>("address"));
            FLDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
            PostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            CountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            PhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

            CountryComboBox.setItems(DBCountry.getAllCountries());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}