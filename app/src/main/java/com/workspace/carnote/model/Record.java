package com.workspace.carnote.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Single tank data model
 */

public class Record implements Serializable {

    private RecordType recordType;
    private Date date;
    private Integer mileage;
    private Integer tankedUpGasLiters;
    private Integer costInPLN;
    private String description;

    private Record(Builder builder) {
        this.recordType = builder.recordType;
        this.date = builder.date;
        this.mileage = builder.mileage;
        this.tankedUpGasLiters = builder.tankedUpGasLiters;
        this.costInPLN = builder.costInPLN;
        this.description = builder.description;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public Date getDate() {
        return date;
    }

    public Integer getMileage() {
        return mileage;
    }

    public Integer getTankedUpGasLiters() {
        return tankedUpGasLiters;
    }

    public Integer getCostInPLN() {
        return costInPLN;
    }

    public String getDescription() {
        return description;
    }


    public static class Builder{

        private RecordType recordType;
        private Date date;
        private Integer mileage;
        private Integer tankedUpGasLiters;
        private Integer costInPLN;
        private String description;

        public Builder(RecordType recordType, Date date) {
            this.recordType = recordType;
            this.date = date;
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

        public Builder description (String description){
            this.description = description;
            return this;
        }

        public Record build(){
            return new Record(this);
        }

    }

    // unused Abstract methods

}
