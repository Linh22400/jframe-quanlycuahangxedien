package com.shop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shop.model.ElectricBike;
import com.shop.model.ElectricMotorcycle;
import com.shop.model.Vehicle;

/**
 * Lớp xử lý dữ liệu cho Xe (Thêm, Xóa, Sửa, Lấy danh sách).
 * Cập nhật: Thêm hàm lấy thống kê.
 */
public class VehicleDAO {

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles"; // Lấy tất cả

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehicle v = mapRowToVehicle(rs);
                if (v != null) {
                    list.add(v);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Vehicle> searchVehicles(String keyword) {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE name LIKE ? OR code LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicles (code, name, brand, price, quantity, type, extra_1, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getCode());
            pstmt.setString(2, v.getName());
            pstmt.setString(3, v.getBrand());
            pstmt.setDouble(4, v.getPrice());
            pstmt.setInt(5, v.getQuantity());
            pstmt.setString(6, v.getType());

            if (v instanceof ElectricMotorcycle) {
                pstmt.setString(7, ((ElectricMotorcycle) v).getPower());
            } else if (v instanceof ElectricBike) {
                pstmt.setString(7, ((ElectricBike) v).getBatteryInfo());
            } else {
                pstmt.setString(7, "");
            }

            pstmt.setString(8, v.getDescription());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateVehicle(Vehicle v) {
        String sql = "UPDATE vehicles SET name=?, brand=?, price=?, quantity=?, extra_1=?, description=? WHERE code=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getName());
            pstmt.setString(2, v.getBrand());
            pstmt.setDouble(3, v.getPrice());
            pstmt.setInt(4, v.getQuantity());

            if (v instanceof ElectricMotorcycle) {
                pstmt.setString(5, ((ElectricMotorcycle) v).getPower());
            } else if (v instanceof ElectricBike) {
                pstmt.setString(5, ((ElectricBike) v).getBatteryInfo());
            } else {
                pstmt.setString(5, "");
            }

            pstmt.setString(6, v.getDescription());
            pstmt.setString(7, v.getCode());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteVehicle(String code) {
        String sql = "DELETE FROM vehicles WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tính tổng số lượng tồn kho.
     */
    public int getTotalQuantity() {
        String sql = "SELECT SUM(quantity) FROM vehicles";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Tính tổng giá trị tài sản (Giá * Số lượng).
     */
    public double getTotalValue() {
        String sql = "SELECT SUM(price * quantity) FROM vehicles";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Vehicle mapRowToVehicle(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Vehicle v = null;

        if ("ELECTRIC_MOTORCYCLE".equalsIgnoreCase(type)) {
            v = new ElectricMotorcycle();
            ((ElectricMotorcycle) v).setPower(rs.getString("extra_1"));
        } else if ("ELECTRIC_BICYCLE".equalsIgnoreCase(type)) {
            v = new ElectricBike();
            ((ElectricBike) v).setBatteryInfo(rs.getString("extra_1"));
        }

        if (v != null) {
            v.setId(rs.getInt("id"));
            v.setCode(rs.getString("code"));
            v.setName(rs.getString("name"));
            v.setBrand(rs.getString("brand"));
            v.setPrice(rs.getDouble("price"));
            v.setQuantity(rs.getInt("quantity"));
            v.setDescription(rs.getString("description"));
        }
        return v;
    }
}
