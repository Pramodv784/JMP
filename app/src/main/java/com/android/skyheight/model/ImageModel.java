package com.android.skyheight.model;

public class ImageModel {
    private String Image;
    private String id;
    private String Site;

    public ImageModel(String image, String site) {
        Image = image;
        Site = site;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return Site;
    }

    public void setSite(String site) {
        Site = site;
    }
}
