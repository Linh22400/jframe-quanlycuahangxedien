package com.shop.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * Lớp chứa các hằng số và tiện ích giao diện dùng chung.
 * Giúp đồng bộ màu sắc và font chữ toàn ứng dụng.
 */
public class UIConstants {
    // Màu sắc chủ đạo (Xanh dương hiện đại)
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Xanh đậm
    public static final Color ACCENT_COLOR = new Color(52, 152, 219); // Xanh nhạt
    public static final Color BG_COLOR = new Color(236, 240, 241); // Xám nhạt nền
    public static final Color TEXT_COLOR = new Color(44, 62, 80); // Chữ đen xám
    public static final Color WHITE = Color.WHITE;

    // Font chữ
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Tạo Button theo style phẳng hiện đại.
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BOLD_FONT);
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setMargin(new Insets(8, 16, 8, 16)); // Padding

        // Hiệu ứng Hover đơn giản (thay đổi màu khi rê chuột)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(ACCENT_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY_COLOR);
            }
        });
        return btn;
    }
}
