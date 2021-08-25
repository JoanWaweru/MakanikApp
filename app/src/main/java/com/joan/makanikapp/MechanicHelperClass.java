package com.joan.makanikapp;

public class MechanicHelperClass {
    String fname,lname,email,phoneno,password,approved;

    public MechanicHelperClass() {
    }

    public MechanicHelperClass(String fname, String lname, String email, String phoneno, String password, String approved) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phoneno = phoneno;
        this.password = password;

        this.approved = approved;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.fname = approved;
    }
}
