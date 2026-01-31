package com.shop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp tiện ích để kết nối Database MySQL.
 * Chứa thông tin cấu hình và phương thức lấy Connection.
 */
public class DatabaseConnection {
    // Thông tin cấu hình Database
    // Lưu ý: Cần đảm bảo MySQL Server đang chạy và database 'shop_management' đã
    // được tạo
    private static final String URL = "jdbc:mysql://localhost:3306/shop_management";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Mật khẩu mặc định của XAMPP thường là chuỗi rỗng

    /**
     * Phương thức lấy kết nối đến Database.
     * 
     * @return Connection đối tượng kết nối
     * @throws SQLException nếu kết nối thất bại
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Nạp Driver MySQL (cần có thư viện mysql-connector-java trong classpath)
            // Với các phiên bản JDBC mới, bước này có thể tự động, nhưng khai báo tường
            // minh giúp dễ debug
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Không tìm thấy Driver MySQL. Hãy thêm thư viện mysql-connector-java vào project.");
        }
    }

    /**
     * Test kết nối thử
     */
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("Kết nối Database thành công!");
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
