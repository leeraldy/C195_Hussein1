package DBAccess;

import utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;


public class DBUser {


    public static ObservableList<User> getAllUsers() throws SQLException {

        ObservableList<User> userList = FXCollections.observableArrayList();

        try {

            PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * from users;");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");

                User users = new User(userID, username, password);
                userList.add(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }


    public static boolean validateUserLogin(String username, String password) throws SQLException {
        try {

            PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT User_Name, Password FROM users WHERE User_Name = ? and Password = ?");

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}