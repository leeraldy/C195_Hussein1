package DBAccess;

import utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import java.sql.*;


public class DBCustomer {


    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM customers AS c INNER JOIN first_level_divisions AS fld ON c.Division_ID = fld.Division_ID INNER JOIN countries AS co ON co.Country_ID = fld.Country_ID");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String division = rs.getString("Division");
                String postalCode = rs.getString("Postal_Code");
                String country = rs.getString("Country");
                String phoneNumber = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");

                Customer cust = new Customer(customerID, customerName, address, division, postalCode, country, phoneNumber, divisionID);
                customerList.add(cust);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }


    public static void addCustomer(String customerName, String address, String postalCode, String phone, Integer divisionID) {

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (? , ?, ?, ?, ?)");
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionID);

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateCustomer(String customerName, String address, String postalCode, String phone, Integer divisionID, int customerID) {

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("UPDATE customers SET Customer_name = ? , Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?");
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divisionID);
            ps.setInt(6, customerID);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static boolean deleteCustomer(int customerID) throws SQLException {
        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("DELETE from customers WHERE Customer_ID=?");

            ps.setInt(1, customerID);

            ps.executeUpdate();
            if (ps.getUpdateCount() > 0) {
                System.out.println("Row affected: " + ps.getUpdateCount());
            } else {
                System.out.println("No change occurred.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

}