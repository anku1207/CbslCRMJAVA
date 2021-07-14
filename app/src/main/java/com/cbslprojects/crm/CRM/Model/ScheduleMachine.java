package com.cbslprojects.crm.CRM.Model;
import java.util.ArrayList;

public class ScheduleMachine {

    private String ErrorMessage;
    private String ErrorCode;
    private ArrayList<ScheduleMachine> ScheduleMachineList;

    private String BankName;
    private String BranchName;
    private String MachineId;
    private String DispatchDate;
    private String ProjectName;
    private String AssignTo;
    private String SoleId;
    private String MachineNo;
    private String ScheduleDate;
    private String CityName;
    private String StateName;
    private String Address;
    private String BankID;

    public String getBankID() {
        return BankID;
    }

    public void setBankID(String bankID) {
        BankID = bankID;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public ArrayList<ScheduleMachine> getScheduleMachineList() {
        return ScheduleMachineList;
    }

    public void setScheduleMachineList(ArrayList<ScheduleMachine> scheduleMachineList) {
        ScheduleMachineList = scheduleMachineList;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getMachineId() {
        return MachineId;
    }

    public void setMachineId(String machineId) {
        MachineId = machineId;
    }

    public String getDispatchDate() {
        return DispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        DispatchDate = dispatchDate;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getAssignTo() {
        return AssignTo;
    }

    public void setAssignTo(String assignTo) {
        AssignTo = assignTo;
    }

    public String getSoleId() {
        return SoleId;
    }

    public void setSoleId(String soleId) {
        SoleId = soleId;
    }

    public String getMachineNo() {
        return MachineNo;
    }

    public void setMachineNo(String machineNo) {
        MachineNo = machineNo;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
