package com.workspace.carnote.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class AutoData implements Serializable {

    /**
     * Model: Golf
     * Brand: VolksWagen
     */

    private String model;
    private String brand;
    private String color;

    private List<TankUpRecord> tankUpRecord;



    public AutoData(String model, String brand, String color) {
        this.model = model;
        this.brand = brand;
        this.color = color;
        tankUpRecord = new LinkedList<>();
    }

    public List<TankUpRecord> getTankUpRecord() {
        return tankUpRecord;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
