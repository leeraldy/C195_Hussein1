package DBAccess;

import utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import model.Division;

/**
 * DBDivision Class: Manages Division in DB
 */



public class DBDivision {

    public static ObservableList<Division> getAllDivisions() {

        ObservableList<Division> divisions = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM first_level_divisions");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                int countryID = rs.getInt("Country_ID");
                Division dv = new Division(divisionID, division, countryID);
                divisions.add(dv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisions;
    }

    public static void getDivisionByID(int customerID) throws SQLException {

        String divisionName = null;

        try {
            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Division FROM first_level_divisions WHERE Division_ID = ?");
            ps.setInt(1, customerID);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                divisionName = rs.getString("Division");
            } else {
                System.out.println("No division by that ID is found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }


    public static ObservableList<Division> getDivisionsByCountryID(int countryID) throws SQLException {

        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT * FROM first_level_divisions WHERE Country_ID = ?");
            ps.setInt(1, countryID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                rs.getInt("Country_ID");
                Division dv1 = new Division(divisionID, division, countryID);
                divisionList.add(dv1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionList;
    }

    public static String getDivisionNameByID(int customerID) throws SQLException {
        String divisionName = null;

        try {
            PreparedStatement ps = DBConnection.dbConn().prepareStatement("SELECT Division FROM first_level_divisions WHERE Division_ID = ?");

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                divisionName = rs.getString("Division");
            }
            else
            {
                System.out.println("No division by that ID is found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return divisionName;

    }

}