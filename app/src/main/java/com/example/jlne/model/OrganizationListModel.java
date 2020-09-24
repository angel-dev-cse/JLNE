package com.example.jlne.model;

public class OrganizationListModel {
    private String name;
    private Boolean showOptions;

    public OrganizationListModel(String name, Boolean showOptions) {
        this.name = name;
        this.showOptions = showOptions;
    }

    public String getName() {
        return name;
    }

    public Boolean isShowOptions() {
        return showOptions;
    }

    public void setShowOptions(Boolean showOptions) {
        this.showOptions = showOptions;
    }

}
