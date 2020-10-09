package com.android.skyheight.model;

import java.io.Serializable;

public class PlotExpenseModel implements Serializable {
    public String plot;
    public String plot_expense_remarks;
    public float construction_material;
    public float labour_charges;
    public float elctricity;
    public float maintenance;
    public float others;


    public PlotExpenseModel(String plot, String plot_expense_remarks, float construction_material,
                            float labour_charges, float elctricity, float maintenance, float others) {
        this.plot = plot;
        this.plot_expense_remarks = plot_expense_remarks;
        this.construction_material = construction_material;
        this.labour_charges = labour_charges;
        this.elctricity = elctricity;
        this.maintenance = maintenance;
        this.others = others;

    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPlot_expense_remarks() {
        return plot_expense_remarks;
    }

    public void setPlot_expense_remarks(String plot_expense_remarks) {
        this.plot_expense_remarks = plot_expense_remarks;
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

    public float getElctricity() {
        return elctricity;
    }

    public void setElctricity(float elctricity) {
        this.elctricity = elctricity;
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
