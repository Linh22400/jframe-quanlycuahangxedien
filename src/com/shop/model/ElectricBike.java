package com.shop.model;

/**
 * Lớp xe đạp điện, kế thừa từ Vehicle.
 * Có thêm thuộc tính thông tin Pin hoặc Công suất (batteryInfo).
 */
public class ElectricBike extends Vehicle {
    private String batteryInfo; // Thông tin Pin/Công suất (VD: 48V-12Ah, 1200W)

    public ElectricBike() {
        super();
    }

    public ElectricBike(int id, String code, String name, String brand, double price, int quantity, String description,
            String batteryInfo) {
        super(id, code, name, brand, price, quantity, description);
        this.batteryInfo = batteryInfo;
    }

    @Override
    public String getType() {
        return "ELECTRIC_BIKE";
    }

    public String getBatteryInfo() {
        return batteryInfo;
    }

    public void setBatteryInfo(String batteryInfo) {
        this.batteryInfo = batteryInfo;
    }
}
