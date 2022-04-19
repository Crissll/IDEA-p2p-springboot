package com.bjpowernode.p2p.model.loan;

import java.io.Serializable;
import java.util.Date;

public class IncomeRecord implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer loanId;

    private Integer bidId;

    private Double bidMoney;

    private Date incomeDate;

    private Double incomeMoney;

    private Integer incomeStatus;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Double getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }

    public Date getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
    }

    public Double getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(Double incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    public Integer getIncomeStatus() {
        return incomeStatus;
    }

    public void setIncomeStatus(Integer incomeStatus) {
        this.incomeStatus = incomeStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uid=").append(uid);
        sb.append(", loanId=").append(loanId);
        sb.append(", bidId=").append(bidId);
        sb.append(", bidMoney=").append(bidMoney);
        sb.append(", incomeDate=").append(incomeDate);
        sb.append(", incomeMoney=").append(incomeMoney);
        sb.append(", incomeStatus=").append(incomeStatus);
        sb.append("]");
        return sb.toString();
    }
}