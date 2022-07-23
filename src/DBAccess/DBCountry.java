package DBAccess;

import utils.DBConnection;
import model.Country;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Handles countries queries
 */


public class DBCountry {


    public static ObservableList<Country> getAllCountries() throws SQLException {

        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM countries");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                Country countries = new Country(countryID, country);
                countryList.add(countries);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryList;
    }

    public static Country getCountryNameByID(String countryName) throws SQLException {

        try{
            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM countries WHERE Country_ID = ?");
            ps.setString(1, countryName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Country countries = new Country(
                        rs.getInt("Country_ID"),
                        rs.getString("Country"));

                return countries;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Country getCountryByDivisionID(int divisionID) throws SQLException {

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM countries WHERE fld.Division_ID = ?");
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Country countries = new Country(
                        rs.getInt("Country_ID"),
                        rs.getString("Country"));

                return countries;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

}