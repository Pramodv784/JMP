package com.android.skyheight.model;

import java.io.Serializable;

public class plot implements Serializable {
    private int id;
    private int site;

    private int plot_number;
    private boolean status;
    private int size;
    private String plot_description;
    private int brokerage;
    private String plot_owner;
    private int broker;
    private int total_plot_amount;

    public int getTotal_plot_amount() {
        return total_plot_amount;
    }
    public void setTotal_plot_amount(int total_plot_amount) {
        this.total_plot_amount = total_plot_amount;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getPlot_number() {
        return plot_number;
    }

    public void setPlot_number(int plot_number) {
        this.plot_number = plot_number;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPlot_description() {
        return plot_description;
    }

    public void setPlot_description(String plot_description) {
        this.plot_description = plot_description;
    }

    public int getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(int brokerage) {
        this.brokerage = brokerage;
    }

    public String getPlot_owner() {
        return plot_owner;
    }

    public void setPlot_owner(String plot_owner) {
        this.plot_owner = plot_owner;
    }

    public int getBroker() {
        return broker;
    }

    public void setBroker(int broker) {
        this.broker = broker;
    }
}