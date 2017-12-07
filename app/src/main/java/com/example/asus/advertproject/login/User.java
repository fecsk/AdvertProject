package com.example.asus.advertproject.login;

/**
 * Created by Asus on 2017. 11. 22..
 */

public class User {
    private String u_id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public User (Builder builder){
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.u_id = builder.Uid;
        this.phoneNumber = builder.phoneNumber;
    }

    public static class Builder{
        String Uid;
        String firstName;
        String lastName;
        String phoneNumber;

        public Builder() {
        }
        public Builder U_id(String bUid) {
            Uid = bUid;
            return this;
        }
        public Builder firstName(String bfname) {
            firstName = bfname;
            return this;
        }
        public Builder lastName(String blname) {
            lastName = blname;
            return this;
        }
        public Builder phoneNumber(String bNumber) {
            phoneNumber = bNumber;
            return this;
        }

        public User bulid(){
            return new User(this);
        }
    }

    public String getU_id() { return u_id; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() { return phoneNumber; }
}