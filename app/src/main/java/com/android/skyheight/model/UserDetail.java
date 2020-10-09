package com.android.skyheight.model;

import java.io.Serializable;

public class UserDetail implements Serializable {
    public  String username;
    public  String password;
    public  String confirm_password;
    public  String mobile_number;
    public  String type;
    public  String address;
    public  String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDetail(String username, String password, String confirm_password,
                      String mobile_number, String type, String address,String status) {
        this.username = username;
        this.password = password;
        this.confirm_password = confirm_password;
        this.mobile_number = mobile_number;
        this.type = type;
        this.status=status;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
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
}
