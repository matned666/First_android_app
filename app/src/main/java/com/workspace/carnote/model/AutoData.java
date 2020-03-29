package com.workspace.carnote.model;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class AutoData implements Serializable {

    /**
     * Model: ex. Golf
     * Brand: ex. VolksWagen
     */

    private String model;
    private String brand;
    private String color;
    private String plates;

    private List<TankUpRecord> tankUpRecord;


    private AutoData(Builder builder) {
        this.model = builder.model;
        this.brand = builder.brand;
        this.color = builder.color;
        this.plates = builder.plates;
        tankUpRecord = new LinkedList<>();
    }

    public List<TankUpRecord> getTankUpRecord() {
        return tankUpRecord;
    }
    public void setTankUpRecord(List<TankUpRecord> tankUpRecord) {
        this.tankUpRecord = tankUpRecord;
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

    public String getPlates() {
        return plates;
    }
    public void setPlates(String plates) {
        this.plates = plates;
    }

    @NonNull
    @Override
    public String toString() {
        return brand+" "+model+" "+color+" "+plates;
    }

    public static class Builder {

        private String model;
        private String brand;
        private String color;
        private String plates;

        public Builder() {
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder plates(String plates) {
            this.plates = plates;
            return this;
        }

        public AutoData build (){
            return new AutoData(this);
        }
    }
}
