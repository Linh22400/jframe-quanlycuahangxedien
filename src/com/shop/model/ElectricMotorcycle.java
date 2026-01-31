package com.shop.model;

/**
 * Lớp Xe Máy Điện, kế thừa từ Vehicle.
 * Thay thế cho Xe máy xăng.
 * Có thuộc tính riêng là Công Suất (power) thay vì dung tích xi lanh.
 */
public class ElectricMotorcycle extends Vehicle {
    private String power; // Công suất động cơ (VD: 1000W, 1500W)

    public ElectricMotorcycle() {
        super();
    }

    public ElectricMotorcycle(int id, String code, String name, String brand, double price, int quantity,
            String description, String power) {
        super(id, code, name, brand, price, quantity, description);
        this.power = power;
    }

    @Override
    public String getType() {
        return "ELECTRIC_MOTORCYCLE";
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
