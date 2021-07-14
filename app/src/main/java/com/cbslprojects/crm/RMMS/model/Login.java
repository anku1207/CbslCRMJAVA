package com.cbslprojects.crm.RMMS.model;

public class Login {
    private String Status;
    private String Message;
    private Login Data;

    private String ID;
    private String Email;
    private String FirstName;
    private String LastName;
    private String ZoneID;
    private String HMAC_Triple_DES;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Login getData() {
        return Data;
    }

    public void setData(Login data) {
        Data = data;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getZoneID() {
        return ZoneID;
    }

    public void setZoneID(String zoneID) {
        ZoneID = zoneID;
    }

    public String getHMAC_Triple_DES() {
        return HMAC_Triple_DES;
    }

    public void setHMAC_Triple_DES(String HMAC_Triple_DES) {
        this.HMAC_Triple_DES = HMAC_Triple_DES;
    }
}
