import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UIHelper {

    // 1. Inisialisasi Tema Global (Light Mode dengan Aksen Teal)
    public static void setupFlatLaf() {
        try {
            // Menggunakan tema FlatMacLightLaf
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            
            // Warna Aksen Utama (Teal / Pastel Green-Blue)
            Color primaryColor = new Color(46, 172, 163); 
            UIManager.put("Component.accentColor", primaryColor);
            
            // Konfigurasi Global Styling (Rounded Corners & Clean Look)
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("ProgressBar.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            
            // Padding default untuk TextComponent (Inner Padding)
            UIManager.put("TextComponent.margin", new Insets(10, 15, 10, 15));
            UIManager.put("ComboBox.padding", new Insets(8, 15, 8, 15));
            UIManager.put("ComboBox.arc", 15);
            
            // Font default agar seragam
            UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 14));
            
        } catch (Exception ex) {
            System.err.println("Gagal menginisialisasi FlatLaf");
        }
    }

    // 2. Styling Header
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.BOLD, 26));
        label.setForeground(new Color(40, 50, 60)); // Darker gray untuk light mode
        return label;
    }

    // 2.1 Styling Label Form
    public static void styleFormLabel(JLabel label) {
        label.setFont(new Font("Inter", Font.BOLD, 14));
        label.setForeground(new Color(100, 100, 100));
    }

    // 3. Styling Primary Button (Besar & Menonjol)
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.putClientProperty("JButton.buttonType", "roundRect"); 
        button.putClientProperty("FlatLaf.styleClass", "primary"); // Menggunakan accent color
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50)); // Lebih besar
        return button;
    }

    // 4. Styling Total Pembayaran
    public static JLabel createTotalLabel() {
        JLabel label = new JLabel("Rp 0");
        label.setFont(new Font("Inter", Font.BOLD, 36));
        label.setForeground(new Color(46, 172, 163)); // Sama dengan accent color
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    // 5. Validasi Visual (Border Merah)
    public static void setInvalidBorder(JComponent comp, boolean isInvalid) {
        if (isInvalid) {
            comp.putClientProperty("JComponent.outline", "error"); // FlatLaf native error outline
            comp.repaint();
        } else {
            comp.putClientProperty("JComponent.outline", null); // Clear outline
            comp.repaint();
        }
    }
}
