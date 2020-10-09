package com.android.skyheight.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SiteListModel implements Serializable {
private String price;
private String image;
private String id;
private String area;
private String description;
private String file;
private String name;
ArrayList<AddressModel> location ;
 public UserDetail owner;
 public AddressModel site_location;
    @SerializedName("partner")
    @Expose
    private List<UserDetail> partner = null;

    public List<UserDetail> getPartner() {
        return partner;
    }

    public void setPartner(List<UserDetail> partner) {
        this.partner = partner;
    }

    public AddressModel getSite_location() {
        return site_location;
    }

    public void setSite_location(AddressModel site_location) {
        this.site_location = site_location;
    }

    public UserDetail getOwner() {
        return owner;
    }

    public void setOwner(UserDetail owner) {
        this.owner = owner;
    }

    public SiteListModel(String price, String image, String id,
                         String area, String description, String file,
                          String name
                          ,UserDetail owner,AddressModel site_location) {
        this.price = price;
        this.image = image;
        this.id = id;
        this.area = area;
        this.owner=owner;
        this.description = description;
        this.file = file;
        this.site_location=site_location;
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
