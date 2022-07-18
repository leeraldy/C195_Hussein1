package DBAccess;

import utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import java.sql.*;


public class DBContact {

    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactNameList = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * from contacts");

            ResultSet rs = ps.executeQuery();

            while ((rs.next())) {
                String name = rs.getString("Contact_ID") + ": " +  rs.getString("Contact_Name");

                contactNameList.add(name);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return contactNameList;
    }


    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * from contacts");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                Contact contacts = new Contact(contactID, contactName, contactEmail);
                contactList.add(contacts);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

}