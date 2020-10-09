package com.android.skyheight.model;

import java.io.Serializable;

public class AddressModel implements Serializable {
    String address;
    String street;
    String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AddressModel(String address, String street, String city) {
        this.address = address;
        this.street = street;
        this.city = city;
    }
}
