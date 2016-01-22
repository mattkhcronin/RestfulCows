package com.example.mcronin.webapiexample;

/**
 * Created by mcronin on 1/22/2016.
 */
public class Cow {

    public Cow(int id, String breed, String color) {
        this.id = id;
        this.breed = breed;
        this.color = color;
    }

    private int id;
    private String breed;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return id + " " + breed + " " + color;
    }

    public String toJSON() {
        return "{\"Id\" : " + id +", \"Breed\" : \"" +  breed + "\", \"Color\" : \""+ color + "\"}";
    }
}
