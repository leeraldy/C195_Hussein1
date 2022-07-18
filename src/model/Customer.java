package model;


public class Customer {

    private int customerID;
    private String customerName;
    private String country;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String division;
    private int divisionID;


    public Customer(int customerID, String customerName, String address, String division, String postalCode, String country, String phoneNumber, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.division = division;
        this.postalCode = postalCode;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
    }


    public int getCustomerID() {
        return customerID;
    }


    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


    public String getCustomerName() {
        return customerName;
    }


    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getDivision() {
        return division;
    }


    public void setDivision(String division) {
        this.division = division;
    }


    public String getPostalCode() {
        return postalCode;
    }


    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public int getDivisionID() {
        return divisionID;
    }


    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

}