package DBAccess;

import utils.DBConnection;
import model.Country;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


public class DBCountry {


    public static ObservableList<Country> getAllCountries() throws SQLException {

        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * from countries");

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


    public static Country getCountryByDivisionID(int divisionID) throws SQLException {

        try {
            String sql = "SELECT * from countries AS c INNER JOIN first_level_divisions AS fld ON c.Country_ID = fld.Country_ID WHERE fld.Division_ID = ?";

            PreparedStatement ps = DBConnection.dbConn().prepareStatement(sql);
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