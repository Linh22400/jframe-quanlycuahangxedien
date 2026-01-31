package com.shop.model;

import java.sql.Timestamp;

/**
 * Data Transfer Object cho hiển thị hóa đơn trong bảng lịch sử.
 * Chứa dữ liệu JOIN từ invoices + customers + users.
 */
public class InvoiceDTO {
    private int invoiceId;
    private Timestamp createdAt;
    private String customerName;
    private String customerPhone;
    private String userName; // Thu ngân
    private double totalAmount;

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
