package com.android.skyheight.model;

import java.io.Serializable;

public class PlotListModel  implements Serializable {

    private String plot_number;
    private String description;
    private int size;
    private Boolean status;
    private String id;
    private int total_plot_amount;

    public int getTotal_plot_amount() {
        return total_plot_amount;
    }

    public void setTotal_plot_amount(int total_plot_amount) {
        this.total_plot_amount = total_plot_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlotListModel(String plot_number, String description, int size, Boolean status, String id) {
        this.plot_number = plot_number;
        this.description = description;
         this.size=size;
        this.status = status;
        this.id=id;
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
}
