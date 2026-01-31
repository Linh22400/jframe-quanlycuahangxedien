package com.shop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.shop.model.ElectricBike;
import com.shop.model.ElectricMotorcycle;
import com.shop.model.Vehicle;

/**
 * Dialog Thêm/Sửa (Giao diện hiện đại).
 */
public class VehicleDialog extends JDialog {
    private JTextField txtCode, txtName, txtBrand, txtPrice, txtQuantity, txtExtra;
    private JComboBox<String> cbType;
    private JLabel lblExtra;
    private JButton btnSave, btnCancel;

    private boolean succeeded = false;
    private Vehicle vehicle = null;

    private static final String TYPE_MOTO = "Xe Máy Điện";
    private static final String TYPE_EBIKE = "Xe Đạp Điện";

    /**
     * @wbp.parser.constructor
     */
    public VehicleDialog(JFrame parent) {
        super(parent, "Thêm Phương Tiện Mới", true);
        initUI();
    }

    public VehicleDialog(JFrame parent, Vehicle existingVehicle) {
        super(parent, "Cập Nhật Thông Tin", true);
        this.vehicle = existingVehicle;
        initUI();
        fillData();
    }

    private void initUI() {
        setSize(450, 600); // Increased from 500 to fit all fields
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        JLabel lblHeader = new JLabel(vehicle == null ? "THÊM SẢN PHẨM" : "SỬA SẢN PHẨM");
        lblHeader.setFont(UIConstants.BOLD_FONT);
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Helper để add component nhanh
        int y = 0;
        addFormRow(formPanel, gbc, y++, "Loại xe:", cbType = new JComboBox<>(new String[] { TYPE_MOTO, TYPE_EBIKE }));
        cbType.addActionListener(e -> updateExtraLabel());

        addFormRow(formPanel, gbc, y++, "Mã sản phẩm:", txtCode = new JTextField());
        addFormRow(formPanel, gbc, y++, "Tên sản phẩm:", txtName = new JTextField());
        addFormRow(formPanel, gbc, y++, "Hãng sản xuất:", txtBrand = new JTextField());
        addFormRow(formPanel, gbc, y++, "Giá bán (VNĐ):", txtPrice = new JTextField());
        addFormRow(formPanel, gbc, y++, "Số lượng tồn:", txtQuantity = new JTextField());

        // Extra field - Window Builder needs separate constraint object
        GridBagConstraints gbc_1 = new GridBagConstraints();
        gbc_1.insets = new Insets(8, 0, 8, 0);
        gbc_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_1.weightx = 1.0;
        gbc_1.gridx = 0;

        lblExtra = new JLabel("Công suất (W):");
        lblExtra.setFont(UIConstants.NORMAL_FONT);
        gbc_1.gridy = y * 2;
        formPanel.add(lblExtra, gbc_1);

        GridBagConstraints gbc_2 = new GridBagConstraints();
        gbc_2.insets = new Insets(8, 0, 8, 0);
        gbc_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_2.weightx = 1.0;
        gbc_2.gridx = 0;

        txtExtra = new JTextField();
        gbc_2.gridy = y * 2 + 1;
        formPanel.add(txtExtra, gbc_2);

        // Wrap formPanel in ScrollPane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UIConstants.BG_COLOR);

        btnSave = UIConstants.createPrimaryButton("Lưu Thông Tin");
        btnCancel = new JButton("Hủy Bỏ");

        // Style nút Hủy
        btnCancel.setBackground(new Color(189, 195, 199));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        btnSave.addActionListener(e -> saveVehicle());
        btnCancel.addActionListener(e -> dispose());

        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel);

        updateExtraLabel();
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int y, String label, java.awt.Component comp) {
        gbc.gridy = y * 2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.NORMAL_FONT);
        panel.add(lbl, gbc);

        gbc.gridy = y * 2 + 1;
        panel.add(comp, gbc);
    }

    private void updateExtraLabel() {
        String selected = (String) cbType.getSelectedItem();
        if (TYPE_MOTO.equals(selected)) {
            lblExtra.setText("Công suất (W):");
        } else {
            lblExtra.setText("Pin / Sạc (Ah):");
        }
    }

    private void fillData() {
        if (vehicle == null)
            return;

        txtCode.setText(vehicle.getCode());
        txtCode.setEditable(false);
        txtName.setText(vehicle.getName());
        txtBrand.setText(vehicle.getBrand());
        txtPrice.setText(String.format("%.0f", vehicle.getPrice())); // Bỏ số decimal
        txtQuantity.setText(String.valueOf(vehicle.getQuantity()));

        if (vehicle instanceof ElectricMotorcycle) {
            cbType.setSelectedItem(TYPE_MOTO);
            txtExtra.setText(((ElectricMotorcycle) vehicle).getPower());
        } else if (vehicle instanceof ElectricBike) {
            cbType.setSelectedItem(TYPE_EBIKE);
            txtExtra.setText(((ElectricBike) vehicle).getBatteryInfo());
        }
        cbType.setEnabled(false);
    }

    private void saveVehicle() {
        if (txtCode.getText().isEmpty() || txtName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã và Tên không được để trống!");
            return;
        }

        try {
            double price = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQuantity.getText());

            if (vehicle == null) {
                if (TYPE_MOTO.equals(cbType.getSelectedItem())) {
                    vehicle = new ElectricMotorcycle();
                } else {
                    vehicle = new ElectricBike();
                }
            }

            vehicle.setCode(txtCode.getText());
            vehicle.setName(txtName.getText());
            vehicle.setBrand(txtBrand.getText());
            vehicle.setPrice(price);
            vehicle.setQuantity(qty);
            vehicle.setDescription("");

            if (vehicle instanceof ElectricMotorcycle) {
                ((ElectricMotorcycle) vehicle).setPower(txtExtra.getText());
            } else if (vehicle instanceof ElectricBike) {
                ((ElectricBike) vehicle).setBatteryInfo(txtExtra.getText());
            }

            succeeded = true;
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và Số lượng phải là số hợp lệ!");
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
