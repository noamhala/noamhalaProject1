package com.ariel.noamhalaproject1.models;

import java.io.Serializable;

public class Coach extends User implements Serializable {
    protected String domain;   // Area of expertise (e.g., fitness, sports)
    protected double price;    // Coaching price
    protected int experience;  // Number of years of experience

    // Constructor with all parameters
    public Coach(String id, String fname, String lname, String phone, String email, String pass,
                 String gender, String city, String domain, int experience, double price) {
        super(id, fname, lname, phone, email, pass, gender, city); // Passing to the superclass constructor
        this.domain = domain;
        this.experience = experience;
        this.price = price;
    }




    // Copy constructor
    public Coach(Coach coach) {
        super(coach.getId(), coach.getFname(), coach.getLname(), coach.getPhone(), coach.getEmail(),
                coach.getPass(), coach.getGender(), coach.getCity());  // Copying User data
        this.domain = coach.getDomain();
        this.experience = coach.getExperience();
        this.price = coach.getPrice();
    }

    // Constructor to initialize only domain, experience, and price
    public Coach(String domain, int experience, double price) {
        this.domain = domain;
        this.experience = experience;
        this.price = price;
    }

    // Default constructor
    public Coach() {
        super(); // Call the superclass constructor (if needed, could be modified)
    }

    // Getters and Setters for all fields:

    // Getters and Setters for domain
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    // Getters and Setters for experience
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Getters and Setters for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Getters and Setters for inherited fields (User class)
    public String getId() {
        return super.getId();
    }

    public void setId(String id) {
        super.setId(id);
    }

    public String getFname() {
        return super.getFname();
    }

    public void setFname(String fname) {
        super.setFname(fname);
    }

    public String getLname() {
        return super.getLname();
    }

    public void setLname(String lname) {
        super.setLname(lname);
    }

    public String getPhone() {
        return super.getPhone();
    }

    public void setPhone(String phone) {
        super.setPhone(phone);
    }

    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }

    public String getPass() {
        return super.getPassword();  // Accessing the User class's password via getter
    }

    public void setPass(String pass) {
        super.setPassword(pass);  // Accessing the User class's password via setter
    }

    public String getGender() {
        return super.getGender();
    }

    public void setGender(String gender) {
        super.setGender(gender);
    }

    public String getCity() {
        return super.getCity();
    }

    public void setCity(String city) {
        super.setCity(city);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "domain='" + domain + '\'' +
                ", price=" + price +
                ", experience=" + experience +
                ", id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
