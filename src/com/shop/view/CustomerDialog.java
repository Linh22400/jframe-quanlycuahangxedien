package com.shop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.shop.model.Customer;

public class CustomerDialog extends JDialog {
    private JTextField txtPhone, txtName, txtAddress;
    private JButton btnSave, btnCancel;

    private boolean succeeded = false;
    private Customer customer = null;

    /**
     * Constructor cho Window Builder (chế độ thiết kế)
     */
    public CustomerDialog() {
        super((JFrame) null, "Thêm Khách Hàng", true);
        initUI();
    }

    public CustomerDialog(JFrame parent) {
        super(parent, "Thêm Khách Hàng", true);
        initUI();
    }

    public CustomerDialog(JFrame parent, Customer existing) {
        super(parent, "Sửa Khách Hàng", true);
        this.customer = existing;
        initUI();
        fillData();
    }

    private void initUI() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JLabel lblHeader = new JLabel("THÔNG TIN KHÁCH HÀNG", JLabel.CENTER);
        lblHeader.setFont(UIConstants.BOLD_FONT);
        lblHeader.setForeground(UIConstants.PRIMARY_COLOR);
        lblHeader.setBorder(new EmptyBorder(10, 0, 10, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        int y = 0;
        addFormRow(formPanel, gbc, y++, "Số điện thoại:", txtPhone = new JTextField());
        addFormRow(formPanel, gbc, y++, "Họ tên:", txtName = new JTextField());
        addFormRow(formPanel, gbc, y++, "Địa chỉ:", txtAddress = new JTextField());

        // Wrap in ScrollPane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnSave = UIConstants.createPrimaryButton("Lưu");
        btnCancel = new JButton("Hủy");
        btnCancel.setFont(UIConstants.BOLD_FONT);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setOpaque(true);
        btnCancel.setBorderPainted(false);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField txt) {
        gbc.gridy = y * 2;
        panel.add(new JLabel(label), gbc);
        gbc.gridy = y * 2 + 1;
        panel.add(txt, gbc);
    }

    private void fillData() {
        if (customer != null) {
            txtPhone.setText(customer.getPhone());
            txtName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
        }
    }

    private void save() {
        if (txtPhone.getText().isEmpty() || txtName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên và SĐT!");
            return;
        }

        if (customer == null)
            customer = new Customer();
        customer.setPhone(txtPhone.getText());
        customer.setName(txtName.getText());
        customer.setAddress(txtAddress.getText());

        succeeded = true;
        dispose();
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public Customer getCustomer() {
        return customer;
    }
}
