package com.android.skyheight.model;

import java.io.Serializable;
import java.util.List;

public class PlotDataModel implements Serializable {
    private  Integer id;
    private  Integer buyer_mobile_number;
    private int paidAmount;
    private int remaining_amount;
    private int total_amount;
    private int buyer;
    private List<PlotListModel> plot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuyer_mobile_number() {
        return buyer_mobile_number;
    }

    public void setBuyer_mobile_number(Integer buyer_mobile_number) {
        this.buyer_mobile_number = buyer_mobile_number;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(int remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getBuyer() {
        return buyer;
    }

    public void setBuyer(int buyer) {
        this.buyer = buyer;
    }

    public List<PlotListModel> getPlot() {
        return plot;
    }

    public void setPlot(List<PlotListModel> plot) {
        this.plot = plot;
    }
}
