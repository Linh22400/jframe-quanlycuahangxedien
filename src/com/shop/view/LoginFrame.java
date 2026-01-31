package com.shop.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.shop.dao.UserDAO;
import com.shop.model.User;

/**
 * Màn hình Đăng nhập (Giao diện hiện đại).
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    public LoginFrame() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        // Panel nền
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.BG_COLOR);
        add(mainPanel);

        // Card Đăng nhập (Màu trắng, bo góc giả lập bằng Border)
        JPanel cardCheck = new JPanel(new GridBagLayout());
        cardCheck.setBackground(UIConstants.WHITE);
        cardCheck.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Tiêu đề
        JLabel lblHeader = new JLabel("LOGIN", JLabel.CENTER);
        lblHeader.setFont(UIConstants.HEADER_FONT);
        lblHeader.setForeground(UIConstants.PRIMARY_COLOR);
        gbc.gridy = 0;
        cardCheck.add(lblHeader, gbc);

        // Username
        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(UIConstants.NORMAL_FONT);
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 0, 5, 0); // Cách trên nhiều hơn
        cardCheck.add(lblUser, gbc);

        txtUsername = new JTextField("admin");
        txtUsername.setFont(UIConstants.NORMAL_FONT);
        txtUsername.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardCheck.add(txtUsername, gbc);

        // Password
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(UIConstants.NORMAL_FONT);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 5, 0);
        cardCheck.add(lblPass, gbc);

        txtPassword = new JPasswordField("123456");
        txtPassword.setFont(UIConstants.NORMAL_FONT);
        txtPassword.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardCheck.add(txtPassword, gbc);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UIConstants.WHITE);

        btnLogin = UIConstants.createPrimaryButton("Đăng Nhập");
        btnExit = new JButton("Thoát");
        btnExit.setFont(UIConstants.NORMAL_FONT);
        btnExit.setBackground(new Color(231, 76, 60)); // Màu đỏ
        btnExit.setForeground(Color.WHITE);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);

        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 0, 0, 0);
        cardCheck.add(btnPanel, gbc);

        // Xử lý sự kiện
        btnLogin.addActionListener(e -> handleLogin());
        btnExit.addActionListener(e -> System.exit(0));

        mainPanel.add(cardCheck);
    }

    // Xử lý logic đăng nhập
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.checkLogin(username, password);

        if (user != null) {
            MainFrame mainFrame = new MainFrame(user);
            mainFrame.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!", "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
