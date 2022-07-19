package model;

import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private int contactID;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;


    //Constructor

    public Appointment(int appointmentID, String title, String description, String location, int contactID, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;

    }

 // Setters and Getters
    public int getAppointmentID() {
        return appointmentID;
    }


    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public LocalDateTime getStart() {
        return start;
    }


    public void setStart(LocalDateTime start) {
        this.start = start;
    }


    public LocalDateTime getEnd() {
        return end;
    }


    public void setEnd(LocalDateTime end) {
        this.end = end;
    }


    public int getCustomerID() {
        return customerID;
    }


    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


    public int getUserID() {
        return userID;
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }

}