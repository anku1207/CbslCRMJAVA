package com.cbslprojects.crm.RMMS.model;


public class RagisterComplaint {

    private String Status;
    private String Message;
    private RagisterComplaint Data;

    private String ErrorCode;
    private String ErrorMessage;
    private String ReferenceNo;

    public String getReferenceNo() {
        return ReferenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        ReferenceNo = referenceNo;
    }

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

    public RagisterComplaint getData() {
        return Data;
    }

    public void setData(RagisterComplaint data) {
        Data = data;
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


}
