package com.cbslprojects.crm.CRM.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuantityDetail {
    private String ErrorCode;
    private String ErrorMessage;
    private ArrayList<QuantityDetail> QuantityDetails;

    @SerializedName("OrderQuantityId")
    private Integer orderQuantityId;

    @SerializedName("Quantity")
    private Integer quantity;

    @SerializedName("CompanyId")
    private Integer CompanyId;

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public ArrayList<QuantityDetail> getQuantityDetails() {
        return QuantityDetails;
    }

    public void setQuantityDetails(ArrayList<QuantityDetail> quantityDetails) {
        QuantityDetails = quantityDetails;
    }

    public Integer getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(Integer companyId) {
        CompanyId = companyId;
    }

    public Integer getOrderQuantityId() {
        return orderQuantityId;
    }

    public void setOrderQuantityId(Integer orderQuantityId) {
        this.orderQuantityId = orderQuantityId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}