package com.ariel.noamhalaproject1.models;

import java.io.Serializable;


public class Trainee extends User implements Serializable {
    double height,Weight;
    int age;

    User myCoach;

    public Trainee() {
        super();
    }



    public Trainee(User user, double height, double weight, int age, User myCoach) {
        super(user);
        this.height = height;
        this.Weight = weight;
        this.age = age;
        this.myCoach = myCoach;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User getCoach() {
        return myCoach;
    }

    public void setCoach(User myCoach) {
        this.myCoach = myCoach;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                super.toString() +
                ", height=" + height +
                ", Weight=" + Weight +
                ", age=" + age +
                ", myCoach=" + myCoach +
                '}';
    }
}
