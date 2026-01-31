-- FILE 1: Cấu trúc cơ bản (Người dùng & Xe)
-- Chạy file này KHI MỚI BẮT ĐẦU DỰ ÁN

CREATE DATABASE IF NOT EXISTS shop_management;
USE shop_management;

-- 1. Bảng User
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'STAFF'
);

INSERT INTO users (username, password, full_name, role) VALUES ('admin', '123456', 'Quản Trị Viên', 'ADMIN') ON DUPLICATE KEY UPDATE username=username;

-- 2. Bảng Vehicle (Xe Điện)
CREATE TABLE IF NOT EXISTS vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    type VARCHAR(30) NOT NULL, 
    extra_1 VARCHAR(50), 
    description TEXT
);

-- Dữ liệu mẫu ban đầu
INSERT INTO vehicles (code, name, brand, price, quantity, type, extra_1, description) VALUES 
('EM01', 'VinFast Klara S', 'VinFast', 39900000, 10, 'ELECTRIC_MOTORCYCLE', '1200W', 'Xe máy điện cao cấp'),
('EM02', 'Yadea G5', 'Yadea', 29990000, 5, 'ELECTRIC_MOTORCYCLE', '1200W', 'Thiết kế châu Âu'),
('EB01', 'Pega Cap A+', 'Pega', 12000000, 15, 'ELECTRIC_BICYCLE', '48V-12Ah', 'Xe đạp điện học sinh'),
('EB02', 'Asama EBK', 'Asama', 10500000, 8, 'ELECTRIC_BICYCLE', '48V-12Ah', 'Bền bỉ, cổ điển')
ON DUPLICATE KEY UPDATE code=code;
