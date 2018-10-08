package com.nitish.nfcqrapp.data;

import java.util.ArrayList;

/**
 * Created by Nitish on 5/4/2018.
 */

public class ProductDetail {
    private String result;
    private String productName;
    private String manufacturerDate;
    private String expirDate;
    private String manufacturerBy;
    private String mrp;
    private String imageUrl;
    private String unit;
    private ArrayList<String> keyFeatures;
    private ArrayList<String> ingredients;
    private String description;
    private String disclaimer;

    @Override
    public String toString() {
        return "ProductDetail{" +
                "result='" + result + '\'' +
                ", productName='" + productName + '\'' +
                ", manufacturerDate='" + manufacturerDate + '\'' +
                ", expirDate='" + expirDate + '\'' +
                ", manufacturerBy='" + manufacturerBy + '\'' +
                ", mrp='" + mrp + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", unit='" + unit + '\'' +
                ", keyFeatures=" + keyFeatures +
                ", ingredients=" + ingredients +
                ", description='" + description + '\'' +
                ", disclaimer='" + disclaimer + '\'' +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturerDate() {
        return manufacturerDate;
    }

    public void setManufacturerDate(String manufacturerDate) {
        this.manufacturerDate = manufacturerDate;
    }

    public String getExpirDate() {
        return expirDate;
    }

    public void setExpirDate(String expirDate) {
        this.expirDate = expirDate;
    }

    public String getManufacturerBy() {
        return manufacturerBy;
    }

    public void setManufacturerBy(String manufacturerBy) {
        this.manufacturerBy = manufacturerBy;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<String> getKeyFeatures() {
        return keyFeatures;
    }

    public void setKeyFeatures(ArrayList<String> keyFeatures) {
        this.keyFeatures = keyFeatures;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
