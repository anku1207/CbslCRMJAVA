package com.cbslprojects.crm.CRM.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReloveComplaint {


    private String ErrorCode;
    private String ErrorMessage;
    private ArrayList<ReloveComplaint> List;

    @SerializedName("BankName")
    private
    String bankName;
    @SerializedName("BranchName")
    private
    String branchName;
    @SerializedName("ComplaintType")
    private
    String complaintType;
    @SerializedName("ComplaintNumber")
    private
    String complaintNumber;
    @SerializedName("ComplaintDate")
    private
    String complaintDate;
    @SerializedName("ProjectName")
    private
    String projectname;
    @SerializedName("MachineId")
    private
    String machindId;
    @SerializedName("SoleId")
    private
    String soleid;


    public ReloveComplaint(String bankName, String branchName, String complaintType, String complaintNumber, String complaintDate, String projectname, String machindId, String soleid) {
        this.bankName = bankName;
        this.branchName = branchName;
        this.complaintType = complaintType;
        this.complaintNumber = complaintNumber;
        this.complaintDate = complaintDate;
        this.projectname = projectname;
        this.machindId = machindId;
        this.soleid = soleid;
    }


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

    public ArrayList<ReloveComplaint> getList() {
        return List;
    }

    public void setList(ArrayList<ReloveComplaint> list) {
        List = list;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getMachindId() {
        return machindId;
    }

    public void setMachindId(String machindId) {
        this.machindId = machindId;
    }

    public String getSoleid() {
        return soleid;
    }

    public void setSoleid(String soleid) {
        this.soleid = soleid;
    }
}
