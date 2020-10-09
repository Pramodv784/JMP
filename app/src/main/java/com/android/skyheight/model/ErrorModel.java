package com.android.skyheight.model;

import java.util.ArrayList;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorModel {

    @SerializedName("non_field_errors")
    @Expose
    private List<String> nonFieldErrors = null;
    @SerializedName("username")
    @Expose
    private List<String> username=null;

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<String> getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(List<String> mobile_number) {
        this.mobile_number = mobile_number;
    }

    @SerializedName("mobile_number")
    @Expose
    private List<String> mobile_number=null;
    public List<String> getNonFieldErrors() {
        return nonFieldErrors;
    }

    public void setNonFieldErrors(List<String> nonFieldErrors) {
        this.nonFieldErrors = nonFieldErrors;
    }

}