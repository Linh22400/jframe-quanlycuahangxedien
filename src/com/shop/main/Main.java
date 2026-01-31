package com.shop.main;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.shop.view.LoginFrame;

/**
 * Class chính khởi chạy ứng dụng.
 * Cập nhật: Set font mặc định cho toàn bộ ứng dụng.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set native LookAndFeel (Windows)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Set font mặc định toàn cục
                setUIFont(new FontUIResource("Segoe UI", Font.PLAIN, 14));

            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    // Hàm helper để set font cho tất cả component
    /**
     * @wbp.parser.entryPoint
     */
    public static void setUIFont(FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
