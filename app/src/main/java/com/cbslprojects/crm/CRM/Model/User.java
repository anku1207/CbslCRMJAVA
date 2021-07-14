package com.cbslprojects.crm.CRM.Model;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private ArrayList<String> emaillist;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        emaillist=new ArrayList<>();
        emaillist.add(email);
    }

    public ArrayList<String> getEmaillist() {
        return emaillist;
    }

    public void setEmaillist(ArrayList<String> emaillist) {
        this.emaillist = emaillist;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
