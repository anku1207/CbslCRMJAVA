package com.cbslprojects.crm.RMMS.model;

public class Transaction {
    private String accountNo;
    private String accountHolderName;

    public Transaction(String accountNo, String accountHolderName) {
        this.accountNo = accountNo;
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }
}
