package com.nitish.nfcqrapp.data;

/**
 * Created by Nitish on 5/4/2018.
 */

public class ProductRequest {
    private String nfcId;

    @Override
    public String toString() {
        return "ProductRequest{" +
                "nfcId='" + nfcId + '\'' +
                '}';
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }
}
