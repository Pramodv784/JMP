package com.android.skyheight.model;

import java.io.Serializable;

public class UserList implements Serializable {

    private String username;
    private String user_image;
    private String mobile_number;
    private String type;
    private String id;
    private boolean is_active;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public UserList(String username, String user_image,String mobile_number,String type,boolean is_active) {
        this.username = username;
        this.user_image = user_image;
        this.mobile_number=mobile_number;
        this.type=type;
        this.is_active=is_active;
    }

    public String getMobile() {
        return mobile_number;
    }

    public void setMobile(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    public String getUser_name() {
        return username;
    }

    public void setUser_name(String username) {
        this.username = username;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }





}
