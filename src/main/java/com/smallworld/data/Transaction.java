package com.smallworld.data;

public class Transaction {
    private double amount;
    private String senderFullName;
    private String beneficiaryFullName;
    private Integer issueId;
    private boolean issueSolved;
    private String issueMessage;
    // other fields and methods

    public double getAmount() {
        return amount;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public boolean isIssueSolved() {
        return issueSolved;
    }

    public String getIssueMessage() {
        return issueMessage;
    }

}


