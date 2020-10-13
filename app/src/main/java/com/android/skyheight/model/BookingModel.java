package com.android.skyheight.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookingModel implements Serializable {
    private List<Integer> plot_ids;
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    private String buyer;
    private int paid_amount;
    private int total_amount;

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    private int remaining_amount;
    private List<plot> plot=null;

    public List<com.android.skyheight.model.plot> getPlot() {
        return plot;
    }

    public void setPlot(List<com.android.skyheight.model.plot> plot) {
        this.plot = plot;
    }

    public BookingModel(List<Integer> plot_ids, String buyer, int paid_amount, int remaining_amount,int total_amount) {
        this.plot_ids = plot_ids;
        this.buyer = buyer;
        this.paid_amount = paid_amount;
        this.remaining_amount = remaining_amount;
        this.total_amount=total_amount;
    }

    public List<Integer> getPlot_ids() {
        return plot_ids;
    }

    public void setPlot_ids(List<Integer> plot_ids) {
        this.plot_ids = plot_ids;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(int paid_amount) {
        this.paid_amount = paid_amount;
    }

    public int getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(int remaining_amount) {
        this.remaining_amount = remaining_amount;
    }
}
