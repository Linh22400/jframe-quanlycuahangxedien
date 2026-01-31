package com.shop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shop.model.InvoiceDTO;
import com.shop.model.InvoiceDetailDTO;

/**
 * DAO để xử lý lịch sử hóa đơn và báo cáo doanh thu.
 */
public class InvoiceHistoryDAO {

    /**
     * Lấy tất cả hóa đơn với thông tin JOIN (khách hàng, nhân viên).
     */
    public List<InvoiceDTO> getAllInvoices() {
        List<InvoiceDTO> list = new ArrayList<>();
        String sql = "SELECT i.id, i.created_at, c.name AS customer_name, c.phone AS customer_phone, " +
                "u.full_name AS user_name, i.total_amount " +
                "FROM invoices i " +
                "JOIN customers c ON i.customer_id = c.id " +
                "JOIN users u ON i.user_id = u.id " +
                "ORDER BY i.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                InvoiceDTO dto = new InvoiceDTO();
                dto.setInvoiceId(rs.getInt("id"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerPhone(rs.getString("customer_phone"));
                dto.setUserName(rs.getString("user_name"));
                dto.setTotalAmount(rs.getDouble("total_amount"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy chi tiết các sản phẩm trong 1 hóa đơn cụ thể.
     */
    public List<InvoiceDetailDTO> getInvoiceDetails(int invoiceId) {
        List<InvoiceDetailDTO> list = new ArrayList<>();
        String sql = "SELECT v.code, v.name, d.quantity, d.price, (d.quantity * d.price) AS subtotal " +
                "FROM invoice_details d " +
                "JOIN vehicles v ON d.vehicle_id = v.id " +
                "WHERE d.invoice_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, invoiceId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                InvoiceDetailDTO dto = new InvoiceDetailDTO();
                dto.setVehicleCode(rs.getString("code"));
                dto.setVehicleName(rs.getString("name"));
                dto.setQuantity(rs.getInt("quantity"));
                dto.setPrice(rs.getDouble("price"));
                dto.setSubtotal(rs.getDouble("subtotal"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm hóa đơn theo tên hoặc SĐT khách hàng.
     */
    public List<InvoiceDTO> searchInvoicesByCustomer(String keyword) {
        List<InvoiceDTO> list = new ArrayList<>();
        String sql = "SELECT i.id, i.created_at, c.name AS customer_name, c.phone AS customer_phone, " +
                "u.full_name AS user_name, i.total_amount " +
                "FROM invoices i " +
                "JOIN customers c ON i.customer_id = c.id " +
                "JOIN users u ON i.user_id = u.id " +
                "WHERE c.name LIKE ? OR c.phone LIKE ? " +
                "ORDER BY i.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                InvoiceDTO dto = new InvoiceDTO();
                dto.setInvoiceId(rs.getInt("id"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerPhone(rs.getString("customer_phone"));
                dto.setUserName(rs.getString("user_name"));
                dto.setTotalAmount(rs.getDouble("total_amount"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy doanh thu hôm nay.
     */
    public double getRevenueToday() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM invoices WHERE DATE(created_at) = CURDATE()";
        return getRevenue(sql);
    }

    /**
     * Lấy doanh thu tháng này.
     */
    public double getRevenueThisMonth() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM invoices " +
                "WHERE MONTH(created_at) = MONTH(NOW()) AND YEAR(created_at) = YEAR(NOW())";
        return getRevenue(sql);
    }

    /**
     * Đếm tổng số đơn hàng.
     */
    public int getTotalInvoicesCount() {
        String sql = "SELECT COUNT(*) FROM invoices";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Helper method để lấy doanh thu từ query.
     */
    private double getRevenue(String sql) {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
