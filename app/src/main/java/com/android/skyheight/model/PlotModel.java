package com.android.skyheight.model;

public class PlotModel {
    public String plot_number;
    public String size;
    public String plot_description;
    public String broker;
    public String site;

    public PlotModel(String plot_number, String size, String plot_description, String broker, String site) {
        this.plot_number = plot_number;
        this.size = size;
        this.plot_description = plot_description;
        this.broker = broker;
        this.site = site;
    }

    public String getPlot_number() {
        return plot_number;
    }

    public void setPlot_number(String plot_number) {
        this.plot_number = plot_number;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPlot_description() {
        return plot_description;
    }

    public void setPlot_description(String plot_description) {
        this.plot_description = plot_description;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
