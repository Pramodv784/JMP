package com.android.skyheight.model;

public class PlotsModel {
    public String no_plots;
    public String id;
    public String site;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public PlotsModel(String no_plots, String id, String site) {
        this.no_plots = no_plots;
        this.id = id;
        this.site=site;
    }

    public String getNo_plots() {
        return no_plots;
    }

    public void setNo_plots(String no_plots) {
        this.no_plots = no_plots;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
