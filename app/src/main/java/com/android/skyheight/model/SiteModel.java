package com.android.skyheight.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import okhttp3.RequestBody;

public class SiteModel implements Serializable {
    String name;
    String area;
    String description;
    @SerializedName("file")
    @Expose
    private Object file;
    @SerializedName("image")
    @Expose
    private String image;

    public Object getFile() {
        return file;
    }

    public void setFile(Object file) {
        this.file = file;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("price")
    String price;
    String id;
    String address_id;

    public SiteModel(RequestBody address_id1, RequestBody name, RequestBody area1, RequestBody price1, RequestBody description1, RequestBody id) {


    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        this.price = price;
    }

    public SiteModel(String name, String area, String description, String image,Object file
                     ,String price,String address_id) {
        this.name = name;
        this.area = area;
        this.description = description;
        this.image=image;
        this.file=file;
        this.price=price;
        this.address_id=address_id;

    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getarea() {
        return area;
    }

    public void setarea(String area) {
        this.area = area;
    }


    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

}
