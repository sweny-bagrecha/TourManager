package com.example.user.groupexpensetracker;

/**
 * Created by user on 12/22/2016.
 */

public class Message {

    private String fromImage, message;
    private boolean isSelf;

    public Message()
    {

    }

    public Message(String fromImage, String message, boolean isSelf) {
        this.fromImage = fromImage;
        this.message = message;
        this.isSelf = isSelf;
    }

    public String getFromImage() {
        return fromImage;
    }

    public void setFromImage(String fromImage) {
        this.fromImage = fromImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
