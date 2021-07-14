package com.cbslprojects.crm.CRM.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetFileType {

    private String Message;
    private String ErrorCode;
    private ArrayList<GetFileType> Filelist;

    @SerializedName("FileTypeId")
    private String fileTypeId;
    @SerializedName("Filename")
    private String filename;

    public GetFileType(String filename, String fileTypeId) {
        this.fileTypeId = fileTypeId;
        this.filename = filename;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public ArrayList<GetFileType> getFilelist() {
        return Filelist;
    }

    public void setFilelist(ArrayList<GetFileType> filelist) {
        Filelist = filelist;
    }

    public String getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
