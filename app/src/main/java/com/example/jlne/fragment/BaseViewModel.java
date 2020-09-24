package com.example.jlne.fragment;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class BaseViewModel extends ViewModel {

    private ArrayList<String> categoryList = new ArrayList<>();

    public void setCategoryList(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }
}