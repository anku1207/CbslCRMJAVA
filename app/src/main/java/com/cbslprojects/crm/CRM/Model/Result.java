package com.cbslprojects.crm.CRM.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("ErrorCode")
    @Expose
    private String errorCode;
    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;

     @SerializedName("ReferenceNo")
    @Expose
    private String ReferenceNo;

    @SerializedName("QuantityDetails")
    @Expose
    private List<QuantityDetail> quantityDetails = null;

    @SerializedName("ItemDetails")
    @Expose
    private List<ItemDetail> itemDetails=null;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getReferenceNo() {
        return ReferenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        ReferenceNo = referenceNo;
    }

    public List<QuantityDetail> getQuantityDetails() {
        return quantityDetails;
    }

    public void setQuantityDetails(List<QuantityDetail> quantityDetails) {
        this.quantityDetails = quantityDetails;
    }

    public List<ItemDetail> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<ItemDetail> itemDetails) {
        this.itemDetails = itemDetails;
    }
}