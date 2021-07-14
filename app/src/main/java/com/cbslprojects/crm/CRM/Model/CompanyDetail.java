package com.cbslprojects.crm.CRM.Model;

import java.util.ArrayList;

public class CompanyDetail {
    private String ErrorCode;
    private String Message;
    private ArrayList<CompanyDetail> MappedInformation;

    private String CompanyId;
    private String CompanyName;

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<CompanyDetail> getMappedInformation() {
        return MappedInformation;
    }

    public void setMappedInformation(ArrayList<CompanyDetail> mappedInformation) {
        MappedInformation = mappedInformation;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
