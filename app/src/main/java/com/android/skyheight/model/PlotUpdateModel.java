package com.android.skyheight.model;

import java.io.Serializable;

public class PlotUpdateModel implements Serializable {
    private String plot_number;
    private String description;
    private int size;
    private String id;
    private String broker;
    private String plot_owner;
    private int site;
    private Boolean status;

    public String getPlot_owner() {
        return plot_owner;
    }

    public void setPlot_owner(String plot_owner) {
        this.plot_owner = plot_owner;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }




    public String getPlot_number() {
        return plot_number;
    }

    public void setPlot_number(String plot_number) {
        this.plot_number = plot_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public PlotUpdateModel(String plot_number,
                           String description, int size, String id, int site,
                           Boolean status,String
                                   broker,String plot_owner) {
        this.plot_number = plot_number;
        this.description = description;
        this.size = size;
        this.status = status;
        this.id = id;
        this.broker=broker;
        this.site = site;
        this.plot_owner=plot_owner;

    }
}
