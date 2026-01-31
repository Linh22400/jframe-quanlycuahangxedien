package com.shop.model;

import java.sql.Timestamp;

public class Customer {
    private int id;
    private String phone;
    private String name;
    private String address;
    private Timestamp createdAt;

    public Customer() {
    }

    public Customer(int id, String phone, String name, String address, Timestamp createdAt) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name + " (" + phone + ")"; // Hiển thị trên ComboBox
    }
}
