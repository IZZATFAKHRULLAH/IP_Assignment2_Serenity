package com.example.bmiapp.model;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id", nullable = false, length = 20)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "yob")
    private int yob;

    @Column(name = "weight")
    private double weight;

    @Column(name = "height")
    private double height;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "interests", length = 500)
    private String interests;  // Stored as comma-separated string in DB

    @Transient  // Not stored in database
    private String[] interestsArray;

    public Person() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYob() {
        return yob;
    }

    public void setYob(int yob) {
        this.yob = yob;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter for database string
    public String getInterests() {
        return interests;
    }

    // Setter for database string
    public void setInterests(String interests) {
        this.interests = interests;
    }

    // Helper method to get interests as array
    @Transient
    public String[] getInterestsArray() {
        if (interestsArray == null && interests != null && !interests.isEmpty()) {
            interestsArray = interests.split(",");
        }
        return interestsArray;
    }

    // Helper method to set interests from array
    @Transient
    public void setInterestsArray(String[] interestsArray) {
        this.interestsArray = interestsArray;
        if (interestsArray != null && interestsArray.length > 0) {
            this.interests = String.join(",", interestsArray);
        } else {
            this.interests = null;
        }
    }

    @Transient
    public double getBmi() {
        if (height <= 0) {
            return 0;
        }
        return weight / (height * height);
    }

    @Transient
    public String getCategory() {
        double bmi = getBmi();
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", yob=" + yob +
                ", weight=" + weight +
                ", height=" + height +
                ", gender='" + gender + '\'' +
                ", interests='" + interests + '\'' +
                '}';
    }
}