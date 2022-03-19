package com.dika.freshgroceries.model;

public class CategoryModel {
    private String txtName;

    public CategoryModel(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }
}
