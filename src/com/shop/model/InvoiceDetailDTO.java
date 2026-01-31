package com.shop.model;

/**
 * Data Transfer Object cho hiển thị chi tiết sản phẩm trong 1 hóa đơn.
 * Chứa dữ liệu JOIN từ invoice_details + vehicles.
 */
public class InvoiceDetailDTO {
    private String vehicleCode;
    private String vehicleName;
    private int quantity;
    private double price;
    private double subtotal; // price * quantity

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
