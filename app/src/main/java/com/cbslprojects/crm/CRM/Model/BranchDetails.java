package com.cbslprojects.crm.CRM.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BranchDetails implements Serializable {

    @SerializedName("MachineNo")
    private String MachineNo;

    @SerializedName("BankName")
    private String bankName;

    @SerializedName("SoleId")
    private String soleId;

    @SerializedName("BranchName")
    private String branchName;

    @SerializedName("PostalCode")
    private Integer postalCode;

    @SerializedName("LocationName")
    private String locationName;

    @SerializedName("ContactNo")
    private String contactNo;

    @SerializedName("EmailId")
    private String emailId;

    @SerializedName("StateName")
    private String stateName;

    @SerializedName("CityName")
    private String cityName;

    @SerializedName("BankId")
    private Integer bankId;

    @SerializedName("ProjectId")
    private Integer projectId;

    @SerializedName("ProjectName")
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BranchDetails(String MachineNo, String bankName, String soleId, String branchName) {
        this.MachineNo = MachineNo;
        this.bankName = bankName;
        this.soleId = soleId;
        this.branchName = branchName;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMachineNo() {
        return MachineNo;
    }

    public String getBankName() {
        return bankName;
    }

    public String getSoleId() {
        return soleId;
    }

    public String getBranchName() {
        return branchName;
    }


    public void setMachineNo(String machineNo) {
        this.MachineNo = machineNo;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setSoleId(String soleId) {
        this.soleId = soleId;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }


}



