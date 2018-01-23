package com.mawaqaa.eatandrun.data;

/**
 * Created by HP on 11/27/2017.
 */

public class PaymentHistoryData {

    private String orderid;
    private String transactionid;
    private String orderdate;
    private String amount;
    private String status;

    public PaymentHistoryData(String orderid, String transactionid, String orderdate, String amount, String status) {
        this.orderid = orderid;
        this.transactionid = transactionid;
        this.orderdate = orderdate;
        this.amount = amount;
        this.status = status;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
