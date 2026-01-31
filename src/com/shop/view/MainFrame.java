package com.shop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

import com.shop.dao.CustomerDAO;
import com.shop.dao.InvoiceHistoryDAO;
import com.shop.dao.VehicleDAO;
import com.shop.model.Customer;
import com.shop.model.ElectricBike;
import com.shop.model.ElectricMotorcycle;
import com.shop.model.InvoiceDetail;
import com.shop.model.InvoiceDTO;
import com.shop.model.Vehicle;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;

    // --- Các component cho Tab Sản phẩm ---
    private JTable tblVehicle;
    private DefaultTableModel modelVehicle;
    private VehicleDAO vehicleDAO;
    private JTextField txtSearchVehicle;
    private JComboBox<String> cbFilterType;
    private JLabel lblTotalQty, lblTotalValue;

    // --- Các component cho Tab Khách hàng ---
    private JTable tblCustomer;
    private DefaultTableModel modelCustomer;
    private CustomerDAO customerDAO;
    private JTextField txtSearchCustomer;

    // --- Các component cho Tab Lịch sử ---
    private JTable tblHistory;
    private DefaultTableModel modelHistory;
    private InvoiceHistoryDAO historyDAO;
    private JTextField txtSearchHistory;
    private JLabel lblRevenueToday, lblRevenueMonth, lblTotalOrders;

    // User đã đăng nhập
    private com.shop.model.User currentUser;

    /**
     * Constructor dùng khi chạy thật (với user đã đăng nhập)
     */
    public MainFrame(com.shop.model.User user) {
        this.currentUser = user;
        vehicleDAO = new VehicleDAO();
        customerDAO = new CustomerDAO();
        historyDAO = new InvoiceHistoryDAO();

        setTitle("Hệ Thống Quản Lý Cửa Hàng Xe Điện - Xin chào: " + (user != null ? user.getFullName() : "Admin"));
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    /**
     * Constructor không tham số chỉ dùng cho Window Builder (chế độ thiết kế).
     * KHÔNG sử dụng constructor này trong code thật!
     */
    public MainFrame() {
        // Tạo user giả để thiết kế giao diện
        this.currentUser = new com.shop.model.User();
        this.currentUser.setId(1);
        this.currentUser.setFullName("Design Mode User");

        vehicleDAO = new VehicleDAO();
        customerDAO = new CustomerDAO();

        setTitle("Hệ Thống Quản Lý Cửa Hàng Xe Điện");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.BOLD_FONT);
        tabbedPane.setForeground(Color.BLACK); // Chữ tab màu đen để thấy rõ
        tabbedPane.setBackground(Color.WHITE);

        // Tab 1: Sản phẩm
        tabbedPane.addTab("SẢN PHẨM", createVehiclePanel());

        // Tab 2: Khách hàng
        tabbedPane.addTab("KHÁCH HÀNG", createCustomerPanel());

        // Tab 3: Bán hàng
        tabbedPane.addTab("BÁN HÀNG", createSalesPanel());

        // Tab 4: Lịch sử
        tabbedPane.addTab("LỊCH SỬ", createHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ================== SALES TAB ==================
    // Các component cho Bán hàng
    private JComboBox<Customer> cbCustomer;
    private JComboBox<Vehicle> cbVehicle;
    private JTable tblCart;
    private DefaultTableModel modelCart;
    private JLabel lblCartTotal;
    private java.util.List<InvoiceDetail> cartDetails = new java.util.ArrayList<>();

    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIConstants.BG_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Top: Selection ---
        JPanel topPanel = new JPanel(new java.awt.GridLayout(3, 2, 10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đơn hàng"));

        // Customer Select
        cbCustomer = new JComboBox<>();
        refreshCustomerCombo();

        // Vehicle Select
        cbVehicle = new JComboBox<>();
        refreshVehicleCombo();

        JButton btnAddToCart = UIConstants.createPrimaryButton("Thêm vào giỏ");

        topPanel.add(new JLabel("Khách hàng:"));
        topPanel.add(cbCustomer);
        topPanel.add(new JLabel("Chọn xe:"));
        topPanel.add(cbVehicle);
        topPanel.add(new JLabel("")); // Spacer
        topPanel.add(btnAddToCart);

        btnAddToCart.addActionListener(e -> addToCart());

        panel.add(topPanel, BorderLayout.NORTH);

        // --- Center: Cart Table ---
        String[] cols = { "Mã Xe", "Tên Xe", "Đơn Giá", "Số Lượng", "Thành Tiền" };
        modelCart = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblCart = new JTable(modelCart);
        styleTable(tblCart);

        panel.add(new JScrollPane(tblCart), BorderLayout.CENTER);

        // --- Bottom: Actions ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        lblCartTotal = new JLabel("Tổng tiền: 0 đ");
        lblCartTotal.setFont(UIConstants.HEADER_FONT);
        lblCartTotal.setForeground(Color.RED);

        JButton btnCheckout = UIConstants.createPrimaryButton("THANH TOÁN & IN HÓA ĐƠN");
        btnCheckout.setBackground(new Color(39, 174, 96)); // Green
        btnCheckout.setPreferredSize(new Dimension(250, 50));

        btnCheckout.addActionListener(e -> checkout());

        bottomPanel.add(lblCartTotal, BorderLayout.WEST);
        bottomPanel.add(btnCheckout, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // --- Các hàm hỗ trợ cho Bán hàng ---
    private void refreshCustomerCombo() {
        cbCustomer.removeAllItems();
        List<Customer> list = customerDAO.getAllCustomers();
        for (Customer c : list)
            cbCustomer.addItem(c);
    }

    private void refreshVehicleCombo() {
        cbVehicle.removeAllItems();
        List<Vehicle> list = vehicleDAO.getAllVehicles();
        for (Vehicle v : list) {
            if (v.getQuantity() > 0)
                cbVehicle.addItem(v); // Chỉ hiện xe còn hàng
        }
    }

    private void addToCart() {
        Vehicle v = (Vehicle) cbVehicle.getSelectedItem();
        if (v == null)
            return;

        // Kiểm tra nếu đã có trong giỏ
        for (InvoiceDetail item : cartDetails) {
            if (item.getVehicleId() == v.getId()) {
                if (item.getQuantity() >= v.getQuantity()) {
                    JOptionPane.showMessageDialog(this, "Đã hết hàng trong kho!");
                    return;
                }
                item.setQuantity(item.getQuantity() + 1);
                refreshCartTable();
                return;
            }
        }

        // Thêm mới
        InvoiceDetail newItem = new InvoiceDetail();
        newItem.setVehicleId(v.getId());
        newItem.setVehicleName(v.getName());
        newItem.setPrice(v.getPrice());
        newItem.setQuantity(1);
        cartDetails.add(newItem);
        refreshCartTable();
    }

    private void refreshCartTable() {
        modelCart.setRowCount(0);
        double total = 0;
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (InvoiceDetail item : cartDetails) {
            double amount = item.getPrice() * item.getQuantity();
            total += amount;
            modelCart.addRow(new Object[] {
                    item.getVehicleId(), item.getVehicleName(),
                    currency.format(item.getPrice()), item.getQuantity(), currency.format(amount)
            });
        }
        lblCartTotal.setText("Tổng tiền: " + currency.format(total));
    }

    private void checkout() {
        if (cartDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }

        Customer c = (Customer) cbCustomer.getSelectedItem();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
            return;
        }

        // Xác nhận
        if (JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán?", "Thanh toán",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        // Chuẩn bị chuỗi chi tiết hóa đơn
        StringBuilder detailsStr = new StringBuilder();
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        for (InvoiceDetail item : cartDetails) {
            detailsStr.append(item.getVehicleName())
                    .append(" x").append(item.getQuantity())
                    .append(" : ").append(currency.format(item.getPrice() * item.getQuantity()))
                    .append("\n");
        }

        // Tạo đối tượng Invoice
        com.shop.model.Invoice inv = new com.shop.model.Invoice();
        inv.setCustomerId(c.getId());
        inv.setUserId(currentUser.getId()); // Set user đang đăng nhập
        double total = cartDetails.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        inv.setTotalAmount(total);

        // Gọi DAO
        com.shop.dao.InvoiceDAO invDAO = new com.shop.dao.InvoiceDAO();
        if (invDAO.createInvoice(inv, cartDetails)) {
            // Hiển thị hóa đơn TRƯỚC thông báo thành công
            showBillDialog(detailsStr.toString(), total, c);

            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            cartDetails.clear();
            refreshCartTable();
            refreshVehicleCombo(); // Cập nhật kho trong combo
            loadVehicleData(); // Cập nhật kho trong Tab Sản phẩm
        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại!");
        }
    }

    private void showBillDialog(String details, double total, Customer c) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        StringBuilder sb = new StringBuilder();
        sb.append("========== HÓA ĐƠN ==========\n");
        sb.append("Ngày: ").append(new java.util.Date()).append("\n");
        sb.append("Khách hàng: ").append(c.getName()).append("\n");
        sb.append("SĐT: ").append(c.getPhone()).append("\n");
        sb.append("Thu ngân: ").append(currentUser.getFullName()).append("\n");
        sb.append("-----------------------------\n");
        sb.append(details);
        sb.append("-----------------------------\n");
        sb.append("TỔNG TIỀN: ").append(currency.format(total)).append("\n");
        sb.append("=============================\n");
        sb.append("Cảm ơn quý khách!");

        javax.swing.JTextArea textArea = new javax.swing.JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

        JOptionPane.showMessageDialog(this, new javax.swing.JScrollPane(textArea), "In Hóa Đơn",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================== VEHICLE TAB ==================
    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIConstants.BG_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top: Stats + Filter
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Stats
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statsPanel.setOpaque(false);
        lblTotalQty = new JLabel("Tổng xe: 0");
        lblTotalQty.setFont(UIConstants.BOLD_FONT);
        lblTotalValue = new JLabel("Tổng giá trị: 0 đ");
        lblTotalValue.setFont(UIConstants.BOLD_FONT);
        lblTotalValue.setForeground(new Color(192, 57, 43));
        statsPanel.add(lblTotalQty);
        statsPanel.add(lblTotalValue);
        topPanel.add(statsPanel, BorderLayout.NORTH);

        // Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        txtSearchVehicle = new JTextField(15);
        JButton btnSearch = UIConstants.createPrimaryButton("Tìm");
        cbFilterType = new JComboBox<>(new String[] { "Tất cả", "Xe Máy Điện", "Xe Đạp Điện" });

        filterPanel.add(new JLabel("Tìm kiếm: "));
        filterPanel.add(txtSearchVehicle);
        filterPanel.add(btnSearch);
        filterPanel.add(new JLabel("  Loại: "));
        filterPanel.add(cbFilterType);

        btnSearch.addActionListener(e -> loadVehicleData());
        cbFilterType.addActionListener(e -> loadVehicleData());

        topPanel.add(filterPanel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.NORTH);

        // Center: Table
        String[] cols = { "Mã", "Tên Xe", "Hãng", "Giá (đ)", "Tồn", "Loại", "Info" };
        modelVehicle = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblVehicle = new JTable(modelVehicle);
        styleTable(tblVehicle);

        tblVehicle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    editVehicle();
            }
        });

        panel.add(new JScrollPane(tblVehicle), BorderLayout.CENTER);

        // Bottom: Actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton btnAdd = UIConstants.createPrimaryButton("Thêm");
        JButton btnEdit = UIConstants.createPrimaryButton("Sửa");
        JButton btnDel = new JButton("Xóa");
        btnDel.setFont(UIConstants.BOLD_FONT);
        btnDel.setBackground(new Color(231, 76, 60));
        btnDel.setForeground(Color.WHITE);
        btnDel.setOpaque(true);
        btnDel.setBorderPainted(false);

        btnAdd.addActionListener(e -> addVehicle());
        btnEdit.addActionListener(e -> editVehicle());
        btnDel.addActionListener(e -> deleteVehicle());

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDel);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadVehicleData(); // Initial load
        return panel;
    }

    // ================== CUSTOMER TAB ==================
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIConstants.BG_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top: Filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        txtSearchCustomer = new JTextField(20);
        JButton btnSearch = UIConstants.createPrimaryButton("Tìm Khách");

        topPanel.add(new JLabel("Tên/SĐT: "));
        topPanel.add(txtSearchCustomer);
        topPanel.add(btnSearch);

        btnSearch.addActionListener(e -> loadCustomerData());
        panel.add(topPanel, BorderLayout.NORTH);

        // Center: Table
        String[] cols = { "ID", "Số Điện Thoại", "Họ Tên", "Địa Chỉ", "Ngày Tạo" };
        modelCustomer = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblCustomer = new JTable(modelCustomer);
        styleTable(tblCustomer);

        tblCustomer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    editCustomer();
            }
        });

        panel.add(new JScrollPane(tblCustomer), BorderLayout.CENTER);

        // Bottom: Actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton btnAdd = UIConstants.createPrimaryButton("Thêm Khách");
        JButton btnEdit = UIConstants.createPrimaryButton("Sửa");
        JButton btnDel = new JButton("Xóa");
        btnDel.setFont(UIConstants.BOLD_FONT);
        btnDel.setBackground(new Color(231, 76, 60));
        btnDel.setForeground(Color.WHITE);
        btnDel.setOpaque(true);
        btnDel.setBorderPainted(false);

        btnAdd.addActionListener(e -> addCustomer());
        btnEdit.addActionListener(e -> editCustomer());
        btnDel.addActionListener(e -> deleteCustomer());

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDel);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadCustomerData();
        return panel;
    }

    // --- Tab 4: Lịch sử Hóa đơn & Báo cáo ---
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIConstants.BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top: Revenue Statistics
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Báo cáo Doanh thu"));

        lblRevenueToday = createStatLabel("Hôm nay:", "0 ₫");
        lblRevenueMonth = createStatLabel("Tháng này:", "0 ₫");
        lblTotalOrders = createStatLabel("Tổng đơn:", "0");

        statsPanel.add(lblRevenueToday);
        statsPanel.add(lblRevenueMonth);
        statsPanel.add(lblTotalOrders);

        panel.add(statsPanel, BorderLayout.NORTH);

        // Center: Table
        modelHistory = new DefaultTableModel(
                new String[] { "Mã HĐ", "Ngày", "Khách hàng", "SĐT", "Thu ngân", "Tổng tiền" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHistory = new JTable(modelHistory);
        styleTable(tblHistory);
        JScrollPane scrollPane = new JScrollPane(tblHistory);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom: Search & Actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm khách:");
        lblSearch.setFont(UIConstants.NORMAL_FONT);
        txtSearchHistory = new JTextField(15);
        JButton btnSearch = UIConstants.createPrimaryButton("Tìm");
        JButton btnViewAll = new JButton("Xem tất cả");
        JButton btnViewDetail = UIConstants.createPrimaryButton("Xem chi tiết");

        btnSearch.addActionListener(e -> searchInvoiceHistory());
        btnViewAll.addActionListener(e -> loadInvoiceHistory());
        btnViewDetail.addActionListener(e -> viewInvoiceDetail());

        bottomPanel.add(lblSearch);
        bottomPanel.add(txtSearchHistory);
        bottomPanel.add(btnSearch);
        bottomPanel.add(btnViewAll);
        bottomPanel.add(btnViewDetail);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadInvoiceHistory();
        refreshRevenueStats();
        return panel;
    }

    // Helper method to create stat labels
    private JLabel createStatLabel(String title, String value) {
        JLabel label = new JLabel("<html><b>" + title + "</b> " + value + "</html>");
        label.setFont(UIConstants.NORMAL_FONT.deriveFont(16f));
        label.setForeground(UIConstants.PRIMARY_COLOR);
        return label;
    }

    // Hàm hỗ trợ: Trang trí Table
    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(UIConstants.NORMAL_FONT);
        table.setSelectionBackground(new Color(174, 214, 241));

        // Custom header renderer để đảm bảo màu hiển thị đúng
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value == null ? "" : value.toString());
                label.setFont(UIConstants.BOLD_FONT);
                label.setBackground(UIConstants.PRIMARY_COLOR);
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                return label;
            }
        });
        header.setPreferredSize(new Dimension(100, 35));
        header.setReorderingAllowed(false); // Không cho phép kéo cột
    }

    // --- Logic xử lý Xe điện ---
    private void loadVehicleData() {
        modelVehicle.setRowCount(0);
        String kw = txtSearchVehicle.getText();
        String type = (String) cbFilterType.getSelectedItem();

        List<Vehicle> list = (kw.isEmpty()) ? vehicleDAO.getAllVehicles() : vehicleDAO.searchVehicles(kw);
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Vehicle v : list) {
            String vTypeStr = (v instanceof ElectricMotorcycle) ? "Xe Máy Điện"
                    : (v instanceof ElectricBike) ? "Xe Đạp Điện" : "Khác";
            String extra = (v instanceof ElectricMotorcycle) ? ((ElectricMotorcycle) v).getPower()
                    : (v instanceof ElectricBike) ? ((ElectricBike) v).getBatteryInfo() : "";

            if (!"Tất cả".equals(type) && !vTypeStr.equals(type))
                continue;

            modelVehicle.addRow(new Object[] {
                    v.getCode(), v.getName(), v.getBrand(), currency.format(v.getPrice()),
                    v.getQuantity(), vTypeStr, extra
            });
        }
        updateStats();
    }

    private void updateStats() {
        lblTotalQty.setText("Tổng tồn: " + vehicleDAO.getTotalQuantity());
        lblTotalValue.setText("Tổng giá trị: "
                + NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(vehicleDAO.getTotalValue()));
    }

    private void addVehicle() {
        VehicleDialog d = new VehicleDialog(this);
        d.setVisible(true);
        if (d.isSucceeded() && vehicleDAO.addVehicle(d.getVehicle())) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadVehicleData();
        }
    }

    private void editVehicle() {
        int r = tblVehicle.getSelectedRow();
        if (r < 0)
            return;
        String code = (String) tblVehicle.getValueAt(r, 0);
        Vehicle v = vehicleDAO.searchVehicles(code).get(0);
        VehicleDialog d = new VehicleDialog(this, v);
        d.setVisible(true);
        if (d.isSucceeded() && vehicleDAO.updateVehicle(d.getVehicle())) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadVehicleData();
        }
    }

    private void deleteVehicle() {
        int r = tblVehicle.getSelectedRow();
        if (r < 0)
            return;
        if (JOptionPane.showConfirmDialog(this, "Xóa xe này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
            vehicleDAO.deleteVehicle((String) tblVehicle.getValueAt(r, 0));
            loadVehicleData();
        }
    }

    // --- Logic xử lý Khách hàng ---
    private void loadCustomerData() {
        modelCustomer.setRowCount(0);
        String kw = txtSearchCustomer.getText();
        List<Customer> list = (kw.isEmpty()) ? customerDAO.getAllCustomers() : customerDAO.searchCustomers(kw);

        for (Customer c : list) {
            modelCustomer.addRow(new Object[] {
                    c.getId(), c.getPhone(), c.getName(), c.getAddress(), c.getCreatedAt()
            });
        }
    }

    private void addCustomer() {
        CustomerDialog d = new CustomerDialog(this);
        d.setVisible(true);
        if (d.isSucceeded() && customerDAO.addCustomer(d.getCustomer())) {
            JOptionPane.showMessageDialog(this, "Thêm khách thành công!");
            loadCustomerData();
            refreshCustomerCombo(); // Cập nhật dropdown trong tab Bán hàng
        }
    }

    private void editCustomer() {
        int r = tblCustomer.getSelectedRow();
        if (r < 0)
            return;
        int id = (int) tblCustomer.getValueAt(r, 0);
        // Tìm lại từ list hiện tại hoặc query DB (Query lại cho chắc)
        // Ở đây mình lấy tạm từ row vì DAO search theo ID chưa viết, nhưng
        // searchCustomer cũng trả về list
        // Để nhanh, ta lấy thông tin từ row -> tạo object
        Customer c = new Customer();
        c.setId(id);
        c.setPhone((String) tblCustomer.getValueAt(r, 1));
        c.setName((String) tblCustomer.getValueAt(r, 2));
        c.setAddress((String) tblCustomer.getValueAt(r, 3));

        CustomerDialog d = new CustomerDialog(this, c);
        d.setVisible(true);
        if (d.isSucceeded() && customerDAO.updateCustomer(d.getCustomer())) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadCustomerData();
            refreshCustomerCombo(); // Cập nhật dropdown trong tab Bán hàng
        }
    }

    private void deleteCustomer() {
        int r = tblCustomer.getSelectedRow();
        if (r < 0)
            return;
        if (JOptionPane.showConfirmDialog(this, "Xóa khách này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
            customerDAO.deleteCustomer((int) tblCustomer.getValueAt(r, 0));
            loadCustomerData();
        }
    }

    // --- Logic xử lý Lịch sử Hóa đơn ---
    private void loadInvoiceHistory() {
        modelHistory.setRowCount(0);
        List<InvoiceDTO> list = historyDAO.getAllInvoices();
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (InvoiceDTO inv : list) {
            modelHistory.addRow(new Object[] {
                    inv.getInvoiceId(),
                    sdf.format(inv.getCreatedAt()),
                    inv.getCustomerName(),
                    inv.getCustomerPhone(),
                    inv.getUserName(),
                    currency.format(inv.getTotalAmount())
            });
        }
    }

    private void searchInvoiceHistory() {
        String keyword = txtSearchHistory.getText().trim();
        if (keyword.isEmpty()) {
            loadInvoiceHistory();
            return;
        }

        modelHistory.setRowCount(0);
        List<InvoiceDTO> list = historyDAO.searchInvoicesByCustomer(keyword);
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (InvoiceDTO inv : list) {
            modelHistory.addRow(new Object[] {
                    inv.getInvoiceId(),
                    sdf.format(inv.getCreatedAt()),
                    inv.getCustomerName(),
                    inv.getCustomerPhone(),
                    inv.getUserName(),
                    currency.format(inv.getTotalAmount())
            });
        }
    }

    private void viewInvoiceDetail() {
        int row = tblHistory.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xem!");
            return;
        }

        int invoiceId = (int) tblHistory.getValueAt(row, 0);
        String customerName = (String) tblHistory.getValueAt(row, 2);
        String userName = (String) tblHistory.getValueAt(row, 4);

        // Parse date
        String dateStr = (String) tblHistory.getValueAt(row, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.sql.Timestamp timestamp = null;
        try {
            timestamp = new java.sql.Timestamp(sdf.parse(dateStr).getTime());
        } catch (Exception e) {
            timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        }

        // Parse total
        String totalStr = ((String) tblHistory.getValueAt(row, 5)).replaceAll("[^0-9]", "");
        double total = Double.parseDouble(totalStr);

        InvoiceDetailDialog dialog = new InvoiceDetailDialog(this, invoiceId,
                customerName, userName, timestamp, total);
        dialog.setVisible(true);
    }

    private void refreshRevenueStats() {
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        double today = historyDAO.getRevenueToday();
        double month = historyDAO.getRevenueThisMonth();
        int totalOrders = historyDAO.getTotalInvoicesCount();

        lblRevenueToday.setText("<html><b>Hôm nay:</b> " + currency.format(today) + "</html>");
        lblRevenueMonth.setText("<html><b>Tháng này:</b> " + currency.format(month) + "</html>");
        lblTotalOrders.setText("<html><b>Tổng đơn:</b> " + totalOrders + "</html>");
    }
}
