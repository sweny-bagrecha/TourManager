package com.example.user.groupexpensetracker;

/**
 * Created by Tanuja on 12-09-2016.
 */
public class Members {
    private String member,amount;

    public Members(String member, String amount) {
        this.member = member;
        this.amount = amount;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
