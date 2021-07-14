package com.cbslprojects.crm.CRM.Model;

import java.util.ArrayList;

public class MachineInformation {

    private String ErrorCode;
    private String ErrorMessage;
    private int MachineId;
    private String MachineNo;
    private  int BankId;
    private String BankName;
    private int StateId;
    private String StateName;
    private int CityId;
    private String CityName;
    private int BranchId;
    private String BranchName;
    private String SoleId;
    private int ProjectId;
    private String ProjectName;
    private ArrayList<HardwareInformation> HarwareInformation;

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

    public int getMachineId() {
        return MachineId;
    }

    public void setMachineId(int machineId) {
        MachineId = machineId;
    }

    public String getMachineNo() {
        return MachineNo;
    }

    public void setMachineNo(String machineNo) {
        MachineNo = machineNo;
    }

    public int getBankId() {
        return BankId;
    }

    public void setBankId(int bankId) {
        BankId = bankId;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public int getStateId() {
        return StateId;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getBranchId() {
        return BranchId;
    }

    public void setBranchId(int branchId) {
        BranchId = branchId;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getSoleId() {
        return SoleId;
    }

    public void setSoleId(String soleId) {
        SoleId = soleId;
    }

    public int getProjectId() {
        return ProjectId;
    }

    public void setProjectId(int projectId) {
        ProjectId = projectId;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public ArrayList<HardwareInformation> getHarwareInformation() {
        return HarwareInformation;
    }

    public void setHarwareInformation(ArrayList<HardwareInformation> harwareInformation) {
        HarwareInformation = harwareInformation;
    }
}
