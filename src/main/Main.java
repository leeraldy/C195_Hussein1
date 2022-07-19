package main;

import utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{

    public static void main(String[] args) {
        // TODO code application logic here
        DBConnection.startConnection();
        launch(args);
        DBConnection.closedConnection();
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Appointment Manager");
        stage.show();
    }

}