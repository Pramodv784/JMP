package com.android.skyheight.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BookingListModel implements Serializable {
    private String id;
    private int paid_amount;
    private int total_amount;
    private int remaining_amount;
    private ArrayList<PlotListModel> plot=null;
    private SiteListModel site;
    private UserDetail buyer;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public int getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(int paid_amount) {
        this.paid_amount = paid_amount;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(int remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public ArrayList<PlotListModel> getPlot() {
        return plot;
    }

    public void setPlot(ArrayList<PlotListModel> plot) {
        this.plot = plot;
    }

    public SiteListModel getSite() {
        return site;
    }

    public void setSite(SiteListModel site) {
        this.site = site;
    }

    public UserDetail getBuyer() {
        return buyer;
    }

    public void setBuyer(UserDetail buyer) {
        this.buyer = buyer;
    }
}
