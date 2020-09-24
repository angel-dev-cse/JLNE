package com.example.jlne.model;

public class Machine {
    private int id, transaction_id, challan, amount;
    private String name, brand, model, serial, owner, running_status, sentTo, sentFrom, date, transactionType, remarks;
    private Boolean expanded, optioned;

    public Machine(int id, String name, String brand, String model, String serial, String owner, String running_status, int transaction_id, String sentFrom, String sentTo, String date, String transactionType, int challan, int amount, String remarks) {
        this.id = id;
        this.transaction_id = transaction_id;
        this.challan = challan;
        this.amount = amount;
        this.running_status = running_status;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.serial = serial;
        this.owner = owner;
        this.sentTo = sentTo;
        this.sentFrom = sentFrom;
        this.owner = owner;
        this.date = date;
        this.transactionType = transactionType;
        this.remarks = remarks;
        // default not expanded, not showing options
        this.expanded = false;
        this.optioned = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getChallan() {
        return challan;
    }

    public void setChallan(int challanNo) {
        this.challan = challanNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRunning_status() {
        return running_status;
    }

    public void setRunning_status(String running_status) {
        this.running_status = running_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionTypeType() {
        return transactionType;
    }

    public void setTransactionTypeType(String type) {
        this.transactionType = type;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Boolean isOptioned() {
        return optioned;
    }

    public void setOptioned(Boolean optioned) {
        this.optioned = optioned;
    }
}
