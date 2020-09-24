package com.example.jlne.model;

public class Transportation {
    private int transactionId, machineId, challan, amount;
    private String sentFrom, sentTo, date, type, remarks;

    public Transportation(int transactionId, int machineId, int challan, int amount, String sentFrom
            , String sentTo, String date, String type, String remarks) {
        this.transactionId = transactionId;
        this.machineId = machineId;
        this.challan = challan;
        this.amount = amount;
        this.sentFrom = sentFrom;
        this.sentTo = sentTo;
        this.date = date;
        this.type = type;
        this.remarks = remarks;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getChallan() {
        return challan;
    }

    public void setChallan(int challan) {
        this.challan = challan;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
