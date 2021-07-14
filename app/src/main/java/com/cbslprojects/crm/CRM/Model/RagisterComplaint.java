package com.cbslprojects.crm.CRM.Model;

import java.util.ArrayList;

public class RagisterComplaint {

    private String ErrorCode;
    private String ErrorMessage;
    private ArrayList<BranchDetails> BranchDetails;

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

    public ArrayList<BranchDetails> getBranchDetails() {
        return BranchDetails;
    }

    public void setBranchDetails(ArrayList<BranchDetails> branchDetails) {
        BranchDetails = branchDetails;
    }
}
