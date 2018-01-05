package com.example.user.groupexpensetracker;

/**
 * Created by user on 8/22/2016.
 */
public class User
{
    private String name;
    private String number;
    private String image;

    public User(String name, String number) {
        this.name = name;
        this.number = number;
    }


    public User(String name, String number, String image) {
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
