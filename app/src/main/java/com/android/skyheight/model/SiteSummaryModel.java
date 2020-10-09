package com.android.skyheight.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SiteSummaryModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("total_site_expense")
    @Expose
    private Integer totalSiteExpense;
    @SerializedName("site_expense_remarks")
    @Expose
    private String siteExpenseRemarks;
    @SerializedName("construction_material")
    @Expose
    private Integer constructionMaterial;
    @SerializedName("labour_charges")
    @Expose
    private Integer labourCharges;
    @SerializedName("electricity")
    @Expose
    private Integer electricity;
    @SerializedName("maintenance")
    @Expose
    private Integer maintenance;
    @SerializedName("others")
    @Expose
    private Integer others;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    private SiteListModel siteListModel;

    public SiteListModel getSiteListModel() {
        return siteListModel;
    }

    public void setSiteListModel(SiteListModel siteListModel) {
        this.siteListModel = siteListModel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalSiteExpense() {
        return totalSiteExpense;
    }

    public void setTotalSiteExpense(Integer totalSiteExpense) {
        this.totalSiteExpense = totalSiteExpense;
    }

    public String getSiteExpenseRemarks() {
        return siteExpenseRemarks;
    }

    public void setSiteExpenseRemarks(String siteExpenseRemarks) {
        this.siteExpenseRemarks = siteExpenseRemarks;
    }

    public Integer getConstructionMaterial() {
        return constructionMaterial;
    }

    public void setConstructionMaterial(Integer constructionMaterial) {
        this.constructionMaterial = constructionMaterial;
    }

    public Integer getLabourCharges() {
        return labourCharges;
    }

    public void setLabourCharges(Integer labourCharges) {
        this.labourCharges = labourCharges;
    }

    public Integer getElectricity() {
        return electricity;
    }

    public void setElectricity(Integer electricity) {
        this.electricity = electricity;
    }

    public Integer getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Integer maintenance) {
        this.maintenance = maintenance;
    }

    public Integer getOthers() {
        return others;
    }

    public void setOthers(Integer others) {
        this.others = others;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}
