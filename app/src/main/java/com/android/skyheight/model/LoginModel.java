package com.android.skyheight.model;

public class LoginModel {
    public String mobile_number;
    public String password;
    public String token;
    public String username;
    public String address;
    public String type;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LoginModel(String mobile_number, String password, String token, String username, String address,String type,String id) {
        this.mobile_number = mobile_number;
        this.password = password;
        this.token =token;
        this.username=username;
        this.address=address;
        this.type=type;
        this.id=id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }


    public void setToken(String token) {
        this.token = token;
    }
}
