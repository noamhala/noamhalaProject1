package com.ariel.noamhalaproject1.models;

import java.io.Serializable;

public class User implements Serializable {
    // Fields for the User class
    String id, fname, lname, phone, email, password, city, typeUser;

    public User(String id, String fname, String lname, String phone, String email, String password, String city, String typeUser) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.city = city;
        this.typeUser = typeUser;
    }

    // Constructor with mandatory parameters (id, fname, lname, phone, email, and typeUser)
    public User(String id, String fname, String lname, String phone, String email, String typeUser) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.password = "";  // Default empty password
        this.city = "";      // Default empty city
        this.typeUser = typeUser;
    }

    public User(User other) {
        this.id = other.id;
        this.fname = other.fname;
        this.lname = other.lname;
        this.phone = other.phone;
        this.email = other.email;
        this.password = other.password;
        this.city = other.city;
        this.typeUser = other.typeUser;
    }

    // Default constructor
    public User() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", typeUser='" + typeUser + '\'' +
                '}';
    }
}
