package com.example.gelgel_short_time_coding.models;

import java.io.Serializable;

public class MyCartModel implements Serializable {

    String productName;
    String productPrice;
    String currentDate;
    String currentTime;
    String totalQuantity;
    double totalPrice;

    String documentId;

    public MyCartModel() {
    }

    @Override
    public String toString(){
        return "\nProductName: "+productName+"\nProductPrice: "+productPrice+"\nCurrentDate: "+currentDate+"\nCurrentTime: "
                +currentTime+"\ntotalQuantity: "+totalQuantity+"\nTotalPrice: "+String.valueOf(totalPrice)+"\n";
    }
    public MyCartModel(String productName, String productPrice, String currentDate, String currentTime, String totalQuantity, double totalPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
