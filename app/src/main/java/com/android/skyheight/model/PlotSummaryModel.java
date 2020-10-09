package com.android.skyheight.model;

import java.io.Serializable;

public class PlotSummaryModel implements Serializable {
    private String id;
    private float labour_charges;
    private float construction_material;
    private float electricity;
    private float maintenance;
    private float others;
    private String plot_expense_remarks;
    private float total_plot_expense;
    private String created_at;
    private String updated_at;
    private plot plot;

    public plot getPlot() {
        return plot;
    }

    public void setPlot(plot plot) {
        this.plot = plot;
    }

    public String getPlot_expense_remarks() {
        return plot_expense_remarks;
    }

    public void setPlot_expense_remarks(String plot_expense_remarks) {
        this.plot_expense_remarks = plot_expense_remarks;
    }

    public float getTotal_plot_expense() {
        return total_plot_expense;
    }

    public void setTotal_plot_expense(float total_plot_expense) {
        this.total_plot_expense = total_plot_expense;
    }


    public PlotSummaryModel(String id, float labour_charges, float construction_material, float electricity, float maintenance,
                            float others, String plot_expense_remarks, String created_at,
                            String updated_at,float total_plot_expense) {
        this.id = id;
        this.labour_charges = labour_charges;
        this.construction_material = construction_material;
        this.electricity = electricity;
        this.maintenance = maintenance;
        this.others = others;
        this.total_plot_expense=total_plot_expense;
        this.plot_expense_remarks = plot_expense_remarks;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLabour_charges() {
        return labour_charges;
    }

    public void setLabour_charges(float labour_charges) {
        this.labour_charges = labour_charges;
    }

    public float getConstruction_material() {
        return construction_material;
    }

    public void setConstruction_material(float construction_material) {
        this.construction_material = construction_material;
    }

    public float getElectricity() {
        return electricity;
    }

    public void setElectricity(float electricity) {
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



    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


}
