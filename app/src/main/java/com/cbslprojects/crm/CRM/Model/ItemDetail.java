package com.cbslprojects.crm.CRM.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ItemDetail {

    private String ErrorCode;
    private String ErrorMessage;
    private ArrayList<ItemDetail> ItemDetails;

    @SerializedName("ConsumableItemId")
    private Integer consumableItemId;

    @SerializedName("ConsumableItemName")
    private String consumableItemName;

    @SerializedName("CompanyId")
    private Integer companyId;

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

    public ArrayList<ItemDetail> getItemDetails() {
        return ItemDetails;
    }

    public void setItemDetails(ArrayList<ItemDetail> itemDetails) {
        ItemDetails = itemDetails;
    }

    public Integer getConsumableItemId() {
        return consumableItemId;
    }

    public void setConsumableItemId(Integer consumableItemId) {
        this.consumableItemId = consumableItemId;
    }

    public String getConsumableItemName() {
        return consumableItemName;
    }

    public void setConsumableItemName(String consumableItemName) {
        this.consumableItemName = consumableItemName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}