package com.cbslprojects.crm.RMMS.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Machine implements Serializable, Parcelable {

    private String Status;
    private String Message;
    private ArrayList<Machine> Data;

    private String branch_name;
    private String BranchCode;
    private String IFSC_Code;
    private String Location_name;
    private String KioskId;
    private String MachineNo;
    private String ConnectionStatus;
    private String SimNo;
    private String BankName;
    private String CityName;
    private String StateName;

    protected Machine(Parcel in) {
        Status = in.readString();
        Message = in.readString();
        Data = in.createTypedArrayList(Machine.CREATOR);
        branch_name = in.readString();
        BranchCode = in.readString();
        IFSC_Code = in.readString();
        Location_name = in.readString();
        KioskId = in.readString();
        MachineNo = in.readString();
        ConnectionStatus = in.readString();
        SimNo = in.readString();
        BankName = in.readString();
        CityName = in.readString();
        StateName = in.readString();
    }

    public static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel in) {
            return new Machine(in);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<Machine> getData() {
        return Data;
    }

    public void setData(ArrayList<Machine> data) {
        Data = data;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }

    public String getIFSC_Code() {
        return IFSC_Code;
    }

    public void setIFSC_Code(String IFSC_Code) {
        this.IFSC_Code = IFSC_Code;
    }

    public String getLocation_name() {
        return Location_name;
    }

    public void setLocation_name(String location_name) {
        Location_name = location_name;
    }

    public String getKioskId() {
        return KioskId;
    }

    public void setKioskId(String kioskId) {
        KioskId = kioskId;
    }

    public String getMachineNo() {
        return MachineNo;
    }

    public void setMachineNo(String machineNo) {
        MachineNo = machineNo;
    }

    public String getConnectionStatus() {
        return ConnectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        ConnectionStatus = connectionStatus;
    }

    public String getSimNo() {
        return SimNo;
    }

    public void setSimNo(String simNo) {
        SimNo = simNo;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Status);
        dest.writeString(Message);
        dest.writeTypedList(Data);
        dest.writeString(branch_name);
        dest.writeString(BranchCode);
        dest.writeString(IFSC_Code);
        dest.writeString(Location_name);
        dest.writeString(KioskId);
        dest.writeString(MachineNo);
        dest.writeString(ConnectionStatus);
        dest.writeString(SimNo);
        dest.writeString(BankName);
        dest.writeString(CityName);
        dest.writeString(StateName);
    }
}
