package com.dika.freshgroceries.model;

public class FruitModel {
    private int imgFruit;
    private String nameFruit;
    private String priceFruit;
    private int ratingFruit;

    public FruitModel(int imgFruit,String nameFruit,String priceFruit, int ratingFruit) {
        this.imgFruit = imgFruit;
        this.nameFruit = nameFruit;
        this.priceFruit = priceFruit;
        this.ratingFruit= ratingFruit;
    }

    public int getImgFruit() {
        return imgFruit;
    }

    public void setImgFruit(int imgFruit) {
        this.imgFruit = imgFruit;
    }

    public String getNameFruit() {
        return nameFruit;
    }

    public void setNameFruit(String nameFruit) {
        this.nameFruit = nameFruit;
    }

    public String getPriceFruit() {
        return priceFruit;
    }

    public void setPriceFruit(String priceFruit) {
        this.priceFruit = priceFruit;
    }

    public int getRatingFruit() {
        return ratingFruit;
    }

    public void setRatingFruit(int ratingFruit) {
        this.ratingFruit = ratingFruit;
    }
}
