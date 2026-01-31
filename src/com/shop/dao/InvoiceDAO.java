package com.shop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.shop.model.Invoice;
import com.shop.model.InvoiceDetail;

public class InvoiceDAO {

    /**
     * Tạo hóa đơn mới (Transaction):
     * 1. Insert bảng Invoice
     * 2. Insert bảng InvoiceDetail
     * 3. Trừ kho bảng Vehicles
     */
    public boolean createInvoice(Invoice invoice, List<InvoiceDetail> details) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Thêm Invoice
            String sqlInv = "INSERT INTO invoices (customer_id, user_id, total_amount) VALUES (?, ?, ?)";
            PreparedStatement pstInv = conn.prepareStatement(sqlInv, Statement.RETURN_GENERATED_KEYS);
            pstInv.setInt(1, invoice.getCustomerId());
            pstInv.setInt(2, invoice.getUserId()); // Lấy từ object truyền vào
            pstInv.setDouble(3, invoice.getTotalAmount());
            pstInv.executeUpdate();

            // Lấy ID hóa đơn vừa tạo
            ResultSet rsKey = pstInv.getGeneratedKeys();
            int invoiceId = 0;
            if (rsKey.next()) {
                invoiceId = rsKey.getInt(1);
            }

            // 2. Thêm Details và Trừ kho
            String sqlDetail = "INSERT INTO invoice_details (invoice_id, vehicle_id, quantity, price) VALUES (?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE vehicles SET quantity = quantity - ? WHERE id = ?";

            PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
            PreparedStatement pstStock = conn.prepareStatement(sqlUpdateStock);

            for (InvoiceDetail item : details) {
                // Thêm chi tiết
                pstDetail.setInt(1, invoiceId);
                pstDetail.setInt(2, item.getVehicleId());
                pstDetail.setInt(3, item.getQuantity());
                pstDetail.setDouble(4, item.getPrice());
                pstDetail.addBatch();

                // Trừ kho
                pstStock.setInt(1, item.getQuantity());
                pstStock.setInt(2, item.getVehicleId());
                pstStock.addBatch();
            }

            pstDetail.executeBatch();
            pstStock.executeBatch();

            conn.commit(); // Xác nhận Transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback(); // Rollback nếu lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
