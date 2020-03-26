package com.workspace.carnote.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Single tank data model
 */

public class TankUpRecord implements Serializable {

    private Date tankUpDate;
    private Integer mileage;
    private Integer tankedUpGasLiters;
    private Integer costInPLN;

    private TankUpRecord(Builder builder) {
        this.tankUpDate = builder.tankUpDate;
        this.mileage = builder.mileage;
        this.tankedUpGasLiters = builder.tankedUpGasLiters;
        this.costInPLN = builder.costInPLN;
    }

    public Date getTankUpDate() {
        return tankUpDate;
    }
    public void setTankUpDate(Date tankUpDate) {
        this.tankUpDate = tankUpDate;
    }

    public Integer getMileage() {
        return mileage;
    }
    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getTankedUpGasLiters() {
        return tankedUpGasLiters;
    }
    public void setTankedUpGasLiters(Integer tankedUpGasLiters) {
        this.tankedUpGasLiters = tankedUpGasLiters;
    }

    public Integer getCostInPLN() {
        return costInPLN;
    }
    public void setCostInPLN(Integer costInPLN) {
        this.costInPLN = costInPLN;
    }

    public static class Builder{
        private Date tankUpDate;
        private Integer mileage;
        private Integer tankedUpGasLiters;
        private Integer costInPLN;

        public Builder() {

        }

        public Builder tankUpDate(Date tankUpDate){
            this.tankUpDate = tankUpDate;
            return this;
        }

        public Builder mileage (Integer mileage){
            this.mileage = mileage;
            return this;
        }

        public Builder tankedUpGasLiters(Integer tankedUpGasLiters){
            this.tankedUpGasLiters = tankedUpGasLiters;
            return this;
        }

        public Builder costInPLN(Integer costInPLN){
            this.costInPLN = costInPLN;
            return this;
        }

        public TankUpRecord build(){
            return new TankUpRecord(this);
        }

    }

}
