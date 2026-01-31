# 🏍️ Hệ Thống Quản Lý Cửa Hàng Xe Điện

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white)

Ứng dụng desktop quản lý cửa hàng bán xe điện (Xe Máy Điện & Xe Đạp Điện) được xây dựng bằng **Java Swing** và **MySQL**.

---

## 📋 Mục Lục

- [Tính Năng](#-tính-năng)
- [Công Nghệ](#-công-nghệ)
- [Cài Đặt](#-cài-đặt)
- [Sử Dụng](#-sử-dụng)
- [Cấu Trúc Dự Án](#-cấu-trúc-dự-án)
- [Screenshots](#-screenshots)
- [Tác Giả](#-tác-giả)

---

## ✨ Tính Năng

### 🔐 1. Đăng Nhập & Quản Lý Phiên
- Xác thực người dùng với username/password
- Session management (theo dõi nhân viên đang đăng nhập)
- Giao diện đăng nhập hiện đại

### 🛵 2. Quản Lý Sản Phẩm (Xe Điện)
- **CRUD đầy đủ**: Thêm, Sửa, Xóa sản phẩm
- **Phân loại xe**:
  - Xe Máy Điện (Electric Motorcycle) - Thuộc tính: Công suất (W)
  - Xe Đạp Điện (Electric Bicycle) - Thuộc tính: Pin (Ah)
- **Tìm kiếm & Lọc**: Theo tên, mã xe, loại xe
- **Thống kê**: Tổng số lượng tồn kho, Tổng giá trị hàng

### 👥 3. Quản Lý Khách Hàng
- Thêm/Sửa/Xóa thông tin khách hàng
- Lưu trữ: Số điện thoại, Họ tên, Địa chỉ
- Tìm kiếm theo tên hoặc số điện thoại

### 💰 4. Bán Hàng & Hóa Đơn
- **Tạo Hóa Đơn**:
  - Chọn khách hàng từ danh sách
  - Chọn xe còn hàng
  - Thêm vào giỏ hàng với số lượng tùy chỉnh
  - Tính tổng tiền tự động
- **Thanh Toán**:
  - Tự động trừ tồn kho (Transaction)
  - Ghi nhận nhân viên bán hàng
  - Lưu vào Database
- **In Hóa Đơn**: Hiển thị chi tiết sau khi thanh toán

### 📊 5. Lịch Sử & Báo Cáo Doanh Thu
- **Xem Lịch Sử Hóa Đơn**:
  - Danh sách tất cả giao dịch đã bán
  - Thông tin: Mã HĐ, Ngày, Khách hàng, Thu ngân, Tổng tiền
  - Xem chi tiết sản phẩm trong từng hóa đơn
  - Tìm kiếm theo tên/SĐT khách hàng
- **Báo Cáo Doanh Thu**:
  - Doanh thu hôm nay
  - Doanh thu tháng này
  - Tổng số đơn hàng

### 🎨 6. Giao Diện Hiện Đại
- Flat Design với màu xanh chủ đạo
- Font: Segoe UI cho toàn bộ ứng dụng
- Tab-based navigation (4 tabs: Sản phẩm, Khách hàng, Bán hàng, Lịch sử)
- Responsive components với scroll support

---

## 🛠️ Công Nghệ

- **Language**: Java 8+
- **GUI Framework**: Java Swing
- **Database**: MySQL 8.0+
- **JDBC Driver**: MySQL Connector/J
- **Design Pattern**: MVC + DAO Pattern

---

## 📦 Cài Đặt

### Yêu Cầu Hệ Thống
- **Java JDK**: 8 trở lên
- **MySQL**: 8.0 trở lên
- **IDE**: Eclipse / IntelliJ IDEA / NetBeans (tùy chọn)

### Bước 1: Clone Repository

```bash
git clone https://github.com/Linh22400/jframe-quanlycuahangxedien.git
cd shop-management
```

### Bước 2: Tạo Database

Chạy **TUẦN TỰ** 2 file SQL sau trong MySQL:

```bash
# 1. Tạo database và bảng cơ bản
mysql -u root -p < database.sql

# 2. Tạo bảng bổ sung (customers, invoices)
mysql -u root -p < database_update.sql
```

**Lưu ý**: File `database.sql` tạo database `shop_management`, bảng `users` và `vehicles`. File `database_update.sql` tạo thêm bảng `customers`, `invoices`, và `invoice_details`.

### Bước 3: Cấu Hình Kết Nối Database

Mở file `src/com/shop/dao/DatabaseConnection.java` và kiểm tra thông tin:

```java
private static final String URL = "jdbc:mysql://localhost:3306/shop_management";
private static final String USER = "root";
private static final String PASSWORD = "";
```

### Bước 4: Thêm MySQL Connector

**Eclipse:**
1. Right-click vào project → **Build Path** → **Configure Build Path**
2. Tab **Libraries** → **Add External JARs**
3. Chọn file `mysql-connector-java-x.x.x.jar`

**IntelliJ IDEA:**
1. **File** → **Project Structure** → **Libraries**
2. Click **+** → **Java** → Chọn file JAR

### Bước 5: Chạy Ứng Dụng

Chạy file `src/com/shop/main/Main.java`

**Tài khoản mặc định:**
- Username: `admin`
- Password: `123456`

---

## 🚀 Sử Dụng

### 1. Đăng Nhập
- Nhập `admin` / `123456` để đăng nhập
- Hệ thống sẽ hiển thị màn hình chính với 4 tab

### 2. Quản Lý Sản Phẩm
**Tab "SẢN PHẨM"**
- Click **Thêm Mới** → Chọn loại xe → Nhập thông tin → **Lưu**
- Click vào dòng cần sửa → **Sửa** → Cập nhật → **Lưu**
- Click vào dòng cần xóa → **Xóa** → Xác nhận
- Tìm kiếm: Nhập từ khóa → **Tìm kiếm**

### 3. Quản Lý Khách Hàng
**Tab "KHÁCH HÀNG"**
- Click **Thêm Khách** → Nhập SĐT, Tên, Địa chỉ → **Lưu**
- Sửa/Xóa tương tự như quản lý sản phẩm

### 4. Bán Hàng
**Tab "BÁN HÀNG"**
1. Chọn **Khách hàng** từ dropdown
2. Chọn **Xe** (chỉ hiện xe còn hàng)
3. Click **Thêm vào giỏ**
4. Lặp lại để thêm nhiều sản phẩm
5. Click **THANH TOÁN**
6. Hóa đơn sẽ hiển thị → Kho tự động cập nhật

### 5. Xem Lịch Sử & Báo Cáo
**Tab "LỊCH SỬ"**
- Xem tất cả hóa đơn đã bán
- Click chọn hóa đơn → **Xem chi tiết** để xem sản phẩm
- Tìm kiếm theo tên/SĐT khách hàng
- Xem thống kê doanh thu (hôm nay, tháng này, tổng đơn)

---

## 📁 Cấu Trúc Dự Án

```
baibaocao/
│
├── src/
│   └── com/shop/
│       ├── main/
│       │   └── Main.java                    # Entry point
│       │
│       ├── model/
│       │   ├── User.java                    # Model người dùng
│       │   ├── Vehicle.java                 # Abstract class xe điện
│       │   ├── ElectricMotorcycle.java      # Xe máy điện
│       │   ├── ElectricBike.java            # Xe đạp điện
│       │   ├── Customer.java                # Model khách hàng
│       │   ├── Invoice.java                 # Model hóa đơn
│       │   ├── InvoiceDetail.java           # Chi tiết hóa đơn
│       │   ├── InvoiceDTO.java              # DTO cho lịch sử
│       │   └── InvoiceDetailDTO.java        # DTO chi tiết
│       │
│       ├── dao/
│       │   ├── DatabaseConnection.java      # Kết nối DB
│       │   ├── UserDAO.java                 # DAO người dùng
│       │   ├── VehicleDAO.java              # DAO xe điện
│       │   ├── CustomerDAO.java             # DAO khách hàng
│       │   ├── InvoiceDAO.java              # DAO hóa đơn
│       │   └── InvoiceHistoryDAO.java       # DAO lịch sử & báo cáo
│       │
│       └── view/
│           ├── LoginFrame.java              # Màn hình đăng nhập
│           ├── MainFrame.java               # Màn hình chính (4 tabs)
│           ├── VehicleDialog.java           # Dialog thêm/sửa xe
│           ├── CustomerDialog.java          # Dialog thêm/sửa khách
│           ├── InvoiceDetailDialog.java     # Dialog xem chi tiết HĐ
│           └── UIConstants.java             # Hằng số UI (màu, font)
│
├── database.sql                              # SQL tạo DB & bảng chính
├── database_update.sql                       # SQL bảng bổ sung
└── README.md                                 # File này
```

**Tổng số files Java**: 18 files

---

## 🎓 Kiến Trúc & Design Patterns

### 1. MVC Pattern (Model-View-Controller)
- **Model**: Package `com.shop.model` - Các class đại diện cho dữ liệu
- **View**: Package `com.shop.view` - Giao diện người dùng (Swing)
- **Controller**: Logic xử lý trong các class DAO

### 2. DAO Pattern (Data Access Object)
- Tách biệt logic database khỏi business logic
- Mỗi entity có 1 DAO riêng

### 3. OOP Principles
- **Inheritance**: `Vehicle` → `ElectricMotorcycle`, `ElectricBike`
- **Polymorphism**: Xử lý chung Vehicle nhưng lưu riêng theo loại
- **Encapsulation**: Private fields + Getter/Setter

### 4. Transaction Management
- Sử dụng `setAutoCommit(false)` và `rollback()` trong `InvoiceDAO`
- Đảm bảo tính toàn vẹn dữ liệu khi bán hàng

---

## 📊 Database Schema

### Bảng `users`
```sql
id (PK) | username | password | full_name
```

### Bảng `vehicles`
```sql
id (PK) | code | name | brand | price | quantity | type | extra_1 | description
```

### Bảng `customers`
```sql
id (PK) | phone | name | address | created_at
```

### Bảng `invoices`
```sql
id (PK) | customer_id (FK) | user_id (FK) | total_amount | created_at
```

### Bảng `invoice_details`
```sql
id (PK) | invoice_id (FK) | vehicle_id (FK) | quantity | price
```

**Quan hệ:**
- `invoices.customer_id` → `customers.id`
- `invoices.user_id` → `users.id`
- `invoice_details.invoice_id` → `invoices.id`
- `invoice_details.vehicle_id` → `vehicles.id`

---

## 🎯 Điểm Nổi Bật

1. ✅ **Giao diện hiện đại**: Flat design, không sử dụng template
2. ✅ **Transaction an toàn**: Rollback khi lỗi, tránh lệch kho
3. ✅ **Session Management**: Theo dõi nhân viên bán hàng
4. ✅ **In hóa đơn**: Chi tiết ngay sau thanh toán
5. ✅ **Validation**: Kiểm tra tồn kho trước khi bán
6. ✅ **Lịch sử đầy đủ**: Xem lại mọi giao dịch đã thực hiện
7. ✅ **Báo cáo doanh thu**: Thống kê theo thời gian

---

## 🐛 Troubleshooting

### Lỗi kết nối Database
```
Error: Communications link failure
```
**Giải pháp:**
- Kiểm tra MySQL đã chạy chưa
- Kiểm tra username/password trong `DatabaseConnection.java`
- Đảm bảo database `shop_management` đã được tạo

### Lỗi ClassNotFoundException
```
java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
**Giải pháp:**
- Thêm `mysql-connector-java.jar` vào Build Path

### Lỗi Window Builder (Eclipse)
**Giải pháp:**
- Đảm bảo các Dialog có no-argument constructor
- Restart Eclipse nếu cần

---

## 📝 License

Dự án này được tạo cho mục đích học tập và báo cáo kỹ năng nghề nghiệp.

---

## 👨‍💻 Tác Giả

Nguyễn Hồng Linh
- Email: ln32587@gmail.com
- GitHub: https://github.com/Linh22400

---

## 🙏 Lời Cảm Ơn
- Thầy Võ Văn Phúc - giảng viên Trường Đại Học Nam Cần Thơ DNC
- Cộng đồng Stack Overflow

---

**⭐ Nếu dự án này hữu ích, hãy cho một Star nhé!**
