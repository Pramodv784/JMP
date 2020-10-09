package com.android.skyheight.model;

import java.io.Serializable;

public class SiteExpenseModel implements Serializable {
   public String Site;
   public String site_expense_remarks;
   public float construction_material;
   public float labour_charges;
   public float electricity;
   public float maintenance;
   public float others;


    public SiteExpenseModel( String site_expense_remarks, float construction_material,
                            float labour_charges, float electricity, float maintenance,
                            float others,String Site) {
        this.Site = Site;
        this.site_expense_remarks = site_expense_remarks;
        this.construction_material = construction_material;
        this.labour_charges = labour_charges;
        this.electricity = electricity;
        this.maintenance = maintenance;
        this.others = others;

    }

    public String getSite() {
        return Site;
    }

    public void setSite(String site) {
        this.Site = site;
    }

    public String getSite_expense_remarks() {
        return site_expense_remarks;
    }

    public void setSite_expense_remarks(String site_expense_remarks) {
        this.site_expense_remarks = site_expense_remarks;
    }

    public float getConstruction_material() {
        return construction_material;
    }

    public void setConstruction_material(float construction_material) {
        this.construction_material = construction_material;
    }

    public float getLabour_charges() {
        return labour_charges;
    }

    public void setLabour_charges(float labour_charges) {
        this.labour_charges = labour_charges;
    }

    public float getEelctricity() {
        return electricity;
    }

    public void setElectricity(float elctricity) {
        this.electricity = electricity;
    }

    public float getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(float maintenance) {
        this.maintenance = maintenance;
    }

    public float getOthers() {
        return others;
    }

    public void setOthers(float others) {
        this.others = others;
    }
}
