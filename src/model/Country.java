package model;


public class Country {

    private int countryID;
    private String countryName;


    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }


    public int getCountryID() {
        return countryID;
    }


    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }


    public String getCountryName() {
        return countryName;
    }


    public void setCountry(String country) {
        this.countryName = country;
    }


    @Override
    public String toString() {
        return (countryName);
    }

}