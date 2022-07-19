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


public class AddCustomer implements Initializable {

    @FXML Label CustomerIDLabel;
    @FXML Label NameLabel;
    @FXML Label Address1Label;
    @FXML Label FLDivisionLabel;
    @FXML Label PostalCodeLabel;
    @FXML Label CountryLabel;
    @FXML Label PhoneNumberLabel;
    @FXML TextField CustomerIDTextField;
    @FXML TextField NameTextField;
    @FXML TextField Address1TextField;
    @FXML ComboBox<Division> FLDivisionComboBox;
    @FXML TextField PostalCodeTextField;
    @FXML ComboBox<Country> CountryComboBox;
    @FXML TextField PhoneNumberTextField;
    @FXML Button SaveButton;
    @FXML Button CancelButton;
    @FXML TableView<Customer> CustomerTableView;
    @FXML TableColumn<Customer, Integer> CustomerIDColumn;
    @FXML TableColumn<Customer, String> NameColumn;
    @FXML TableColumn<Customer, String> Address1Column;
    @FXML TableColumn<Customer, String> FLDivisionColumn;
    @FXML TableColumn<Customer, String> PostalCodeColumn;
    @FXML TableColumn<Customer, String> CountryColumn;
    @FXML TableColumn<Customer, String> PhoneNumberColumn;

     ObservableList<Customer> setCustomers;

    public static int id;


    @FXML
    public void saveButtonHandler(ActionEvent event) throws IOException {
        int customerID = id++;
        String customerName = NameTextField.getText();
        String address = Address1TextField.getText();
        String postalCode = PostalCodeTextField.getText();
        String phoneNumber = PhoneNumberTextField.getText();
        Division flDivision = FLDivisionComboBox.getValue();
        int divisionID = flDivision.getDivisionID();
        Country country = CountryComboBox.getValue();

        if (CustomerIDTextField.getText().isEmpty() || NameTextField.getText().isEmpty() || Address1TextField.getText().isEmpty() || FLDivisionComboBox.getSelectionModel().isEmpty()
                || PostalCodeTextField.getText().isEmpty() || CountryComboBox.getSelectionModel().isEmpty() || PhoneNumberTextField.getText().isEmpty()) {
            //appointmentAlertsEN(4);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Warning: Blank Fields");
            alert.setContentText("One or more fields have been left blank. Please fill them and try again.");
            alert.showAndWait();
        }

        DBCustomer.addCustomer(customerName, address, postalCode, phoneNumber, divisionID);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }


    @FXML
    public void selectCountry(ActionEvent event) throws SQLException {

        int countryID = CountryComboBox.getValue().getCountryID();
        try {
            FLDivisionComboBox.setItems(DBDivision.getDivisionsByCountryID(countryID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void selectDivision(ActionEvent event) throws SQLException {

    }


    @FXML
    public void cancelButtonHandler(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Main");
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO
        CustomerIDTextField.setText(Integer.toString(id));

        //This is to populate the customer table view.
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