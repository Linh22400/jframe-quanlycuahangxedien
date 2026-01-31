package com.shop.model;

/**
 * Lớp trừu tượng đại diện cho một phương tiện chung.
 * Chứa các thuộc tính cơ bản nhưng thêm toString() để hiển thị ComboBox
 */
public abstract class Vehicle {
    private int id;
    private String code;
    private String name;
    private String brand;
    private double price;
    private int quantity;
    private String description;

    public Vehicle() {
    }

    public Vehicle(int id, String code, String name, String brand, double price, int quantity, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }

    public abstract String getType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (Tồn: " + quantity + ")";
    }
}
