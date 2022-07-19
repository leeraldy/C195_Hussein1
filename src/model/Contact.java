package model;


public class Contact {

    private int contactID;
    private String contactName;


    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
    }


    public int getContactID() {
        return contactID;
    }


    public void setContactID(int contactID) {
        this.contactID = contactID;
    }


    public String getContactName() {
        return contactName;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


}