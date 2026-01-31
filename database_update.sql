-- FILE 2: Cập nhật Mở rộng (Khách hàng & Hóa đơn)
-- Chạy file này SAU KHI đã chạy database.sql để thêm tính năng mới

USE shop_management;

-- 3. Bảng Customer (Khách hàng) - MỚI
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Bảng Invoice (Hóa đơn bán hàng) - MỚI
CREATE TABLE IF NOT EXISTS invoices (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    user_id INT, -- Nhân viên tạo hóa đơn
    total_amount DOUBLE NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 5. Bảng Chi tiết Hóa đơn (Invoice Details) - MỚI
CREATE TABLE IF NOT EXISTS invoice_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL, -- Giá tại thời điểm bán
    
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- Dữ liệu mẫu Khách hàng
INSERT INTO customers (phone, name, address) VALUES 
('0909123456', 'Nguyen Van A', 'Ha Noi'),
('0988777666', 'Tran Thi B', 'Ho Chi Minh')
ON DUPLICATE KEY UPDATE name=name;
