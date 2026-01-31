package com.shop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.shop.dao.InvoiceHistoryDAO;
import com.shop.model.InvoiceDetailDTO;

/**
 * Dialog hiển thị chi tiết 1 hóa đơn cụ thể.
 */
public class InvoiceDetailDialog extends JDialog {

    private int invoiceId;
    private String customerName;
    private String userName;
    private java.sql.Timestamp createdAt;
    private double totalAmount;

    private JTable table;
    private DefaultTableModel model;
    private InvoiceHistoryDAO dao;

    public InvoiceDetailDialog(JFrame parent, int invoiceId, String customerName,
            String userName, java.sql.Timestamp createdAt, double totalAmount) {
        super(parent, "Chi tiết Hóa đơn", true);
        this.invoiceId = invoiceId;
        this.customerName = customerName;
        this.userName = userName;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.dao = new InvoiceHistoryDAO();

        initUI();
        loadData();
    }

    private void initUI() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        JLabel lblTitle = new JLabel("HÓA ĐƠN #" + invoiceId + " - " + sdf.format(createdAt));
        lblTitle.setFont(UIConstants.BOLD_FONT.deriveFont(18f));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(UIConstants.PRIMARY_COLOR);
        JLabel lblCustomer = new JLabel("Khách hàng: " + customerName);
        lblCustomer.setFont(UIConstants.NORMAL_FONT);
        lblCustomer.setForeground(Color.WHITE);
        JLabel lblUser = new JLabel("Thu ngân: " + userName);
        lblUser.setFont(UIConstants.NORMAL_FONT);
        lblUser.setForeground(Color.WHITE);
        infoPanel.add(lblCustomer, BorderLayout.WEST);
        infoPanel.add(lblUser, BorderLayout.EAST);
        headerPanel.add(infoPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
                new String[] { "Mã xe", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(UIConstants.NORMAL_FONT);
        table.getTableHeader().setFont(UIConstants.BOLD_FONT);
        table.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 35));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer (Total)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        JLabel lblTotal = new JLabel("TỔNG TIỀN: " + currency.format(totalAmount));
        lblTotal.setFont(UIConstants.BOLD_FONT.deriveFont(20f));
        lblTotal.setForeground(new Color(220, 53, 69));
        lblTotal.setHorizontalAlignment(JLabel.RIGHT);
        footerPanel.add(lblTotal);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {
        model.setRowCount(0);
        List<InvoiceDetailDTO> details = dao.getInvoiceDetails(invoiceId);
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (InvoiceDetailDTO item : details) {
            model.addRow(new Object[] {
                    item.getVehicleCode(),
                    item.getVehicleName(),
                    item.getQuantity(),
                    currency.format(item.getPrice()),
                    currency.format(item.getSubtotal())
            });
        }
    }
}
