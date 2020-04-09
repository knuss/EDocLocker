package com.example.e_doclocker;

import android.graphics.Bitmap;

import java.sql.Blob;

public class Citizen {
    private String nic;
    private String name,email,mpbile,dob,city,password;
    private Bitmap image;

    public Citizen(String nic, String name, String email, String mpbile, String dob, String city, String password, Bitmap image) {
        this.nic = nic;
        this.name = name;
        this.email = email;
        this.mpbile = mpbile;
        this.dob = dob;
        this.city = city;
        this.password = password;
        this.image = image;
    }

    public Citizen(String nic, String name, String email, String mpbile, String dob, String city) {
        this.nic = nic;
        this.name = name;
        this.email = email;
        this.mpbile = mpbile;
        this.dob = dob;
        this.city = city;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMpbile() {
        return mpbile;
    }

    public void setMpbile(String mpbile) {
        this.mpbile = mpbile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return " NIC : " + nic +
                "\n NAME : '" + name + '\'' +
                "\n EMAIL : '" + email + '\'' +
                "\n Mobile : '" + mpbile + '\'' +
                "\n DOB : '" + dob + '\'' +
                "\n CITY : '" + city + '\'';
    }
}
