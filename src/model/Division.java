package model;

/**
 * Division class: Manages all First Level Division objects
 *
 * @author Hussein Coulibaly
 */

public class Division {

    private int divisionID;
    private String division;
    private int countryID;


    public Division(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    // The getter for DivisionID
    public int getDivisionID() {
        return divisionID;
    }


     // The setter for divisionID
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    // The getter for division
    public String getDivision() {
        return division;
    }

    // The setter for division
    public void setDivision(String division) {
        this.division = division;
    }

    // The getter for countryID
    public int getCountryID() {
        return countryID;
    }

    // The setter for countryID
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }


    @Override
    public String toString() {
        return (division);
    }

}