package controller;

import DBAccess.DBAppointment;
import DBAccess.DBUser;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Appointment;


public class Login implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label userNameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label timeZoneLabel;
    @FXML private TextField userNameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private TextField timeZoneTxt;
    @FXML private Button loginButton;
    @FXML private Button clearButton;
    @FXML private Button exitButton;


    @FXML
    public void loginButtonHandler(ActionEvent event) throws IOException, SQLException {
        System.out.println("Login Button was pressed");


        if (userNameTextField.getText().isEmpty()) {
            if (Locale.getDefault().toString().equals("en_US")) {
                //loginScreenErrorsEN(1);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error: Username Field Is Blank");
                alert.setContentText("Please enter a username");
                alert.showAndWait();
                return;
            }
            if (Locale.getDefault().toString().equals("fr_FR")) {
                //loginScreenErrorsFR(1);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERREUR");
                alert.setHeaderText("Erreur : le champ du nom d'utilisateur est vide");
                alert.setContentText("Merci d'entrer un nom d'utilisateur");
                alert.showAndWait();
                return;
            }
        }
        if (passwordTextField.getText().isEmpty()) {
            if (Locale.getDefault().toString().equals("en_US")) {
                //loginScreenErrorsEN(2);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error: Password Field Is Blank");
                alert.setContentText("Please enter a password");
                alert.showAndWait();
                return;
            }
            if (Locale.getDefault().toString().equals("fr_FR")) {
                //loginScreenErrorsFR(2);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERREUR");
                alert.setHeaderText("Erreur : le champ du mot de passe est vide");
                alert.setContentText("Merci d'entrer un mot de passe");
                alert.showAndWait();
                return;
            }
        }
        String user = userNameTextField.getText();
        String password = passwordTextField.getText();

        boolean loginSuccess = DBUser.validateUserLogin(user, password);

        if (loginSuccess) {
            appointmentIn15mins();
            loginFileSuccess(user);
            try {

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.setTitle("Main");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginFileFail(user);
            if (Locale.getDefault().toString().equals("en_US")) {
                //loginScreenErrorsEN(3);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Error: Incorrect Username And/Or Password");
                alert.setContentText("Please try again.");
                alert.showAndWait();
                return;
            }
            if (Locale.getDefault().toString().equals("fr_FR")) {
                //loginScreenErrorsFR(3);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERREUR");
                alert.setHeaderText("Erreur : Nom d'utilisateur et/ou mot de passe incorrects");
                alert.setContentText("Veuillez essayer à nouveau.");
                alert.showAndWait();
                return;
            }
        }

    }

    @FXML
    public void clearButtonHandler(ActionEvent event) throws IOException {
        System.out.println("Clear Button was pressed");
        userNameTextField.clear();
        passwordTextField.clear();
    }

    @FXML
    public void exitButtonHandler(ActionEvent event) throws IOException {
        System.out.println("Exit Button was pressed");
        System.exit(0);
    }


    public void appointmentIn15mins() throws SQLException {
        ObservableList<Appointment> appointments = DBAppointment.getAllAppointments();
        ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();

        if (appointments != null) {
            for (Appointment apptIn15 : appointments) {
                LocalDateTime ldtNow = LocalDateTime.now();
                LocalDateTime ldtIn15 = ldtNow.plusMinutes(15);

                if (apptIn15.getStart().isAfter(ldtNow) && apptIn15.getStart().isBefore(ldtIn15)) {
                    upcomingAppointments.add(apptIn15);
                    if (Locale.getDefault().toString().equals("en_US")) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("WARNING");
                        alert.setHeaderText("Upcoming Appointment");
                        alert.setContentText("Upcoming appointment in 15 minutes. Appointment ID: " + apptIn15.getAppointmentID() + ". Date and Time: " + apptIn15.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ".");
                        alert.showAndWait();
                        return;
                    }
                    if (Locale.getDefault().toString().equals("fr_FR")) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("AVERTISSEMENT");
                        alert.setHeaderText("Rendez-vous à venir");
                        alert.setContentText("Prochain rendez-vous dans 15 minutes. ID de rendez-vous: " + apptIn15.getAppointmentID() + ". Date et l'heure: " + apptIn15.getStart() + ".");
                        alert.showAndWait();
                        return;
                    }
                }

            }
            if (upcomingAppointments.size() < 1) {
                if (Locale.getDefault().toString().equals("en_US")) {
                    //alert15minsEN(2);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("WARNING");
                    alert.setHeaderText("No Upcoming Appointment");
                    alert.setContentText("There's no upcoming appointments at this time.");
                    alert.showAndWait();
                    return;
                }
                if (Locale.getDefault().toString().equals("fr_FR")) {
                    //alert15minsFR(2);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("AVERTISSEMENT");
                    alert.setHeaderText("Aucun rendez-vous à venir");
                    alert.setContentText("Aucun rendez-vous à venir pour le moment.");
                    alert.showAndWait();
                    return;
                }
            }
        }
    }


    public static void loginFileSuccess(String user) throws IOException {
        FileWriter fw = new FileWriter("login_activity.txt", true);
        PrintWriter pw = new PrintWriter(fw);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String loginTime = dtf.format(now);

        pw.println(user + " successfully logged in at " + loginTime);
        pw.close();
    }


    public static void loginFileFail(String user) throws IOException {
        FileWriter fw = new FileWriter("login_activity.txt", true);
        PrintWriter pw = new PrintWriter(fw);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String loginTime = dtf.format(now);

        pw.println(user + " unsuccessfully logged in at " + loginTime);
        pw.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ResourceBundle resourceb = ResourceBundle.getBundle("main/Lang", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
            ZoneId z = ZoneId.systemDefault();
            titleLabel.setText(resourceb.getString("TitleLabel"));
            userNameLabel.setText(resourceb.getString("userNameLabel"));
            passwordLabel.setText(resourceb.getString("passwordLabel"));
            timeZoneLabel.setText(resourceb.getString("timeZoneLabel"));
            timeZoneTxt.setText(z.toString());
            loginButton.setText(resourceb.getString("loginButton"));
            clearButton.setText(resourceb.getString("clearButton"));
            exitButton.setText(resourceb.getString("exitButton"));
        }
    }

}