package com.example.user.groupexpensetracker.bean;

/**
 * Created by user on 8/22/2016.
 */
public class User {
    private String name;
    private String number;
    private String image;
    private int groupId;
    private String tripImage;
    private int amount;
    private boolean isSelected;
//    private boolean isAdmin;

    public User(String name, String number, String image, boolean isSelected) {
        this.name = name;
        this.number = number;
        this.image = image;
        this.isSelected = isSelected;
    }
//    public User(String name, String number, String image, boolean isSelected,boolean isAdmin) {
//        this.name = name;
//        this.number = number;
//        this.image = image;
//        this.isSelected = isSelected;
//        this.isAdmin = isAdmin;
//
//    }
    public User(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public User(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public User(String name, String number, String image) {
        this.name = name;
        this.number = number;
        this.image = image;
    }

    public User(int groupId, String tripImage) {
        this.groupId = groupId;
        this.tripImage = tripImage;
    }

//    public boolean isAdmin() {
//        return isAdmin;
//    }
//
//    public void setAdmin(boolean admin) {
//        isAdmin = admin;
//    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTripImage() {
        return tripImage;
    }

    public void setTripImage(String tripImage) {
        this.tripImage = tripImage;
    }
}
